package cfs;

import com.raylib.Raylib;

public class CircleFighter {
    public final String name;
    public final Raylib.Color color;
    public final float radius;

    public int maxHp = 100;
    public int hp    = 100;

    public float x, y;
    public float vx, vy;

    public boolean hasSaw = false;
    public float sawAngle = 0f; // degrees

    public CircleFighter(String name, Raylib.Color color, float radius) {
        this.name = name;
        this.color = color;
        this.radius = radius;
    }
}

