package org.pinusgames.cuntromne.weapon.script.deagle;

import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

import java.util.Random;

public class DeagleReview extends Script {

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.animation = Animations.animations.get("deagle.intro");
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 10 );
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.deagle.review" + (new Random().nextInt(3) + 1), 1, 1);
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }

        ItemMeta meta = this.data.item.getItemMeta();
        meta.setCustomModelData(this.animation[this.frame]);
        this.data.item.setItemMeta(meta);
        this.frame++;

        return true;
    }
}
