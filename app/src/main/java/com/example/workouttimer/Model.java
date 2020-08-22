package com.example.workouttimer;

public class Model {
    private int id, work_time, rest_time, number, set_count, set_during;
    private String name;

    //constructor
    public Model(int id,String name, int work_time, int rest_time, int number, int set_count, int set_during) {
        this.id = id;
        this.name = name;
        this.work_time = work_time;
        this.rest_time = rest_time;
        this.number = number;
        this.set_count = set_count;
        this.set_during = set_during;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkTime() {
        return work_time;
    }

    public void setWorkTime(int work_time) {
        this.work_time = work_time;
    }

    public int getRestTime() {
        return rest_time;
    }

    public void setRestTime(int rest_time) {
        this.rest_time = rest_time;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSetCount() {
        return set_count;
    }

    public void setSetCount(int set_count) {
        this.set_count = set_count;
    }

    public int getSetDuring() {
        return set_during;
    }

    public void setSetDuring(int set_during) {
        this.set_during = set_during;
    }
}
