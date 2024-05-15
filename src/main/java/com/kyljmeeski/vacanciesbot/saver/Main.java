package com.kyljmeeski.vacanciesbot.saver;

import com.kyljmeeski.rabbitmqwrapper.Consumer;
import com.kyljmeeski.rabbitmqwrapper.PlainConsumer;
import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.kyljmeeski.rabbitmqwrapper.RabbitQueue;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        Queues queues = new Queues(factory);
        try {
            RabbitQueue toStoreQueue = queues.declare(
                    "vacancies-to-store", false, false, false, null
            );
            Consumer consumer = new PlainConsumer(factory, toStoreQueue, System.out::println);
            consumer.startConsuming();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
