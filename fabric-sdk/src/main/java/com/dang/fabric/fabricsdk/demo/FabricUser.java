/*
 *  Copyright 2018 Aliyun.com All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.dang.fabric.fabricsdk.demo;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;

/**
 * User 是一个 Fabric 应用程序需要自行实现的接口。官方在测试用例中给出了一份示例代码：
 * fabric-sdk-java/src/test/java/org/hyperledger/fabric/sdkintegration/SampleUser.java
 */
public class FabricUser implements User {

    private String name;
    private String organization;
    private String mspId;
    private String account;
    private Set<String> roles;
    Enrollment enrollment;
    private String affiliation;

    public FabricUser(){
        super();
    }

    public FabricUser(String name, String mspId,String keyFile, String certFile) {
        this.name = name;
        this.mspId = mspId;
        try {
            loadFromPemFile(keyFile,certFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFromPemFile(String keyFile, String certFile) throws Exception { /*见下文说明*/
        byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));     //载入私钥PEM文本
        byte[] certPem = Files.readAllBytes(Paths.get(certFile));   //载入证书PEM文本
        CryptoPrimitives suite = new CryptoPrimitives();            //载入密码学套件
        PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);    //将PEM文本转换为私钥对象
        enrollment =new X509Enrollment(privateKey,new String(certPem));  //创建并返回X509Enrollment对象
    }



    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public String getName() {
        return this.name;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return this.roles;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAffiliation() {
        return this.affiliation;
    }

    public Enrollment getEnrollment() {
        return this.enrollment;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getMspId() {
        return this.mspId;
    }
}
