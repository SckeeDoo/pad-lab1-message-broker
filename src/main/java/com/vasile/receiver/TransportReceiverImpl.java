package com.vasile.receiver;

import com.vasile.models.CommandType;
import com.vasile.models.MessageInfo;
import com.vasile.models.Message;
import com.vasile.transport.TransporterClient;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class TransportReceiverImpl extends Observable implements Observer, TransportReceiver {

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private TransporterClient transportClient;

    public TransportReceiverImpl() {
        LOGGER.info("Started");
        transportClient = new TransporterClient("localhost", 8878);
    }

    @Override
    public void listenFromBroker() {
        transportClient.addObserver(this);
    }

    @Override
    public void send(MessageInfo messageInfo) {
        try {
            byte[] serializedMessage = SerializationUtils.serialize(messageInfo);
            transportClient.send(serializedMessage);
            LOGGER.info("Message successfully sent message to broker");
        } catch (IOException e) {
            LOGGER.error("Problem on sending message to broker");
        }
    }

    @Override
    public void close() {
        transportClient.stop();
    }

    public void treatSend() {
        System.out.println("Type command: (GET, PUT)");
        CommandType commandType;
        try {
            commandType = CommandType.valueOf(new Scanner(System.in).nextLine().toUpperCase());
        } catch (IllegalArgumentException iae) {
            LOGGER.error("There is no such command-type, please insert message information again:");
            return;
        }

        Message message = null;
        if (commandType.equals(CommandType.PUT)) {
            System.out.println("Insert message to Broker:");
            String messageContent = new Scanner(System.in).nextLine();
            message = new Message(messageContent);
        }
        send(new MessageInfo(message, commandType));
    }


    @Override
    public void update(Observable o, Object arg) {
        Message message =  SerializationUtils.deserialize((byte[]) arg);

        setChanged();
        notifyObservers(message);
    }
}
