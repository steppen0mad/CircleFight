package cfs;

import com.raylib.Raylib;
import static com.raylib.Raylib.*;

/** Loads game textures and trims transparent padding so sprites center correctly. */
public final class Assets {
    private Assets() {}

    public static Raylib.Texture saw;
    public static Raylib.Texture heart;

    public static void load() {
        // ---- Load & trim saw ----
        Raylib.Image sawImg = LoadImage("assets/saw.png");
        // Trim transparent borders so visual center == image center
        // (Threshold 0.05 trims pixels whose alpha <= 0.05)
        ImageAlphaCrop(sawImg, 0.05f);
        saw = LoadTextureFromImage(sawImg);
        UnloadImage(sawImg);

        // ---- Load & trim heart ----
        Raylib.Image heartImg = LoadImage("assets/heart.png");
        ImageAlphaCrop(heartImg, 0.05f);
        heart = LoadTextureFromImage(heartImg);
        UnloadImage(heartImg);
    }

    public static void unload() {
        if (saw != null && saw.id() != 0)     UnloadTexture(saw);
        if (heart != null && heart.id() != 0) UnloadTexture(heart);
    }
}

