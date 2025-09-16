package cfs;

import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;

import java.util.*;
import com.raylib.Raylib;

public class CFSApp {
    private static final float PHYSICS_HZ   = 240f;
    private static final float FIXED_DT     = 1.0f / PHYSICS_HZ;
    private static final float MAX_FRAME    = 1f / 15f;
    private static final int   HEART_HEAL   = 15;

    // ---- pickup sprite sizes on the floor ----
    public static final float SAW_PICKUP_SIZE   = 44f;
    public static final float HEART_PICKUP_SIZE = 40f;

    // rotation speed of equipped saw
    private static final float SAW_SPIN_DPS = 360f;

    // Damage tuning
    private static final float DMG_PER_SPEED       = 0.03f;
    private static final int   MIN_DMG             = 6;
    private static final int   MAX_DMG             = 50;
    private static final float MIN_IMPACT_FOR_HIT  = 30f;

    // === Equipped saw: scale & optional offsets ===
    // Scale relative to BALL DIAMETER. 1.00 = exactly ball diameter; >1 pushes teeth outside.
    private static float SAW_RING_SCALE = 1.40f;
    // Offsets (pixels) in case a particular PNG still needs a tiny nudge
    private static float SAW_RING_OFFSET_X = 0f;
    private static float SAW_RING_OFFSET_Y = 0f;

    public static void main(String[] args) {
        final int W = 1000, H = 700;

        InitWindow(W, H, "Circle Fighter Simulation");
        Assets.load();

        final int HUD_H = 140, MARGIN = 80;
        BoxArena arena = new BoxArena(
            MARGIN, HUD_H + 20,
            W - 2*MARGIN,
            H - (HUD_H + 20) - MARGIN/2,
            10
        );

        CircleFighter blue = new CircleFighter("Blue", BLUE, 28);
        CircleFighter red  = new CircleFighter("Red",  RED,  28);
        blue.x = arena.x + arena.w * 0.75f; blue.y = arena.y + arena.h * 0.75f;
        red.x  = arena.x + arena.w * 0.45f; red.y  = arena.y + arena.h * 0.45f;

        Random rng = new Random();
        Physics.setRandomVelocity(blue, 400f, 600f, rng);
        Physics.setRandomVelocity(red,  400f, 600f, rng);

        List<Item> items = new ArrayList<>();
        List<DamageText> dmgTexts = new ArrayList<>();
        float spawnTimer = nextSpawn(rng);

        float accumulator = 0f;
        boolean running = true;

        while (running && !WindowShouldClose()) {
            float frameDt = GetFrameTime();
            accumulator += (frameDt > MAX_FRAME ? MAX_FRAME : frameDt);

            // Live tuning (optional): ] bigger, [ smaller; I/J/K/L to nudge center
            if (IsKeyPressed(KEY_RIGHT_BRACKET)) SAW_RING_SCALE += 0.05f;
            if (IsKeyPressed(KEY_LEFT_BRACKET))  SAW_RING_SCALE = Math.max(1.05f, SAW_RING_SCALE - 0.05f);
            if (IsKeyPressed(KEY_J)) SAW_RING_OFFSET_X -= 1f;
            if (IsKeyPressed(KEY_L)) SAW_RING_OFFSET_X += 1f;
            if (IsKeyPressed(KEY_I)) SAW_RING_OFFSET_Y -= 1f;
            if (IsKeyPressed(KEY_K)) SAW_RING_OFFSET_Y += 1f;

            // Spin equipped saws
            if (blue.hasSaw) blue.sawAngle = (blue.sawAngle + SAW_SPIN_DPS * frameDt) % 360f;
            if (red.hasSaw)  red.sawAngle  = (red.sawAngle  + SAW_SPIN_DPS * frameDt) % 360f;

            // Spawning
            spawnTimer -= frameDt;
            if (spawnTimer <= 0f) {
                items.add(Item.spawnRandom(arena, rng));
                spawnTimer = nextSpawn(rng);
            }

            // Fixed-step physics
            float maxImpactThisFrame = 0f;
            while (accumulator >= FIXED_DT) {
                float impact = Physics.step(blue, red, arena, FIXED_DT);
                if (impact > maxImpactThisFrame) maxImpactThisFrame = impact;
                accumulator -= FIXED_DT;
            }

            // Apply speed-based damage if exactly one has a saw
            if (maxImpactThisFrame >= MIN_IMPACT_FOR_HIT && (blue.hasSaw ^ red.hasSaw)) {
                CircleFighter attacker = blue.hasSaw ? blue : red;
                CircleFighter defender = blue.hasSaw ? red  : blue;

                int dmg = computeDamage(maxImpactThisFrame);
                defender.hp = Math.max(0, defender.hp - dmg);
                attacker.hasSaw = false;

                dmgTexts.add(new DamageText(
                    defender.x,
                    defender.y - defender.radius - 12f,
                    "-" + dmg,
                    0.9f,
                    -80f
                ));
            }

            // Pickups
            for (int i = items.size() - 1; i >= 0; i--) {
                Item it = items.get(i);
                if (touches(blue, it)) { onPickup(blue, red, it); items.remove(i); continue; }
                if (touches(red,  it)) { onPickup(red,  blue, it); items.remove(i); }
            }

            // Update floating damage
            for (int i = dmgTexts.size()-1; i >= 0; i--) {
                if (!dmgTexts.get(i).update(frameDt)) dmgTexts.remove(i);
            }

            if (blue.hp <= 0 || red.hp <= 0) running = false;

            // Draw
            BeginDrawing();
            ClearBackground(DARKGRAY);

            int barW = W - 2*MARGIN, barH = 26;
            HealthBar.draw(MARGIN, 60,  barW, barH, blue.name, blue.hp, blue.maxHp, BLUE);
            HealthBar.draw(MARGIN, 100, barW, barH, red.name,  red.hp,  red.maxHp,  RED);

            arena.draw();
            for (Item it : items) it.draw();

            // Ball first, then the centered saw-ring ON TOP of it
            DrawCircle((int)blue.x, (int)blue.y, blue.radius, blue.color);
            DrawCircleLines((int)blue.x, (int)blue.y, blue.radius, RAYWHITE);
            if (blue.hasSaw) drawSawRingWrapped(blue);

            DrawCircle((int)red.x, (int)red.y, red.radius, red.color);
            DrawCircleLines((int)red.x, (int)red.y, red.radius, RAYWHITE);
            if (red.hasSaw) drawSawRingWrapped(red);

            for (DamageText dt : dmgTexts) dt.draw();

            if (!running) {
                String msg = (blue.hp <= 0 && red.hp <= 0) ? "Draw"
                             : (blue.hp <= 0) ? "Red Wins!" : "Blue Wins!";
                int tw = MeasureText(msg, 36);
                DrawText(msg, (W - tw)/2, arena.y + arena.h/2 - 18, 36, RAYWHITE);
            }

            EndDrawing();
        }

        Assets.unload();
        CloseWindow();
    }

