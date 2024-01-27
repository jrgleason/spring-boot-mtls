package org.gleason.ssl.democlient;


import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobExec implements JobExecutionListener {

    @Override
    public void beforeJob(org.springframework.batch.core.JobExecution jobExecution) {
        System.out.println("Before Job");
    }

    @Override
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
        System.out.println("After Job");
    }
}