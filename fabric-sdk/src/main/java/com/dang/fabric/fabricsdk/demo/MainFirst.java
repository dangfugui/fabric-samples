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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.exception.ServiceDiscoveryException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
// https://help.aliyun.com/document_detail/141500.html?spm=a2c4g.11186623.6.733.2805469a3BX9XJ
public class MainFirst {

    private static final Log logger = LogFactory.getLog(MainFirst.class);
    private static final long waitTime = 6000;
    private static String connectionProfilePath;

    private static String channelName = "mychannel";
//    private static String userName = "admin";
//    private static String secret = "adminpw";
    private static String chaincodeName = "mycc";
    private static String chaincodeVersion = "1.0";

//
//    Path networkConfigPath = Paths.get("..", "..", "first-network", "connection-org1.yaml");
//
//    Gateway.Builder builder = Gateway.createBuilder();
//		builder.identity(wallet, "user1").networkConfig(networkConfigPath).discovery(true);

    public static void main(String[] args) {

//        connectionProfilePath = System.getProperty("user.dir") + "/connection-profile-standard.yaml";
        // load a CCP
        InputStream in = MainApi.class.getResourceAsStream("/first-connection-org1.yaml");
        try {
            NetworkConfig networkConfig = NetworkConfig.fromYamlStream(in);
            // set netty channel builder options to avoid error "gRPC message exceeds maximum size"
            for (String peerName : networkConfig.getPeerNames()) {
                Properties peerProperties = networkConfig.getPeerProperties(peerName);
                if (peerProperties == null) {
                    peerProperties = new Properties();
                }
                peerProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 100*1024*1024);
                networkConfig.setPeerProperties(peerName, peerProperties);
            }
            for (String ordererName : networkConfig.getOrdererNames()) {
                Properties ordererProperties = networkConfig.getOrdererProperties(ordererName);
                if (ordererProperties == null) {
                    ordererProperties = new Properties();
                }
                ordererProperties.put("grpc.NettyChannelBuilderOption.maxInboundMessageSize", 100*1024*1024);
                networkConfig.setOrdererProperties(ordererName, ordererProperties);
            }
//            NetworkConfig.OrgInfo clientOrg = networkConfig.getClientOrganization();
//            NetworkConfig.CAInfo caInfo = clientOrg.getCertificateAuthorities().get(0);
//            FabricUser user = getFabricUser(clientOrg, caInfo);
//            User user = networkConfig.getPeerAdmin();
            User user = buildUserByFile();
            HFClient client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
//            client.setUserContext(networkConfig.getPeerAdmin());
            client.setUserContext(user);
            Channel channel = client.loadChannelFromConfig(channelName, networkConfig);

            channel.initialize();

            channel.registerBlockListener(blockEvent -> {
                logger.info(String.format("Receive block event (number %s) from %s", blockEvent.getBlockNumber(), blockEvent.getPeer()));
            });
            printChannelInfo(client, channel);
            executeChaincode(client, channel);

            logger.info("Shutdown channel.");
            channel.shutdown(true);

        } catch (Exception e) {
            logger.error("exception", e);
        }
    }



    private static void lineBreak() {
        logger.info("=============================================================");
    }

    private static void executeChaincode(HFClient client, Channel channel) throws
            ProposalException, InvalidArgumentException, UnsupportedEncodingException, InterruptedException,
            ExecutionException, TimeoutException, ServiceDiscoveryException
    {
        lineBreak();
        ChaincodeExecuter executer = new ChaincodeExecuter(chaincodeName, chaincodeVersion);
        String key = "java_a1";
        executer.executeTransaction(client, channel, false,"query", key);

//        String newValue = String.valueOf(new Random().nextInt(1000));
        executer.executeTransaction(client, channel, true,"set", key, "a1");
        executer.executeTransaction(client, channel, false,"get", key);

        lineBreak();
        executer.executeTransaction(client, channel, true,"set", key, "a2");
        executer.executeTransaction(client, channel, false,"get", key);
        executer.executeTransaction(client, channel, false,"history", key);
        executer.executeTransaction(client, channel, false,"cinfo");



    }
    private static void printChannelInfo(HFClient client, Channel channel) throws
            ProposalException, InvalidArgumentException, IOException
    {
        lineBreak();
        BlockchainInfo channelInfo = channel.queryBlockchainInfo();

        logger.info("Channel height: " + channelInfo.getHeight());
        for (long current = channelInfo.getHeight() - 1; current > -1; --current) {
            BlockInfo returnedBlock = channel.queryBlockByNumber(current);
            final long blockNumber = returnedBlock.getBlockNumber();

            logger.info(String.format("Block #%d has previous hash id: %s", blockNumber, Hex.encodeHexString(returnedBlock.getPreviousHash())));
            logger.info(String.format("Block #%d has data hash: %s", blockNumber, Hex.encodeHexString(returnedBlock.getDataHash())));
            logger.info(String.format("Block #%d has calculated block hash is %s",
                    blockNumber, Hex.encodeHexString(SDKUtils.calculateBlockHash(client,blockNumber, returnedBlock.getPreviousHash(), returnedBlock.getDataHash()))));
        }

    }

    private static User buildUserByFile() {
        String bashPath = "C:\\Users\\admin\\go\\src\\github.com\\hyperledger\\fabric-samples\\first-network\\crypto-config\\peerOrganizations\\org1.example.com\\users\\User1@org1.example.com\\";
        String keyFile = bashPath+"msp\\keystore\\priv_sk";
        String certFile = bashPath +"msp\\signcerts\\User1@org1.example.com-cert.pem";
        FabricUser user = new FabricUser("PeerUser1_Org1MSP_Org1","Org1MSP",keyFile,certFile);
        return user;
    }


//    private static FabricUser getFabricUser(NetworkConfig.OrgInfo clientOrg, NetworkConfig.CAInfo caInfo) throws
//            MalformedURLException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException, InfoException,
//            EnrollmentException
//    {
//        HFCAClient hfcaClient = HFCAClient.createNewInstance(caInfo);
//        HFCAInfo cainfo = hfcaClient.info();
//        lineBreak();
//        logger.info("CA name: " + cainfo.getCAName());
//        logger.info("CA version: " + cainfo.getVersion());
//
//        // Persistence is not part of SDK.
//
//        logger.info("Going to enroll user: " + userName);
//        Enrollment enrollment = hfcaClient.enroll(userName, secret);
//        logger.info("Enroll user: " + userName +  " successfully.");
//
//        FabricUser user = new FabricUser();
//        user.setMspId(clientOrg.getMspId());
//        user.setName(userName);
//        user.setOrganization(clientOrg.getName());
//        user.setEnrollment(enrollment);
//        return user;
//    }
}
