package cfs;

import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;

import com.raylib.Raylib; // for Raylib.Color

public final class HealthBar {
    private HealthBar() {}

    /** Simple filled bar with border + centered name label. */
    public static void draw(int x, int y, int w, int h,
                            String label, int hp, int maxHp,
                            Raylib.Color barColor) {
        // background track
        DrawRectangle(x, y, w, h, DARKGRAY);

        // fill
        float pct = maxHp <= 0 ? 0f : Math.max(0f, Math.min(1f, hp / (float)maxHp));
        int filled = (int)(w * pct);
        DrawRectangle(x, y, filled, h, barColor);

        // border
        DrawRectangleLines(x, y, w, h, BLACK);

        // label (white with subtle drop)
        int fs = 22;
        int tw = MeasureText(label, fs);
        int tx = x + (w - tw)/2;
        int ty = y + (h - fs)/2;
        DrawText(label, tx+1, ty+1, fs, BLACK);
        DrawText(label, tx,   ty,   fs, RAYWHITE);
    }
}

