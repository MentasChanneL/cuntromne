package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.script.Script;
import org.pinusgames.cuntromne.weapon.script.deagle.DeagleFire;
import org.pinusgames.cuntromne.weapon.script.deagle.DeagleIntro;
import org.pinusgames.cuntromne.weapon.script.deagle.DeagleReload;
import org.pinusgames.cuntromne.weapon.script.deagle.DeagleReview;
import org.pinusgames.cuntromne.weapon.script.glock.GlockFire;
import org.pinusgames.cuntromne.weapon.script.glock.GlockIntro;
import org.pinusgames.cuntromne.weapon.script.glock.GlockReload;

public class Glock implements WeaponActions{

    private Script script;

    @Override
    public void fire(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new GlockFire();
        this.script.run(data);
    }

    @Override
    public void reload(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new GlockReload();
        this.script.run(data);
    }

    @Override
    public void intro(WeaponData data) {
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new GlockIntro();
        this.script.run(data);
    }

    @Override
    public void outro(WeaponData data) {
        ItemMeta meta = data.item.getItemMeta();
        meta.setCustomModelData(450);
        data.item.setItemMeta(meta);
    }

    @Override
    public void review(WeaponData data) {
        //if( data.player.getCooldown( data.item.getType() ) > 0) return;
        //try{ script.close(); } catch (Exception ignore) {}
        //this.script = new DeagleReview();
        //this.script.run(data);
    }

    @Override
    public Script getScript() {
        return this.script;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(450);
        meta.displayName(Component.text("ГЛЕК").decoration(TextDecoration.ITALIC, false));
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new Glock(), result, 20, 120, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}