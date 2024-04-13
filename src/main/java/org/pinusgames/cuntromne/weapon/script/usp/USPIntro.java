package org.pinusgames.cuntromne.weapon.script.usp;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class USPIntro extends Script {

    private boolean next;

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.animation = Animations.animations.get("usp.intro");
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 34 );
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }

        if(this.frame == 10 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.glock.intro", 1, 1);
        }

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
