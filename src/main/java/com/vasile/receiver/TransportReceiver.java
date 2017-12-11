package com.vasile.receiver;

import com.vasile.models.MessageInfo;

public interface TransportReceiver {
    void listenFromBroker();
    void send(MessageInfo messageInfo);
    void close();
}
