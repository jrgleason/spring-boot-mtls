package org.gleason.ssl.democlient;

import lombok.AllArgsConstructor;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.X509ExtendedKeyManager;
import java.util.Arrays;

@AllArgsConstructor
public class CustomKeyManagerFactorySpi extends KeyManagerFactorySpi {
    private final String alias;
    private final KeyManagerFactory delegate;
    @Override
    protected void engineInit(java.security.KeyStore keyStore, char[] chars) throws java.security.KeyStoreException, java.security.NoSuchAlgorithmException, java.security.UnrecoverableKeyException {
        this.delegate.init(keyStore, chars);
    }

    @Override
    protected void engineInit(javax.net.ssl.ManagerFactoryParameters managerFactoryParameters) throws java.security.InvalidAlgorithmParameterException {
        this.delegate.init(managerFactoryParameters);
    }

    @Override
    protected javax.net.ssl.KeyManager[] engineGetKeyManagers() {
        return Arrays.stream(this.delegate.getKeyManagers())
                .map(keyManager -> new CustomKeyManager(
                        (X509ExtendedKeyManager) keyManager,
                        this.alias )
                )
                .toArray(javax.net.ssl.KeyManager[]::new);
    }
}
