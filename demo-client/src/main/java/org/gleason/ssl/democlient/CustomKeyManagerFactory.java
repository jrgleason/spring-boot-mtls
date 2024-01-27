package org.gleason.ssl.democlient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManagerFactorySpi;

public class CustomKeyManagerFactory extends KeyManagerFactory {
    public CustomKeyManagerFactory(KeyManagerFactory delegate, String alias) {
        super(
                new CustomKeyManagerFactorySpi(alias, delegate),
                delegate.getProvider(),
                delegate.getAlgorithm()
        );
    }
}
