#!/bin/bash

# Variables
alias=clientkey
keyalg=RSA
keysize=2048
storetype=PKCS12
validity=3650
keystore=./src/main/resources/ssl/client_keystore.p12
certificate=./src/main/resources/ssl/clientkey.cer
truststore=../demo-server/src/main/resources/ssl/truststore.p12

mkdir -p ./src/main/resources/ssl

# Generate a new key pair and store it in a new keystore
keytool -genkeypair -alias $alias -keyalg $keyalg -keysize $keysize -storetype $storetype -keystore $keystore -validity $validity

# Export the certificate from the keystore
keytool -exportcert -alias $alias -file $certificate -keystore $keystore
keytool -importcert -file $certificate -alias $alias -keystore $truststore