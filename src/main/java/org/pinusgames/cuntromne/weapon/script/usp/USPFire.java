package org.pinusgames.cuntromne.weapon.script.usp;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class USPFire extends Script {
    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            if(this.data.ammo < 1) { close();return false; }
            this.data.ammo--;
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            this.data.player.setCooldown(this.data.item.getType(), 8);
            ProjectileCreator.createBullet(this.data.player.getEyeLocation(), this.data.player.getUniqueId(), 5);
            this.animation = Animations.animations.get("usp.fire");
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.usp.fire", 2, (float) 1);
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
