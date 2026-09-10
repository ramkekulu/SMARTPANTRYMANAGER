package com.esther.smartpantrymanager;

public class RecipeItem {

    private int id;
    private String name;
    private String method;

    public RecipeItem(int id, String name, String method) {
        this.id = id;
        this.name = name;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }
}

