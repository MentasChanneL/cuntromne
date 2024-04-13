package org.pinusgames.cuntromne.weapon.script.awp;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class AwpFire extends Script {

    boolean next;

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.next = true;
            if(this.data.ammo < 1) { close();return false; }
            this.data.ammo--;
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            Vector vector = this.data.player.getEyeLocation().getDirection();
            this.data.player.setVelocity( this.data.player.getVelocity().add( vector.multiply(-1) ) );
            this.data.player.setCooldown(this.data.item.getType(), 35);
            ProjectileCreator.createBullet(this.data.player.getEyeLocation(), this.data.player.getUniqueId(), 30);
            this.animation = Animations.animations.get("awp.fire");
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.fire", 4, 1);
            this.frame = 0;
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }
        if(this.frame > 2) {
            if(this.next) {

                if(this.frame == 15) this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.intro", 1, (float)(1 + (Math.random() * 0.1)) );

                ItemMeta meta = this.data.item.getItemMeta();
                meta.setCustomModelData(this.animation[this.frame]);
                this.data.item.setItemMeta(meta);
                this.frame++;
                this.next = false;
            }else {
                this.next = true;
            }
        }else {
            ItemMeta meta = this.data.item.getItemMeta();
            meta.setCustomModelData(this.animation[this.frame]);
            this.data.item.setItemMeta(meta);
            this.frame++;
        }
        return true;
    }
}
