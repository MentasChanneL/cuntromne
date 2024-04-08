package org.pinusgames.cuntromne.weapon.script.glock;

import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class GlockFire extends Script {

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            if(this.data.ammo < 1) { close();return false; }
            this.data.ammo--;
            this.data.player.setCooldown(this.data.item.getType(), 4);
            ProjectileCreator.createBullet(this.data.player.getEyeLocation(), this.data.player.getUniqueId(), 3);
            this.animation = Animations.animations.get("glock.fire");
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.glock.fire", 4, (float) 0.9);
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
