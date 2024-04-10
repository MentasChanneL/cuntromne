package org.pinusgames.cuntromne.utils;

import org.bukkit.util.Vector;

public class SmokeVector {

    public int x;
    public int y;
    public int z;

    public SmokeVector(Vector vector) {
        this.x = (int) vector.getX();
        this.y = (int) vector.getY();
        this.z = (int) vector.getZ();
    }

    public boolean isEqual(SmokeVector vector) {
        return vector.x == this.x && vector.y == this.y && vector.z == this.z;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

}
