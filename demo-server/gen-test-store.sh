#!/bin/bash

# Variables
alias=server
keyalg=RSA
keysize=2048
storetype=PKCS12
validity=3650
keystore=./src/main/resources/ssl/keystore.p12
certificate=./src/main/resources/ssl/server.cer
password=${KEY_STORE_PASSWORD:-changeit}

rm -rf ./src/main/resources/ssl
mkdir -p ./src/main/resources/ssl

# Generate a new key pair and store it in a new keystore
keytool -genkeypair -alias $alias -keyalg $keyalg -keysize $keysize -storetype $storetype -keystore $keystore -validity $validity -storepass $password -dname "CN=localhost"

# Export the certificate from the keystore
keytool -exportcert -alias $alias -file $certificate -keystore $keystore -storepass $password