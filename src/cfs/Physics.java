package cfs;

import java.util.Random;

public final class Physics {
    private Physics(){}

    public static void setRandomVelocity(CircleFighter f, float speedMin, float speedMax, Random rng) {
        float angle = (float)(rng.nextFloat() * Math.PI * 2.0);
        float speed = speedMin + rng.nextFloat() * (speedMax - speedMin);
        f.vx = (float)Math.cos(angle) * speed;
        f.vy = (float)Math.sin(angle) * speed;
    }

    /** Integrate + resolve; return approach impact speed if a circle–circle collision occurred (else 0). */
    public static float step(CircleFighter a, CircleFighter b, BoxArena arena, float dt) {
        // Integrate
        a.x += a.vx * dt; a.y += a.vy * dt;
        b.x += b.vx * dt; b.y += b.vy * dt;

        // Walls
        bounceOnWalls(a, arena);
        bounceOnWalls(b, arena);

        // Circles
        return collideCirclesElastic(a, b);
    }

    private static void bounceOnWalls(CircleFighter f, BoxArena ar) {
        float left   = ar.x + ar.wall + f.radius;
        float right  = ar.x + ar.w - ar.wall - f.radius;
        float top    = ar.y + ar.wall + f.radius;
        float bottom = ar.y + ar.h - ar.wall - f.radius;

        if (f.x < left)  { f.x = left;  f.vx = -f.vx; }
        if (f.x > right) { f.x = right; f.vx = -f.vx; }
        if (f.y < top)   { f.y = top;   f.vy = -f.vy; }
        if (f.y > bottom){ f.y = bottom;f.vy = -f.vy; }
    }

    /**
     * Equal-mass, perfectly elastic.
     * Returns impact speed (>0) only when they were approaching along the normal; 0 otherwise.
     */
    private static float collideCirclesElastic(CircleFighter A, CircleFighter B) {
        float dx = B.x - A.x, dy = B.y - A.y;
        float rsum = A.radius + B.radius;
        float dist2 = dx*dx + dy*dy;
        if (dist2 <= 0f || dist2 > rsum*rsum) return 0f;

        float dist = (float)Math.sqrt(dist2);
        float nx = dx / dist, ny = dy / dist;

        // Relative velocity along normal
        float rvx = B.vx - A.vx, rvy = B.vy - A.vy;
        float vn = rvx*nx + rvy*ny;  // <0 when approaching

        // Always separate overlap (split equally) to avoid sinking
        separate(A, B, rsum - dist, nx, ny);

        if (vn >= 0f) return 0f; // already separating; not a hit for damage

        // Exchange normal components (equal mass)
        float tx = -ny, ty = nx;
        float va_n = A.vx*nx + A.vy*ny, vb_n = B.vx*nx + B.vy*ny;
        float va_t = A.vx*tx + A.vy*ty, vb_t = B.vx*tx + B.vy*ty;
        float va_n_new = vb_n, vb_n_new = va_n;

        A.vx = va_n_new*nx + va_t*tx; A.vy = va_n_new*ny + va_t*ty;
        B.vx = vb_n_new*nx + vb_t*tx; B.vy = vb_n_new*ny + vb_t*ty;

        return -vn; // positive impact speed along the normal
    }

    private static void separate(CircleFighter A, CircleFighter B, float overlap, float nx, float ny) {
        float push = overlap * 0.5f;
        A.x -= nx * push; A.y -= ny * push;
        B.x += nx * push; B.y += ny * push;
    }
}

