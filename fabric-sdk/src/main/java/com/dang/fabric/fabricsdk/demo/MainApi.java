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

public class MainApi {

    private static final Log logger = LogFactory.getLog(MainApi.class);
    private static final long waitTime = 6000;

    private static String channelName = "myc";
//    private static String userName = "admin";
//    private static String secret = "adminpw";
    private static String chaincodeName = "mycc";
    private static String chaincodeVersion = "0";

    public static void main(String[] args) {
        InputStream in = MainApi.class.getResourceAsStream("/network-config.yaml");
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

            HFClient client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            client.setUserContext(networkConfig.getPeerAdmin());

            Channel channel = client.loadChannelFromConfig(channelName, networkConfig);

            channel.initialize();

            channel.registerBlockListener(blockEvent -> {
                logger.info(String.format("Receive block event (number %s) from %s", blockEvent.getBlockNumber(), blockEvent.getPeer()));
            });
//            printChannelInfo(client, channel);
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


        executer.executeTransaction(client, channel, false,"get", "c");

        lineBreak();
        executer.executeTransaction(client, channel, true,"set", "c", "cc2");
        executer.executeTransaction(client, channel, false,"get", "c");

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
