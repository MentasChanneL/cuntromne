package org.pinusgames.cuntromne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Evolution {
    private static final HashMap<UUID, UUID> evolution = new HashMap<>();

    public static boolean hasGenerate(UUID target) {
        return evolution.containsKey(target);
    }

    public static void addEvolution(UUID parent, UUID child) {
        evolution.put(parent, child);
    }

    public static UUID getEvo(UUID entity) {
        UUID result = entity;
        while (Evolution.evolution.containsKey(result)) {
            result = Evolution.evolution.get(result);
        }
        return result;
    }

    public static void removeGenerate(UUID target) {
        List<UUID> victims = new ArrayList<>();
        UUID result = target;
        while (Evolution.evolution.containsKey(result)) {
            victims.add(result);
            result = Evolution.evolution.get(result);
        }
        for(UUID victim : victims) {
            evolution.remove(victim);
        }
    }

}
