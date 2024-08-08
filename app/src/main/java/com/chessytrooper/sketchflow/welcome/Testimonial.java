package com.chessytrooper.sketchflow.welcome;

public class Testimonial {
    private String name;
    private String comment;

    public Testimonial(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }
}