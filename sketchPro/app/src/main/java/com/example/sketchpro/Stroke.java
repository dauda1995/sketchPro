package com.example.sketchpro;

import android.graphics.Path;

public class Stroke {
    public int id;
    public int color;

    // width of the stroke
    public int strokeWidth;
    public float x, y, radius;

    // a Path object to
    // represent the path drawn
    public Path path;

    public Stroke(int color, int strokeWidth, Path path) {
        id = 1;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;

    }

    public Stroke(int color, int strokeWidth, float x, float y, float radius) {
        id = 2;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
}
