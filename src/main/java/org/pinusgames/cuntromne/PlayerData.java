package org.pinusgames.cuntromne;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {

    private static final HashMap<UUID, PlayerData> data = new HashMap<>();

    public final Player player;
    public Team team;

    public PlayerData(Player player) {
        this.player = player;
    }

    public static void createData(Player player) {
        if(data.containsKey( player.getUniqueId() ) ) {
            player.getServer().getLogger().warning("PlayerData уже существует у игрока " + player.getName() + "! Класс " + data.get(player.getUniqueId()).getClass());
            return;
        }
        data.put(player.getUniqueId(), new PlayerData(player));
    }

    public static PlayerData get(Player player) {
        if(!data.containsKey( player.getUniqueId() )) createData(player);
        return data.get( player.getUniqueId() );
    }

    public static void remove(Player player) {
        data.remove( player.getUniqueId() );
    }

}
