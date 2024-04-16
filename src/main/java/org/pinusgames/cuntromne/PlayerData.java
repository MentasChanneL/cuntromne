package org.pinusgames.cuntromne;

import org.bukkit.entity.Player;
import org.pinusgames.cuntromne.exteptions.ObjectAlreadyExists;

import java.util.HashMap;

public class PlayerData {

    private static final HashMap<Player, PlayerData> data = new HashMap<>();

    public final Player player;
    public Team team;

    public PlayerData(Player player) {
        this.player = player;
    }

    public static void createData(Player player) throws ObjectAlreadyExists {
        if(data.containsKey(player)) throw new ObjectAlreadyExists("PlayerData уже существует у игрока " + player.getName() + "! Класс " + data.get(player).getClass());
        data.put(player, new PlayerData(player));
    }

    public static PlayerData get(Player player) {
        if(!data.containsKey(player)) return null;
        return data.get(player);
    }

    public static void remove(Player player) {
        data.remove(player);
    }

}
