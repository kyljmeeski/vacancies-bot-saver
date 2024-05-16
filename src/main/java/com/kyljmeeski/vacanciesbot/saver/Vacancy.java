package com.kyljmeeski.vacanciesbot.saver;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Vacancy {

    private final JsonObject vacancy;

    public Vacancy(String source) {
        this(new Gson().fromJson(source, JsonObject.class));
    }

    public Vacancy(JsonObject vacancy) {
        this.vacancy = vacancy;
    }

    public String id() {
        return vacancy.get("id").getAsString();
    }

    public Optional<String> title() {
        if (vacancy.get("title") != null) {
            return Optional.of(vacancy.get("title").getAsString());
        }
        return Optional.empty();
    }

    public Optional<String> company() {
        if (vacancy.get("company") != null) {
            return Optional.of(vacancy.get("company").getAsString());
        }
        return Optional.empty();
    }

    public Optional<String> type() {
        if (vacancy.get("type") != null) {
            return Optional.of(vacancy.get("type").getAsString());
        }
        return Optional.empty();
    }

    public Optional<String> salary() {
        if (vacancy.get("salary") != null) {
            return Optional.of(vacancy.get("salary").getAsString());
        }
        return Optional.empty();
    }

    public Optional<String> description() {
        if (vacancy.get("description") != null) {
            return Optional.of(vacancy.get("description").getAsString());
        }
        return Optional.empty();
    }

    public Map<String, String> contacts() {
        Map<String, String> contacts = new HashMap<>();
        JsonArray contactsArray = vacancy.getAsJsonArray("contacts");
        for (JsonElement contactElement : contactsArray) {
            contacts.put(
                    contactElement.getAsJsonObject().get("type").getAsString(),
                    contactElement.getAsJsonObject().get("contact").getAsString()
            );
        }
        return contacts;
    }

    public String link() {
        return vacancy.get("link").getAsString();
    }

}
