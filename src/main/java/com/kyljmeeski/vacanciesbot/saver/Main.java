package com.kyljmeeski.vacanciesbot.saver;

import com.rabbitmq.client.ConnectionFactory;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
    }

}
