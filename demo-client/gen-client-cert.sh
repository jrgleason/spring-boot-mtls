#!/bin/bash

# Variables
alias=client
keyalg=RSA
keysize=2048
storetype=PKCS12
validity=3650
keystore=./src/main/resources/ssl/client.p12
certificate=./src/main/resources/ssl/client1.cer
truststore=./src/main/resources/ssl/truststore.p12
server_cert=../demo-server/src/main/resources/ssl/server.cer
server_truststore=../demo-server/src/main/resources/ssl/truststore.p12
password=${KEY_STORE_PASSWORD:-changeit}

rm -rf ./src/main/resources/ssl
mkdir -p ./src/main/resources/ssl

# Generate a new key pair and store it in a new keystore
keytool -genkeypair -alias $alias -keyalg $keyalg -keysize $keysize -storetype $storetype -keystore $keystore -validity $validity -storepass $password -dname "CN=client"
keytool -genkeypair -alias client2 -keyalg $keyalg -keysize $keysize -storetype $storetype -keystore $keystore -validity $validity -storepass $password -dname "CN=client2"
keytool -genkeypair -alias client3 -keyalg $keyalg -keysize $keysize -storetype $storetype -keystore $keystore -validity $validity -storepass $password -dname "CN=client3"


# Export the certificate from the keystore
keytool -exportcert -alias $alias -file $certificate -keystore $keystore -storepass $password

# Import your cert to the server so they know who you are
keytool -importcert -file $certificate -alias something-else -keystore $server_truststore -noprompt -storepass $password

# Take in the server cert so you know who they are
keytool -importcert -file $server_cert -alias server -keystore $truststore -noprompt -storepass $password

echo "Client keystore"
keytool -list -keystore $keystore -storepass $password
keytool -list -keystore $truststore -storepass $password
keytool -list -keystore $server_truststore -storepass $password
