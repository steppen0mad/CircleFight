package cfs;

import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;

public class BoxArena {
    public final int x, y, w, h;
    public final int wall; // thickness in pixels

    public BoxArena(int x, int y, int w, int h) {
        this(x, y, w, h, 8); // default thicker walls
    }

    public BoxArena(int x, int y, int w, int h, int wallThickness) {
        this.x = x; this.y = y; this.w = w; this.h = h;
        this.wall = wallThickness;
    }

    public void draw() {
        // Draw filled rectangles for thick walls (inside the bounds)
        DrawRectangle(x, y, w, wall, PURPLE);                 // top
        DrawRectangle(x, y + h - wall, w, wall, PURPLE);      // bottom
        DrawRectangle(x, y, wall, h, PURPLE);                 // left
        DrawRectangle(x + w - wall, y, wall, h, PURPLE);      // right
    }
}

