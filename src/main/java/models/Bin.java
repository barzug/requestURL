package models;

import utils.StringGenerate;

import java.util.Date;

public class Bin {
    private String name;
    private long created;
    private long updated;
    private int requestCount;

    public Bin() {
        long now = new Date().getTime();
        created = now;
        updated = now;
        requestCount = 0;
        name = StringGenerate.Generate(6);
    }

    public String getName() {
        return name;
    }
}
