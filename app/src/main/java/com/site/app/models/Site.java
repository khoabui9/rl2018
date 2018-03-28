package com.site.app.models;

/**
 * Created by dell 5558 on 3/25/2018.
 */

public class Site {
    String name;
    int id;

    public  Site(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public Site() {
        name = "default";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
