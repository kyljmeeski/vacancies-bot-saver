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
