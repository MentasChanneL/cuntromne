package org.pinusgames.cuntromne.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.pinusgames.cuntromne.Cuntromne;

import java.util.UUID;

public class Timer {

    int id;

    public static void forInterval(Runnable run, int count, int interval) {
        int i = 0;
        while(i < count){
            i++;
            Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), run, (long) i * interval);
        }
    }

    public static void runLater(Runnable run, int time) {
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), run, time);
    }

    public Timer(int interval, UUID player, Runnable run) {
        this.id = -1;
        this.id = Bukkit.getScheduler().runTaskTimer(Cuntromne.getInstance(), () -> {
            Player pl = Bukkit.getPlayer(player);
            if(pl == null) {
                Bukkit.getScheduler().cancelTask(this.id);
                return;
            }
            run.run();
        }, 1, interval).getTaskId();
    }

}
