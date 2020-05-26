package com.redsponge.irrigation;

@FunctionalInterface
public interface CommunicationCallback {
    void handle(String[] returned);
}
