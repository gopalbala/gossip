package com.gb.gossip;

import com.gb.gossip.config.GossipConfig;
import com.gb.gossip.service.GossipService;

import java.net.InetSocketAddress;
import java.time.Duration;

public class GossipMain {
    public static void main(String[] args) {
        GossipConfig gossipConfig = new GossipConfig(
                Duration.ofSeconds(3),
                Duration.ofSeconds(3),
                Duration.ofMillis(500),
                Duration.ofMillis(500),
                3
        );
        GossipService initialNode = new GossipService
                (new InetSocketAddress("127.0.0.1", 9090), gossipConfig);

        initialNode.setOnNewNodeHandler((inetSocketAddress) -> {
            System.out.println("Connected to " +
                    inetSocketAddress.getHostName() + ":"
                    + inetSocketAddress.getPort());
        });

        initialNode.setOnFailedNodeHandler((inetSocketAddress) -> {
            System.out.println("Node " + inetSocketAddress.getHostName() + ":"
                    + inetSocketAddress.getPort() + " failed");
        });

        initialNode.setOnRemoveNodeHandler((inetSocketAddress) -> {
            System.out.println("Node " + inetSocketAddress.getHostName() + ":"
                    + inetSocketAddress.getPort() + " removed");
        });

        initialNode.setOnRevivedNodeHandler((inetSocketAddress) -> {
            System.out.println("Node " + inetSocketAddress.getHostName() + ":"
                    + inetSocketAddress.getPort() + " revived");
        });

        initialNode.start();

        for (int i = 1; i <= 10; i++) {
            GossipService gossipService = new GossipService
                    (new InetSocketAddress("127.0.0.1", 9090 + i),
                            new InetSocketAddress("127.0.0.1", 9090 + i - 1), gossipConfig);
            gossipService.start();
        }
    }
}
