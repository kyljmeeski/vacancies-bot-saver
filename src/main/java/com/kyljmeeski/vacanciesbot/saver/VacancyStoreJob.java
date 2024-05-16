package com.kyljmeeski.vacanciesbot.saver;

import java.util.function.Consumer;

public class VacancyStoreJob implements Consumer<String> {

    private final Vacancies vacancies;

    public VacancyStoreJob(Vacancies vacancies) {
        this.vacancies = vacancies;
    }

    @Override
    public void accept(String message) {
        Vacancy vacancy = new Vacancy(message);
        vacancies.add(vacancy);
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
