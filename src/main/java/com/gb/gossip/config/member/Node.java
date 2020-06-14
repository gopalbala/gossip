package com.gb.gossip.config.member;

import com.gb.gossip.config.GossipConfig;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;


public class Node implements Serializable {

    private final InetSocketAddress address;
    private long heartbeatSequenceNumber = 0;
    private LocalDateTime lastUpdateTime = null;
    private boolean hasFailed = false;
    private GossipConfig config;

    public Node(InetSocketAddress address,
                long initialSequenceNumber,
                GossipConfig config) {
        this.address = address;
        this.heartbeatSequenceNumber = initialSequenceNumber;
        this.config = config;
        updateLastUpdateTime();
    }

    public void setConfig(GossipConfig config) {
        this.config = config;
    }

    public String getAddress() {
        return address.getHostName();
    }

    public InetAddress getInetAddress() {
        return address.getAddress();
    }

    public InetSocketAddress getSocketAddress() {
        return address;
    }

    public int getPort() {
        return address.getPort();
    }

    public String getUniqueId() {
        return address.toString();
    }

    public long getSequenceNumber() {
        return heartbeatSequenceNumber;
    }

    public void updateSequenceNumber(long newSequenceNumber) {
        if (newSequenceNumber > heartbeatSequenceNumber) {
            heartbeatSequenceNumber = newSequenceNumber;
            updateLastUpdateTime();
        }
    }

    public void updateLastUpdateTime() {
        lastUpdateTime = LocalDateTime.now();
    }

    public void incremenetSequenceNumber() {
        heartbeatSequenceNumber++;
        updateLastUpdateTime();
    }

    public void checkIfFailed() {
        LocalDateTime failureTime = lastUpdateTime.plus(config.failureTimeout);
        LocalDateTime now = LocalDateTime.now();

        hasFailed = now.isAfter(failureTime);
    }

    public boolean shouldCleanup() {
        if (hasFailed) {
            Duration cleanupTimeout = config.failureTimeout.plus(config.cleanupTimeout);
            LocalDateTime cleanupTime = lastUpdateTime.plus(cleanupTimeout);
            LocalDateTime now = LocalDateTime.now();

            return now.isAfter(cleanupTime);
        } else {
            return false;
        }
    }

    public boolean hasFailed() {
        return hasFailed;
    }

    public String getNetworkMessage() {
        return "[" + address.getHostName() +
                ":" + address.getPort() +
                "-" + heartbeatSequenceNumber + "]";
    }
}
