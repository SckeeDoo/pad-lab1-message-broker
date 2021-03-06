package com.vasile.receiver;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class Receiver implements Observer {
    private static final Logger LOGGER = Logger.getLogger(Receiver.class);
    private static final TransportReceiverImpl transport = new TransportReceiverImpl();

    private Receiver() {
        transport.listenFromBroker();
        transport.addObserver(this);
    }

    public static void main(String[] args) {
        LOGGER.info("Receiver started");

        new Receiver(); // activate observer

        boolean isStopped = false;
        while (!isStopped) {
            System.out.println("Commands: SEND, EXIT");
            System.out.println("Choose command:");
            String userInput = new Scanner(System.in).nextLine();

            switch (userInput.toUpperCase()) {
                case "SEND":
                    transport.treatSend();
                    break;
                case "EXIT":
                    isStopped = true;
                    transport.close();
                    break;
                default:
                    break;
            }
        }

        LOGGER.info("Receiver Stopped");
    }


    @Override
    public void update(Observable o, Object arg) {
        LOGGER.info("Received message: " + arg);
    }

}
