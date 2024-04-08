package org.pinusgames.cuntromne.weapon.script.ak;

import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class AkReview extends Script {

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.animation = Animations.animations.get("ak.review.1");
            if(Math.random() < 0.01) {
                this.animation = Animations.animations.get("ak.review.2");
                this.data.player.getWorld().playSound( this.data.player.getLocation(), "ctum:weapon.ak.review02", 1, 1 );
            }else {
                this.data.player.getWorld().playSound( this.data.player.getLocation(), "ctum:weapon.ak.review01", 1, 1 );
            }
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 7 );
            return true;
        }

        if(this.frame >= this.animation.length) {
            close();
            return false;
        }

        ItemMeta meta = this.data.item.getItemMeta();
        meta.setCustomModelData( this.animation[this.frame] );
        this.data.item.setItemMeta( meta );
        this.frame++;
        return true;
    }
}
