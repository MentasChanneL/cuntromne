package org.pinusgames.cuntromne.shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.*;


public class SlotParameter {
    public int slot;
    public String item;
    public int cost;
    public int giveSlot;
    public int cmd;

    public SlotParameter(int menuSlot, String item, int cost, int hotbarSlot, int customModelData) {
        this.slot = menuSlot;
        this.item = item;
        this.cost = cost;
        this.giveSlot = hotbarSlot;
        this.cmd = customModelData;
    }

    public void giveItem(Player player) {

        int oldSlot = player.getInventory().getHeldItemSlot();
        int newSlot = 8;
        player.getInventory().setHeldItemSlot(8);
        ItemStack give = null;
        if(this.item.equals("ak")) give = Ak47.give(player);
        if(this.item.equals("deagle")) give = Deagle.give(player);
        if(this.item.equals("m4a1")) give = M4A1.give(player);
        if(this.item.equals("awp")) give = Awp.give(player);
        if(this.item.equals("glock")) give = Glock.give(player);
        if(this.item.equals("tester")) give = Tester.give(player);
        if(this.item.equals("he")) give = GrenadeHE.give(player);
        if(this.item.equals("flash")) give = GrenadeFlash.give(player);
        if(this.item.equals("smoke")) give = GrenadeSmoke.give(player);
        if(this.item.equals("usp")) give = USP.give(player);
        if(this.item.equals("armor")) {
            give = Armor.give(player);
            player.getWorld().playSound(player.getLocation(), "ctum:armor", 1, 1);
        }
        player.getInventory().setItem(this.giveSlot, give);
        if(this.giveSlot < 9) {
            newSlot = this.giveSlot;
            player.getInventory().setHeldItemSlot(this.giveSlot);
        }

        ItemStack newW = player.getInventory().getItem( newSlot );
        ItemStack oldW = player.getInventory().getItem( oldSlot );
        Object tag = NBTEditor.getItemTag(newW, "cunt-weaponid");
        if(tag != null && Cuntromne.getInstance().weapons.containsKey( (int) tag )) {
            int id = (int) tag;
            Bukkit.getScheduler().runTask(Cuntromne.getInstance(), () -> {
                Cuntromne.getInstance().weapons.get(id).intro();
            });
        }
        tag = NBTEditor.getItemTag(oldW, "cunt-weaponid");
        if(tag != null && Cuntromne.getInstance().weapons.containsKey( (int) tag )) {
            Cuntromne.getInstance().weapons.get( (int) tag ).outro();
        }

    }

}
