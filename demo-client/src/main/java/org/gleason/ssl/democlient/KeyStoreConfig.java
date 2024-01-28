package org.gleason.ssl.democlient;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "client.key-store")
@Getter
@Setter
public class KeyStoreConfig {
    private String location;
    private String password;
    private String alias;
    private String type = "PKCS12";

    public char[] getPassword() {
        return password.toCharArray();
    }
}