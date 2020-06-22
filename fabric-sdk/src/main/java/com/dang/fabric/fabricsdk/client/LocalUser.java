package com.dang.fabric.fabricsdk.client;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;
// https://blog.csdn.net/shebao3333/article/details/89469140
public class LocalUser implements User {             //实现User接口
    private String name;
    private String mspId;
    private String keyFile;
    private String certFile;
    private Enrollment enrollment;

    public LocalUser(String name, String mspId,String keyFile, String certFile) {
        this.name = name;
        this.mspId = mspId;
        this.keyFile = keyFile;
        this.certFile = certFile;
        try {
            loadFromPemFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromPemFile() throws Exception { /*见下文说明*/
        byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));     //载入私钥PEM文本
        byte[] certPem = Files.readAllBytes(Paths.get(certFile));   //载入证书PEM文本
        CryptoPrimitives suite = new CryptoPrimitives();            //载入密码学套件
        PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);    //将PEM文本转换为私钥对象
        enrollment =new X509Enrollment(privateKey,new String(certPem));  //创建并返回X509Enrollment对象
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return null;
    }

    @Override
    public String getMspId() {
        return mspId;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public String getAffiliation() {
        return null;
    }
}
