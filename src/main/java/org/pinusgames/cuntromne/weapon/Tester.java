package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Look;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Script;
import org.pinusgames.cuntromne.weapon.script.m4a1.M4A1Fire;
import org.pinusgames.cuntromne.weapon.script.m4a1.M4A1Intro;
import org.pinusgames.cuntromne.weapon.script.m4a1.M4A1Reload;

public class Tester implements WeaponActions{

    private Script script;

    @Override
    public void fire(WeaponData data) {
        Location location = data.player.getLocation().clone().set(2488, 100, -1842);
        if(Look.isPlayerLookingAtLocation(data.player, location, 0.5)) {
            data.player.sendMessage("yoy");
            if(Look.wayIsClear( data.player.getEyeLocation(), location )) {
                data.player.sendMessage("yas");
            };
        };
    }

    @Override
    public void reload(WeaponData data) {}

    @Override
    public void intro(WeaponData data) {}

    @Override
    public void outro(WeaponData data) {}

    @Override
    public void review(WeaponData data) {}

    @Override
    public Script getScript() {
        return this.script;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(0);
        meta.displayName(Component.text("TESTER").decoration(TextDecoration.ITALIC, false));
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new Tester(), result, 25, 90, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}