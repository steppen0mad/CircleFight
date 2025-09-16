package cfs;

import static com.raylib.Raylib.*;
import com.raylib.Raylib;
import java.util.Random;

public class Item {
    public enum Type { SAW, HEART }

    public final Type type;
    public float x, y;
    public float drawW, drawH;
    public float pickupRadius;

    public Item(Type t, float x, float y, float drawW, float drawH) {
        this.type = t; this.x = x; this.y = y;
        this.drawW = drawW; this.drawH = drawH;
        this.pickupRadius = 0.70f * 0.5f * Math.min(drawW, drawH);
    }

    public static Item spawnRandom(BoxArena ar, Random rng) {
        Type t = rng.nextBoolean() ? Type.SAW : Type.HEART;

        float w = (t == Type.SAW) ? CFSApp.SAW_PICKUP_SIZE : CFSApp.HEART_PICKUP_SIZE;
        float h = w;

        float halfX = w * 0.5f, halfY = h * 0.5f;

        // 🔧 margin to keep items away from the border
        final float MARGIN = 6f;

        float minX = ar.x + ar.wall + halfX + MARGIN;
        float maxX = ar.x + ar.w - ar.wall - halfX - MARGIN;
        float minY = ar.y + ar.wall + halfY + MARGIN;
        float maxY = ar.y + ar.h - ar.wall - halfY - MARGIN;

        float x = minX + rng.nextFloat() * (maxX - minX);
        float y = minY + rng.nextFloat() * (maxY - minY);

        return new Item(t, x, y, w, h);
    }

    public void draw() {
        Raylib.Texture tex = (type == Type.SAW) ? Assets.saw : Assets.heart;
        Raylib.Rectangle src = new Raylib.Rectangle().x(0).y(0).width(tex.width()).height(tex.height());
        Raylib.Rectangle dst = new Raylib.Rectangle().x(x - drawW/2f).y(y - drawH/2f).width(drawW).height(drawH);
        Raylib.Vector2 origin = new Raylib.Vector2().x(drawW/2f).y(drawH/2f);
        DrawTexturePro(tex, src, dst, origin, 0f, com.raylib.Colors.WHITE);
    }
}

