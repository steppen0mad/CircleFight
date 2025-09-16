package cfs;

import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;

import com.raylib.Raylib;

public final class DrawUtil {
    private DrawUtil(){}

    /** Ring of outward spikes around a circle. */
    public static void drawSpikesRing(float cx, float cy, float radius,
                                      int spikes, float length) {
        for (int i = 0; i < spikes; i++) {
            float ang = (float)(i * (2*Math.PI / spikes));
            float sx = (float)(cx + Math.cos(ang) * radius);
            float sy = (float)(cy + Math.sin(ang) * radius);
            float ex = (float)(cx + Math.cos(ang) * (radius + length));
            float ey = (float)(cy + Math.sin(ang) * (radius + length));
            DrawLineEx(new Raylib.Vector2().x(sx).y(sy),
                       new Raylib.Vector2().x(ex).y(ey),
                       3f, YELLOW);
        }
    }

    /** Little gray star to represent a spike pickup. */
    public static void drawSpikePickupIcon(int cx, int cy, int r) {
        int spikes = 8;
        float inner = r * 0.45f, outer = r;
        for (int i=0;i<spikes;i++) {
            float a1 = (float)(2*Math.PI*i/spikes);
            float a2 = (float)(2*Math.PI*(i+0.5)/spikes);
            float a3 = (float)(2*Math.PI*(i+1)/spikes);
            Raylib.Vector2 p1 = new Raylib.Vector2().x((float)(cx + Math.cos(a1)*outer)).y((float)(cy + Math.sin(a1)*outer));
            Raylib.Vector2 p2 = new Raylib.Vector2().x((float)(cx + Math.cos(a2)*inner)).y((float)(cy + Math.sin(a2)*inner));
            Raylib.Vector2 p3 = new Raylib.Vector2().x((float)(cx + Math.cos(a3)*outer)).y((float)(cy + Math.sin(a3)*outer));
            DrawTriangle(p1, p2, p3, LIGHTGRAY);
        }
        DrawCircleLines(cx, cy, r, GRAY);
    }

    /** Simple heart (two circles + triangle). */
    public static void drawHeart(int cx, int cy, int r) {
        int rr = r;
        DrawCircle(cx - rr/2, cy - rr/4, rr/2f, RED);
        DrawCircle(cx + rr/2, cy - rr/4, rr/2f, RED);
        Raylib.Vector2 p1 = new Raylib.Vector2().x(cx - rr).y(cy - rr/8f);
        Raylib.Vector2 p2 = new Raylib.Vector2().x(cx + rr).y(cy - rr/8f);
        Raylib.Vector2 p3 = new Raylib.Vector2().x(cx).y(cy + rr);
        DrawTriangle(p1, p2, p3, RED);
        DrawTriangleLines(p1, p2, p3, MAROON);
    }
}

