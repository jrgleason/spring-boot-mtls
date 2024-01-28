package org.gleason.ssl.democlient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "client.trust-store")
@Getter
@Setter
public class TrustStoreConfig extends KeyStoreConfig{

}