    // ---- helpers ----
    private static float nextSpawn(Random rng) { return 4f + rng.nextFloat() * 3f; }

    private static boolean touches(CircleFighter f, Item it) {
        float dx = f.x - it.x, dy = f.y - it.y;
        float r  = f.radius + it.pickupRadius;
        return (dx*dx + dy*dy) <= (r*r);
    }

    private static void onPickup(CircleFighter picker, CircleFighter other, Item it) {
        switch (it.type) {
            case HEART:
                picker.hp = Math.min(picker.maxHp, picker.hp + HEART_HEAL);
                break;
            case SAW:
                other.hasSaw = false;
                picker.hasSaw = true;
                picker.sawAngle = 0f;
                break;
        }
    }

    private static int computeDamage(float impactSpeed) {
        int dmg = Math.round(impactSpeed * DMG_PER_SPEED);
        if (dmg < MIN_DMG) dmg = MIN_DMG;
        if (dmg > MAX_DMG) dmg = MAX_DMG;
        return dmg;
    }

    /** Draw the equipped saw as a centered, rotating ring sized from the ball diameter. */
    private static void drawSawRingWrapped(CircleFighter f) {
        Raylib.Texture tex = Assets.saw;

        // Size from ball diameter (PNG padding already trimmed in Assets.load()).
        float diameter = f.radius * 2f;
        float size = diameter * SAW_RING_SCALE;

        float cx = f.x + SAW_RING_OFFSET_X;
        float cy = f.y + SAW_RING_OFFSET_Y;

        Raylib.Rectangle src = new Raylib.Rectangle().x(0).y(0).width(tex.width()).height(tex.height());
        Raylib.Rectangle dst = new Raylib.Rectangle().x(cx - size/2f).y(cy - size/2f).width(size).height(size);
        Raylib.Vector2 origin = new Raylib.Vector2().x(size/2f).y(size/2f);

        DrawTexturePro(tex, src, dst, origin, f.sawAngle, com.raylib.Colors.WHITE);
    }
}

