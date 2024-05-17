package com.kyljmeeski.vacanciesbot.saver;

import com.kyljmeeski.rabbitmqwrapper.Consumer;
import com.kyljmeeski.rabbitmqwrapper.PlainConsumer;
import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.kyljmeeski.rabbitmqwrapper.RabbitQueue;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try {
            Connection connection = factory.newConnection();

            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase database = mongoClient.getDatabase("vacancies");
            Vacancies vacancies = new Vacancies(database);

            Queues queues = new Queues(connection);
            try {
                RabbitQueue toStoreQueue = queues.declare(
                        "vacancies-to-store", false, false, false, null
                );
                Consumer consumer = new PlainConsumer(connection, toStoreQueue, new VacancyStoreJob(vacancies));
                consumer.startConsuming();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
