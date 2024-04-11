package org.pinusgames.cuntromne.utils;

import org.bukkit.Bukkit;
import org.pinusgames.cuntromne.Cuntromne;

public class Timer {

    public Timer(Runnable run, int count, int interval) {
        int i = 0;
        while(i < count){
            i++;
            Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), run, (long) i * interval);
        }
    }

}
