package com.vasile.messagebroker;

import com.vasile.models.Message;

public interface TransportBroker {
    void listenToMessages();
    void sendToAll(Message message);
    void send(Integer id, Message message);
    void close();
}
