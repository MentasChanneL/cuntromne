package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.c4.C4Data;
import org.pinusgames.cuntromne.weapon.script.Script;

public class FakeC4 implements WeaponActions{

    @Override
    public void fire(WeaponData data) {}

    @Override
    public void reload(WeaponData data) {}

    @Override
    public void intro(WeaponData data) {
        Round.setAB(data.player, Component.text(""));
    }

    @Override
    public void outro(WeaponData data) {}

    @Override
    public void review(WeaponData data) {}

    @Override
    public void shift(WeaponData data) {
        if(data.player.isSneaking() && C4Data.locationOnPlant( data.player.getLocation() )) {
            C4Data.createC4Fake(data.player.getLocation());
            data.player.getInventory().setItemInMainHand(null);
        }
    }

    @Override
    public Script getScript() {
        return null;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(851);
        meta.displayName(Component.text("ФЕИК").decoration(TextDecoration.ITALIC, false));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new FakeC4(), result, 0, 0, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}