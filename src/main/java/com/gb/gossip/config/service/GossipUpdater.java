package com.gb.gossip.config.service;

import java.net.InetSocketAddress;

public interface GossipUpdater {
    void update(InetSocketAddress inetSocketAddress);
}
