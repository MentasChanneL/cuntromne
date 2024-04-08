package org.pinusgames.cuntromne.weapon.script.awp;

import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class AwpIntro extends Script {

    private boolean next;

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.animation = Animations.animations.get("awp.intro");
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 40 );
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }

        if(this.frame == 16 && this.next) this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.intro", 1, 1);

        if(this.next) {
            ItemMeta meta = this.data.item.getItemMeta();
            meta.setCustomModelData(this.animation[this.frame]);
            this.data.item.setItemMeta(meta);
            this.frame++;
            this.next = false;
        }else {
            this.next = true;
        }

        return true;
    }
}