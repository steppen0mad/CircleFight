package cfs;

import java.util.ArrayList;
import java.util.List;

public class Snapshot {
    // Fighter 1 (Blue)
    public float b_x, b_y, b_vx, b_vy, b_sawAngle;
    public int b_hp;
    public boolean b_hasSaw;

    // Fighter 2 (Red)
    public float r_x, r_y, r_vx, r_vy, r_sawAngle;
    public int r_hp;
    public boolean r_hasSaw;

    // Items
    public List<Item> items;

    // DamageText
    public List<DamageText> dmgTexts;
    
    public float spawnTimer;

    public Snapshot(CircleFighter blue, CircleFighter red, List<Item> items, List<DamageText> dmgTexts, float spawnTimer) {
        // Copy Blue
        this.b_x = blue.x; this.b_y = blue.y;
        this.b_vx = blue.vx; this.b_vy = blue.vy;
        this.b_sawAngle = blue.sawAngle;
        this.b_hp = blue.hp;
        this.b_hasSaw = blue.hasSaw;

        // Copy Red
        this.r_x = red.x; this.r_y = red.y;
        this.r_vx = red.vx; this.r_vy = red.vy;
        this.r_sawAngle = red.sawAngle;
        this.r_hp = red.hp;
        this.r_hasSaw = red.hasSaw;

        // Copy Items (Shallow copy of list is enough as Item is effectively immutable in this sim)
        this.items = new ArrayList<>(items);

        // Copy DamageTexts (Deep copy needed as they update position/age)
        this.dmgTexts = new ArrayList<>();
        for (DamageText dt : dmgTexts) {
            DamageText copy = new DamageText(dt.x, dt.y, dt.text, dt.life, dt.vy);
            copy.age = dt.age;
            this.dmgTexts.add(copy);
        }
        
        this.spawnTimer = spawnTimer;
    }

    public void restore(CircleFighter blue, CircleFighter red, List<Item> currentItems, List<DamageText> currentDmgTexts) {
        // Restore Blue
        blue.x = b_x; blue.y = b_y;
        blue.vx = b_vx; blue.vy = b_vy;
        blue.sawAngle = b_sawAngle;
        blue.hp = b_hp;
        blue.hasSaw = b_hasSaw;

        // Restore Red
        red.x = r_x; red.y = r_y;
        red.vx = r_vx; red.vy = r_vy;
        red.sawAngle = r_sawAngle;
        red.hp = r_hp;
        red.hasSaw = r_hasSaw;

        // Restore Items
        currentItems.clear();
        currentItems.addAll(this.items);

        // Restore DamageTexts
        currentDmgTexts.clear();
        for (DamageText dt : this.dmgTexts) {
             DamageText newDt = new DamageText(dt.x, dt.y, dt.text, dt.life, dt.vy);
             newDt.age = dt.age;
             currentDmgTexts.add(newDt);
        }
    }
    
    public float getSpawnTimer() {
        return spawnTimer;
    }
}
