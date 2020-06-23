package com.dang.fabric.fabricsdk.client;


import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;

import java.util.Collection;

public class App2 {
    public static void main(String[] args) throws Exception{
        System.out.println("counter app");
        //创建User实例
        String mspPath = "D:\\Data\\window\\User\\go\\src\\github.com\\hyperledger\\fabric-samples\\nucc-fabric\\crypto-config\\peerOrganizations\\org1.nucc.com\\users\\Admin@org1.nucc.com\\msp\\";
        String keyFile = mspPath + "\\keystore\\b873ec2f4d29213f3d9641930ae2e0b3a55aa8c58b8bd537835b5e5013c67851_sk";
        String certFile = mspPath+ "\\signcerts\\Admin@org1.nucc.com-cert.pem";
        LocalUser user = new LocalUser("Org1MSP","Org1MSP",keyFile,certFile);

        //创建HFClient实例
        HFClient client = HFClient.createNewInstance();
        client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        client.setUserContext(user);

        //创建通道实例
        Channel channel = client.newChannel("nuccchannel");
        Peer peer = client.newPeer("peer0.org1.nucc.com","grpc://localhost:7051");
        channel.addPeer(peer);
        Orderer orderer = client.newOrderer("orderer.nucc.com","grpc://localhost:7050");
        channel.addOrderer(orderer);
        channel.initialize();

        //查询链码
        QueryByChaincodeRequest req = client.newQueryProposalRequest();
        ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        req.setChaincodeID(cid);
        req.setFcn("get");
        req.setArgs("c");
        ProposalResponse[] rsp = channel.queryByChaincode(req).toArray(new ProposalResponse[0]);
        System.out.format("rsp message => %s\n",rsp[0].getProposalResponse().getResponse().getPayload().toStringUtf8());

        //提交链码交易
        TransactionProposalRequest req2 = client.newTransactionProposalRequest();
        req2.setChaincodeID(cid);
        req2.setFcn("get");
        req2.setArgs("c");
        Collection<ProposalResponse> rsp2 = channel.sendTransactionProposal(req2);
        TransactionEvent event = channel.sendTransaction(rsp2).get();
        System.out.format("txid: %s\n", event.getTransactionID());
        System.out.format("valid: %b\n", event.isValid());
    }
}
