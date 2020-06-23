package com.dang.fabric.fabricsdk.client;


import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.FabricProposal;
import org.hyperledger.fabric.protos.peer.FabricProposalResponse;
import org.hyperledger.fabric.protos.peer.Query;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.transaction.QueryInstalledChaincodesBuilder;
import org.hyperledger.fabric.sdk.transaction.TransactionContext;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.String.format;


public class HFClientTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    String ccName="mycc";
    String version="1.0";
    String path="chaincode/ecdspay/";
    public static HFClient client = null;
    public static Peer peer = null;
    public static Channel channel = null;
    public static Orderer orderer = null;
    public static String adminPem = "-----BEGIN CERTIFICATE-----\n" +
            "MIICNjCCAd2gAwIBAgIRAMnf9/dmV9RvCCVw9pZQUfUwCgYIKoZIzj0EAwIwgYEx\n" +
            "CzAJBgNVBAYTAlVTMRMwEQYDVQQIEwpDYWxpZm9ybmlhMRYwFAYDVQQHEw1TYW4g\n" +
            "RnJhbmNpc2NvMRkwFwYDVQQKExBvcmcxLmV4YW1wbGUuY29tMQwwCgYDVQQLEwND\n" +
            "T1AxHDAaBgNVBAMTE2NhLm9yZzEuZXhhbXBsZS5jb20wHhcNMTcxMTEyMTM0MTEx\n" +
            "WhcNMjcxMTEwMTM0MTExWjBpMQswCQYDVQQGEwJVUzETMBEGA1UECBMKQ2FsaWZv\n" +
            "cm5pYTEWMBQGA1UEBxMNU2FuIEZyYW5jaXNjbzEMMAoGA1UECxMDQ09QMR8wHQYD\n" +
            "VQQDExZwZWVyMC5vcmcxLmV4YW1wbGUuY29tMFkwEwYHKoZIzj0CAQYIKoZIzj0D\n" +
            "AQcDQgAEZ8S4V71OBJpyMIVZdwYdFXAckItrpvSrCf0HQg40WW9XSoOOO76I+Umf\n" +
            "EkmTlIJXP7/AyRRSRU38oI8Ivtu4M6NNMEswDgYDVR0PAQH/BAQDAgeAMAwGA1Ud\n" +
            "EwEB/wQCMAAwKwYDVR0jBCQwIoAginORIhnPEFZUhXm6eWBkm7K7Zc8R4/z7LW4H\n" +
            "ossDlCswCgYIKoZIzj0EAwIDRwAwRAIgVikIUZzgfuFsGLQHWJUVJCU7pDaETkaz\n" +
            "PzFgsCiLxUACICgzJYlW7nvZxP7b6tbeu3t8mrhMXQs956mD4+BoKuNI\n" +
            "-----END CERTIFICATE-----\n";
    public static String adminKey = "-----BEGIN PRIVATE KEY-----\n" +
            "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgXa3mln4anewXtqrM\n" +
            "hMw6mfZhslkRa/j9P790ToKjlsihRANCAARnxLhXvU4EmnIwhVl3Bh0VcByQi2um\n" +
            "9KsJ/QdCDjRZb1dKg447voj5SZ8SSZOUglc/v8DJFFJFTfygjwi+27gz\n" +
            "-----END PRIVATE KEY-----\n";
    public static String pem = "-----BEGIN CERTIFICATE-----\n" +
            "MIICZDCCAgqgAwIBAgIRAJdl11zCkPNfxpSf88Kava8wCgYIKoZIzj0EAwIwgYIx\n" +
            "CzAJBgNVBAYTAkNOMRIwEAYDVQQIEwlndWFuZ2RvbmcxETAPBgNVBAcTCHNoZW56\n" +
            "aGVuMSIwIAYDVQQKExlwbGF0Zm9ybS5iYW5rYWxsaWFuY2Uub3JnMSgwJgYDVQQD\n" +
            "Ex90bHNjYS5wbGF0Zm9ybS5iYW5rYWxsaWFuY2Uub3JnMB4XDTE4MDkyMDEwMTYw\n" +
            "OFoXDTI4MDkxNzEwMTYwOFowgYIxCzAJBgNVBAYTAkNOMRIwEAYDVQQIEwlndWFu\n" +
            "Z2RvbmcxETAPBgNVBAcTCHNoZW56aGVuMSIwIAYDVQQKExlwbGF0Zm9ybS5iYW5r\n" +
            "YWxsaWFuY2Uub3JnMSgwJgYDVQQDEx90bHNjYS5wbGF0Zm9ybS5iYW5rYWxsaWFu\n" +
            "Y2Uub3JnMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEIaCYjoh42oL7yyvWgufa\n" +
            "DbRWn8WuN4AdRrI4rVxuBChAxUoru8u6F8LPX783x0y0iFQFavviP0/0Sgo5fevx\n" +
            "cKNfMF0wDgYDVR0PAQH/BAQDAgGmMA8GA1UdJQQIMAYGBFUdJQAwDwYDVR0TAQH/\n" +
            "BAUwAwEB/zApBgNVHQ4EIgQgu6ELkHx4gBF9r2E1luR09N0LGtimYLZPNsZslI5K\n" +
            "8CkwCgYIKoZIzj0EAwIDSAAwRQIhAOHKOaN3c0UJCSIMIvF2BIzgwu9IttCw8GLi\n" +
            "6GD0ZNSNAiBnTgMAJc7kdot9LmXWiUrqoFRcuRpfcfO8ZsrFm0kVdA==\n" +
            "-----END CERTIFICATE-----\n";

    @BeforeClass
    public static void setupClient() {
        try {
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            FCUserInfo user = new FCUserInfo("SampleOrg", adminPem, adminKey);
            client.setUserContext(user);
            channel = client.newChannel("myc");
            Properties opts = new Properties();
//            opts.put("pemBytes", pem.getBytes());
//            opts.setProperty("trustServerCertificate", "true");
//            opts.put("clientKeyBytes", clientKey.getBytes());
//            opts.put("clientCertBytes", clientCert.getBytes());
//            peer = client.newPeer("peer0", "grpcs://peer0.platform.bankalliance.org:7051", opts);
            peer = client.newPeer("peer", "grpc://127.0.0.1:7051", opts);
            orderer = client.newOrderer("orderer", "grpc://127.0.0.1:7050", opts);

            channel.addPeer(peer, Channel.PeerOptions.createPeerOptions());
            channel.addOrderer(orderer);

            channel.initialize();
            System.out.println(channel.isShutdown());
//            channel.shutdown(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryInstalledChaincodes() {
        try {
            List<Query.ChaincodeInfo> chaincodeInfos = client.queryInstalledChaincodes(peer);
            if (!chaincodeInfos.isEmpty())
                System.out.println(chaincodeInfos.get(0).toString());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryChannels() {
        try {
            Set<String> channels = client.queryChannels(peer);
            if (channels != null)
                System.out.println(channels.toString());
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        } catch (ProposalException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testQueryBlock() {
        try {
            BlockInfo blockInfo = channel.queryBlockByNumber(peer, 0);
            System.out.println(blockInfo.getChannelId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //测试调用任一cc
    @Test
    public void testInvokeCC() {
        ChaincodeID.Builder ccBuilder=ChaincodeID.newBuilder();
        ccBuilder.setName(ccName);
//        ccBuilder.setVersion(version);
//        ccBuilder.setPath(path);
        String func="OrderListQuery";
        ArrayList<String> argList = new ArrayList<>();
        argList.add("");
        try {
            TransactionProposalRequest request = TransactionProposalRequest.newInstance(client.getUserContext());
            request.setFcn(func);
            request.setArgs(argList);
            request.setProposalWaitTime(15000);
            request.setChaincodeID(ccBuilder.build());
            Collection<ProposalResponse> proposalResponses = channel.sendTransactionProposal(request, Collections.singletonList(peer));
            if (null == proposalResponses) {
                Assert.fail("proposalResponses is null");
            }
            if (proposalResponses.size() != 1) {
                Assert.fail(format("Peer %s expected one response but got back %d  responses ", peer.getName(), proposalResponses.size()));
            }
            ProposalResponse proposalResponse = proposalResponses.iterator().next();
            FabricProposalResponse.ProposalResponse fabricResponse = proposalResponse.getProposalResponse();
            if (null == fabricResponse) {
                Assert.fail(format("Peer %s return with empty fabric response", peer.getName()));
            }
            final FabricProposalResponse.Response fabricResponseResponse = fabricResponse.getResponse();
            if (null == fabricResponseResponse) { //not likely but check it.
                Assert.fail(format("Peer %s return with empty fabricResponseResponse", peer.getName()));
            }
            if (200 != fabricResponseResponse.getStatus()) {
                Assert.fail(format("Peer %s  expected 200, actual returned was: %d. "
                        + fabricResponseResponse.getMessage(), peer.getName(), fabricResponseResponse.getStatus()));
            }
            System.out.println(fabricResponseResponse.getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
