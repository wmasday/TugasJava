package com.bookspk;

public class Criteria {
    private int id;
    private String code;
    private String name;
    private float bobot;

    public Criteria() {}
    public Criteria(int id, String code, String name, float bobot) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.bobot = bobot;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public float getBobot() { return bobot; }
    public void setBobot(float bobot) { this.bobot = bobot; }
} 