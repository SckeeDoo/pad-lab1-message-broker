package com.vasile.messagebroker;

import com.vasile.models.*;
import org.apache.log4j.Logger;

import java.util.*;

public class MessageBroker implements Observer {
    private final static Logger LOGGER = Logger.getLogger(MessageBroker.class.getName());
    public final static TransportBrokerImpl transport = new TransportBrokerImpl();



    private MessageBroker() {
        transport.listenToMessages();
        transport.addObserver(this);
    }

    public static void main(String[] args) {
        LOGGER.info("MessageBroker started!");
        System.out.println("Broker commands: LS, SEND, EXIT");

        new MessageBroker();

        transport.startBrocker();
        LOGGER.info("Close MessageBroker");

    }

    @Override
    public void update(Observable o, Object arg) {
        MessageInfo inputInfo = (MessageInfo) arg;
        LOGGER.info("update with arg - " + arg);
        transport.treatMessageInput(inputInfo);
    }



}
