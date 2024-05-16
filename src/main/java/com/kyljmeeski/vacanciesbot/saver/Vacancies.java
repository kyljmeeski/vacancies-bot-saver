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

    public void add(Vacancy vacancy) {
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
        } catch (MongoWriteException exception) {
            System.out.println(vacancy.id() + " is already in db");
        }
    }

}
