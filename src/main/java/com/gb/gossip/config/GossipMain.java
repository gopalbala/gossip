package com.gb.gossip.config;

import com.gb.gossip.config.service.GossipService;

import java.net.InetSocketAddress;
import java.time.Duration;

public class GossipMain {
    public static void main(String[] args) {
        GossipConfig gossipConfig = new GossipConfig(
                Duration.ofSeconds(3),
                Duration.ofSeconds(3),
                Duration.ofSeconds(2),
                Duration.ofSeconds(1),
                3
        );
        GossipService initialNode = new GossipService
                (new InetSocketAddress("127.0.0.1", 9090), gossipConfig);

        initialNode.setOnNewMemberHandler((inetSocketAddress) -> {
            System.out.println("Connected to " +
                    inetSocketAddress.getHostName() + ":"
                    + inetSocketAddress.getPort());
        });
        initialNode.start();
        for (int i = 0; i < 10; i++) {
            GossipService gossipService = new GossipService
                    (new InetSocketAddress("127.0.0.1", 9091 + i),
                            new InetSocketAddress("127.0.0.1", 9090 + i - 1), gossipConfig);
            gossipService.start();
        }
    }
}
