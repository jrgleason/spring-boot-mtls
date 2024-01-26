package org.gleason.ssl.demoserver;

import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.*;
import javax.sql.DataSource;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.SecureRandom;

@Configuration
@EnableBatchProcessing
public class JobConfig {


    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExec();
    }

    @Bean
    public Step step1(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager
    ) {
        return new StepBuilder("my-step", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    // Load the p12 file from the classpath
                    InputStream keystoreInputStream = new ClassPathResource("ssl/client.p12").getInputStream();

// Create a KeyStore containing our trusted CAs
                    KeyStore ks = KeyStore.getInstance("PKCS12");
                    ks.load(keystoreInputStream, "changeit".toCharArray());

                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                    keyManagerFactory.init(ks, "changeit".toCharArray());

                    InputStream truststoreInputStream = new ClassPathResource("ssl/truststore.p12").getInputStream();
                    KeyStore ts = KeyStore.getInstance("PKCS12");
                    ts.load(truststoreInputStream, "changeit".toCharArray());


                    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    trustManagerFactory.init(ts);


// Create a SslContextBuilder
                    SslContextBuilder sslContextBuilder = SslContextBuilder.forClient()
                            .keyManager(keyManagerFactory)
                            .trustManager(trustManagerFactory);
// Create a HttpClient that uses the custom SSLContext
                    HttpClient httpClient = HttpClient.create()
                            .secure(sslContextSpec -> {
                                try {
                                    sslContextSpec.sslContext(sslContextBuilder.build());
                                } catch (SSLException e) {
                                    throw new RuntimeException(e);
                                }
                            });

// Create a WebClient that uses the custom HttpClient
                    WebClient webClient = WebClient.builder()
                            .clientConnector(new ReactorClientHttpConnector(httpClient))
                            .build();

// Use the WebClient
                    String result = webClient.get()
                            .uri("https://localhost:8443/test")
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
                    System.out.println(result);
                    return null;
                }, transactionManager)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {
        return new JobBuilder("my-job", jobRepository)
                .preventRestart()
                .start(step1)
                .listener(jobExecutionListener())
                .build();
    }

    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
                .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
                .build();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource());
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(getJobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
