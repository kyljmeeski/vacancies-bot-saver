/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Amir Syrgabaev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.kyljmeeski.vacanciesbot.saver;

import com.kyljmeeski.rabbitmqwrapper.*;
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

            Exchanges exchanges = new Exchanges(connection);
            RabbitExchange exchange = exchanges.declare("vacancies", "direct");
            Queues queues = new Queues(connection);
            try {
                RabbitQueue toStoreQueue = queues.declare(
                        "vacancies-to-store", false, false, false, null
                );
                queues.declare(
                        "vacancies-to-notify", false, false, false, null
                ).bind(exchange, "to-notify");

                Producer producer = new PlainProducer(connection, exchange, "to-notify");

                Consumer consumer = new PlainConsumer(connection, toStoreQueue, new VacancyStoreJob(vacancies, producer));
                consumer.startConsuming();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
