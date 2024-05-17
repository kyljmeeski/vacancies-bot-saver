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

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Vacancies {

    private final MongoCollection<Document> vacancies;

    public Vacancies(MongoDatabase database) {
        this(database.getCollection("vacancies"));
    }

    public Vacancies(MongoCollection<Document> vacancies) {
        this.vacancies = vacancies;
    }

    public boolean add(Vacancy vacancy) {
        Document document = new Document("_id", vacancy.id());
        vacancy.title().ifPresent(title -> document.append("title", title));
        vacancy.company().ifPresent(company -> document.append("company", company));
        vacancy.type().ifPresent(type -> document.append("type", type));
        vacancy.salary().ifPresent(salary -> document.append("salary", salary));
        vacancy.description().ifPresent(description -> document.append("description", description));
        document.append("contacts", vacancy.contacts());
        document.append("link", vacancy.link());
        try {
            vacancies.insertOne(document);
            System.out.println(vacancy.id() + " saved to db");
            return true;
        } catch (MongoWriteException exception) {
            System.out.println(vacancy.id() + " is already in db");
            return false;
        }
    }

}
