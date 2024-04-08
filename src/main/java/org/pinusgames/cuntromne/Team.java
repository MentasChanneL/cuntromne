package org.pinusgames.cuntromne;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class Team {
    public String name = "";
    public final String id;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void teamMessage(String id, Component message) {
        List<Player> listeners = Round.getTeam(id);
        for(Player player : listeners) {
            player.sendMessage(Component.text("(Командный) ").append(message));
        }
    }

}
