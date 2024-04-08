package org.pinusgames.cuntromne.weapon.script;

import org.bukkit.Bukkit;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.WeaponData;

public class Script {
    public int frame;
    public int[] animation;
    public WeaponData data;
    public int schedulerId;

    public void run(WeaponData data) {
        frame = -1;
        animation = null;
        this.data = data;
        this.data.item = data.player.getInventory().getItemInMainHand();
        execute();
    }

    private void execute() {
        this.schedulerId = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), this::step,1, 1).getTaskId();
    }

    public boolean step() {
        Object tag = NBTEditor.getItemTag( this.data.player.getInventory().getItemInMainHand() , "cunt-weaponid");
        if(tag == null) {
            close();
            return false;
        }
        int currentId = (int) tag;
        if(currentId != this.data.weaponId) {
            close();
            return false;
        }
        return true;
    }

    public void close() {
        Bukkit.getScheduler().cancelTask( this.schedulerId );
    }

}
