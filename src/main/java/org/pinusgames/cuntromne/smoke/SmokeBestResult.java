package org.pinusgames.cuntromne.smoke;

import org.bukkit.Location;

public class SmokeBestResult {
    public final SmokeBestType type;
    public final Location move;


    public SmokeBestResult(SmokeBestType type, Location move) {
        this.type = type;
        this.move = move;
    }
}
