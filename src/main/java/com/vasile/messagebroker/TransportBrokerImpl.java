package com.vasile.messagebroker;

import com.vasile.models.Message;
import com.vasile.transport.TransportServer;
import com.vasile.models.MessageInfo;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class TransportBrokerImpl extends Observable implements TransportBroker, Observer {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    private TransportServer transportServer;

    // queues
    private static MessageQueue generalMessageQueue = new MessageQueue();


    public TransportBrokerImpl() {
        LOGGER.info("Started");
        transportServer = new TransportServer(8878);
    }

    @Override
    public void update(Observable o, Object arg) {
        MessageInfo inputInfo = (MessageInfo) arg;

        LOGGER.info("update with arg - " + arg);
        treatMessageInput(inputInfo);
    }




    public void startBrocker() {
        boolean isStopped = false;
        while (!isStopped) {
            String userInput = new Scanner(System.in).nextLine();

            switch (userInput.toUpperCase().trim()) {
                case "LS":
                    System.out.println("List all the messages:");
                    generalMessageQueue.getQueue().forEach(System.out::println);
                    break;
                case "SEND":
                    sendToAll(generalMessageQueue.pop());
                    break;
                case "EXIT":
                    close();
                    isStopped = true;
                    break;
                default:
                    LOGGER.error("No such command");
                    break;
            }

        }

    }


    public void treatMessageInput(MessageInfo inputInfo) {
        switch (inputInfo.getCommandType()) {
            case GET:
                treatGet(inputInfo.getId());
                break;
            case PUT:
                treatPut(inputInfo.getMessage());
                break;
            default:
                LOGGER.error("Something wrong with the command-type");
                break;
        }

    }

    private void treatGet(Integer id) {
        send(id, generalMessageQueue.pop());
    }

    private void treatPut(Message message) {
        generalMessageQueue.push(message);
    }


    @Override
    public void listenToMessages() {
        transportServer.addObserver(this);
    }

    @Override
    public void sendToAll(Message message) {
        byte[] serializedMessage = SerializationUtils.serialize(message);
        transportServer.sendToAllClients(serializedMessage);
    }

    @Override
    public void send(Integer id, Message message) {
        byte[] serializedMessage = SerializationUtils.serialize(message);
        transportServer.send(id, serializedMessage);
    }

    @Override
    public void close() {
        transportServer.stop();
    }


}
