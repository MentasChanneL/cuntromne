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
import org.pinusgames.cuntromne.weapon.script.awp.AwpFire;
import org.pinusgames.cuntromne.weapon.script.awp.AwpIntro;
import org.pinusgames.cuntromne.weapon.script.awp.AwpReload;

public class Awp implements WeaponActions{

    private Script script;

    @Override
    public void fire(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        ItemMeta meta = data.item.getItemMeta();
        meta.setCustomModelData(300);
        data.item.setItemMeta(meta);
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AwpFire();
        this.script.run(data);
    }

    @Override
    public void reload(WeaponData data) {
        if( data.player.getCooldown( data.item.getType() ) > 0) return;
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AwpReload();
        this.script.run(data);
    }

    @Override
    public void intro(WeaponData data) {
        try{ script.close(); } catch (Exception ignore) {}
        this.script = new AwpIntro();
        this.script.run(data);
    }

    @Override
    public void outro(WeaponData data) {
        ItemMeta meta = data.item.getItemMeta();
        meta.setCustomModelData(310);
        data.item.setItemMeta(meta);
    }

    @Override
    public void review(WeaponData data) {
        //if( data.player.getCooldown( data.item.getType() ) > 0) return;
        //try{ script.close(); } catch (Exception ignore) {}
        //this.script = new AkReview();
        //this.script.run(data);
    }

    @Override
    public Script getScript() {
        return this.script;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(310);
        meta.displayName(Component.text("СЛОНОЁБ").decoration(TextDecoration.ITALIC, false));
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new Awp(), result, 5, 25, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}