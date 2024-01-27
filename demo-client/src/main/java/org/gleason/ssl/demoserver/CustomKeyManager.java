package org.gleason.ssl.demoserver;

import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;

public class CustomKeyManager extends X509ExtendedKeyManager {
    private final X509ExtendedKeyManager delegate;

    public CustomKeyManager(X509ExtendedKeyManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, javax.net.ssl.SSLEngine engine) {
        // Implement your custom logic here
        // For example, return the first alias that matches a specific condition
        return "client3";
    }

    // Delegate other methods to the original KeyManager
    @Override
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return delegate.getClientAliases(keyType, issuers);
    }

    @Override
    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
        return delegate.chooseClientAlias(keyType, issuers, socket);
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return delegate.getServerAliases(keyType, issuers);
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
        return delegate.chooseServerAlias(keyType, issuers, socket);
    }

    @Override
    public java.security.cert.X509Certificate[] getCertificateChain(String alias) {
        return delegate.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String s) {
        return delegate.getPrivateKey(s);
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] issuers, javax.net.ssl.SSLEngine engine) {
        return delegate.chooseEngineServerAlias(keyType, issuers, engine);
    }
}
