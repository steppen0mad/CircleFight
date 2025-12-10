package cfs;

import static com.raylib.Raylib.*;
import com.raylib.Raylib;

public class DamageText {
    public float x, y;
    public String text;
    public float age = 0f;
    public final float life;   // seconds
    public float vy;           // pixels/sec upward

    public DamageText(float x, float y, String text, float life, float vy) {
        this.x = x; this.y = y; this.text = text; this.life = life; this.vy = vy;
    }

    /** @return true if still alive */
    public boolean update(float dt) {
        age += dt;
        y += vy * dt;
        return age < life;
    }
    
    public void draw() {
    float alpha = 1f - (age / life);
    if (alpha < 0f) alpha = 0f; if (alpha > 1f) alpha = 1f;

    int fs = 26;
    int tw = MeasureText(text, fs);
    int tx = (int)(x - tw/2f);
    int ty = (int)(y);

    Raylib.Color col = new Raylib.Color()
            .r((byte)255).g((byte)220).b((byte)80).a((byte)(alpha * 255));
    Raylib.Color shadow = new Raylib.Color()
            .r((byte)0).g((byte)0).b((byte)0).a((byte)(alpha * 160));

    DrawText(text, tx+1, ty+1, fs, shadow);
    DrawText(text, tx,   ty,   fs, col);
    }

}

