package com.dang.fabric.fabricsdk.client;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.security.PrivateKey;
import java.util.Set;
import java.util.UUID;

//改为直接传入string的key.
public class FCUserInfo implements User {
    protected String name;
    protected String mspid;
    private Set<String> roles;
    private String account;
    private String affiliation;
    private Enrollment enrollment;

    public FCUserInfo(String mspid, String pem, String key) {
        this.name= UUID.randomUUID().toString();
        this.mspid = mspid;
        this.enrollment = new FCEnrollment(pem, key);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMspid(String mspid) {
        this.mspid = mspid;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    /**
     * Get the name that identifies the user.
     *
     * @return the user name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the roles to which the user belongs.
     *
     * @return role names.
     */
    @Override
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * Get the user's account
     *
     * @return the account name
     */
    @Override
    public String getAccount() {
        return account;
    }

    /**
     * Get the user's affiliation.
     *
     * @return the affiliation.
     */
    @Override
    public String getAffiliation() {
        return affiliation;
    }

    /**
     * Get the user's enrollment certificate information.
     *
     * @return the enrollment information.
     */
    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    /**
     * Get the Membership Service Provider Identifier provided by the user's organization.
     *
     * @return MSP Id.
     */
    @Override
    public String getMspId() {
        return mspid;
    }
}

