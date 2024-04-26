package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.script.ak.AkFire;
import org.pinusgames.cuntromne.weapon.script.ak.AkIntro;
import org.pinusgames.cuntromne.weapon.script.ak.AkReload;
import org.pinusgames.cuntromne.weapon.script.Script;
import org.pinusgames.cuntromne.weapon.script.ak.AkReview;

public class Ak47 implements WeaponActions{

    private Script script;

    @Override
    public void fire(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AkFire();
        this.script.run(data);
    }

    @Override
    public void reload(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AkReload();
        this.script.run(data);
    }

    @Override
    public void intro(WeaponData data) {
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AkIntro();
        this.script.run(data);
    }

    @Override
    public void outro(WeaponData data) {
        ItemMeta meta = data.item.getItemMeta();
        meta.setCustomModelData(50);
        data.item.setItemMeta(meta);
    }

    @Override
    public void review(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AkReview();
        this.script.run(data);
    }

    @Override
    public void shift(WeaponData data) {

    }

    @Override
    public Script getScript() {
        return this.script;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(50);
        meta.displayName(Component.text("КАЛШАШ-48").decoration(TextDecoration.ITALIC, false));
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new Ak47(), result, 30, 100, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}