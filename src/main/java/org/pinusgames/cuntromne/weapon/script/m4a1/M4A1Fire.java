package org.pinusgames.cuntromne.weapon.script.m4a1;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class M4A1Fire extends Script {

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            if(this.data.ammo < 1) { close();return false; }
            this.data.ammo--;
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            this.data.player.setCooldown(this.data.item.getType(), 4);
            ProjectileCreator.createBullet(this.data.player.getEyeLocation(), this.data.player.getUniqueId(), 6);
            this.animation = Animations.animations.get("m4a1.fire");
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.m4a1.fire", 4, 1);
            this.frame = 0;
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }
        ItemMeta meta = this.data.item.getItemMeta();
        meta.setCustomModelData(this.animation[this.frame]);
        this.data.item.setItemMeta( meta );
        this.frame++;
        return true;
    }
}
