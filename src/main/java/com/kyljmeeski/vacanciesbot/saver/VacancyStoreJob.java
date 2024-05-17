package com.kyljmeeski.vacanciesbot.saver;

import com.kyljmeeski.rabbitmqwrapper.Producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class VacancyStoreJob implements Consumer<String> {

    private final Vacancies vacancies;
    private final Producer producer;

    public VacancyStoreJob(Vacancies vacancies, Producer producer) {
        this.vacancies = vacancies;
        this.producer = producer;
    }

    @Override
    public void accept(String message) {
        Vacancy vacancy = new Vacancy(message);
        boolean saved = vacancies.add(vacancy);
        if (saved) {
            try {
                producer.produce(vacancy.toString());
                System.out.println(vacancy.id() + " -> " + vacancy.id());
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
