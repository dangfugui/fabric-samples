package com.dang.fabric.fabricsdk.client;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.PrivateKey;
// https://blog.csdn.net/baidu_37379451/article/details/84589633
public class FCEnrollment implements Enrollment, Serializable {

    private static final long serialVersionUID = -4274445336349657179L;
    private PrivateKey key;
    private String cert;

    FCEnrollment(String signedPem, String key) {
        PrivateKey privateKey = null;
        try {
            privateKey = getPrivateKeyFromString(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.key = privateKey;
        this.cert = signedPem;
    }

    public PrivateKey getKey() {
        return key;
    }

    public String getCert() {
        return cert;
    }

    private static PrivateKey getPrivateKeyFromString(String data)
            throws IOException {

        final Reader pemReader = new StringReader(data);

        final PrivateKeyInfo pemPair;
        try (PEMParser pemParser = new PEMParser(pemReader)) {
            pemPair = (PrivateKeyInfo) pemParser.readObject();
        }

        return new JcaPEMKeyConverter().getPrivateKey(pemPair);
    }
}