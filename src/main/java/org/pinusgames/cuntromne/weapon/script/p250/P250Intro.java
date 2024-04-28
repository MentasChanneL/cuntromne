package org.pinusgames.cuntromne.weapon.script.p250;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

import java.util.Random;

public class P250Intro extends Script {


    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.animation = Animations.animations.get("p250.intro");
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 13 );
            return true;
        }
        if(this.frame >= this.animation.length) {
            close();
            return false;
        }

        if(this.frame == 6) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.deagle.review" + (new Random().nextInt(3) + 1), 1F, 1.5F);
        }

        ItemMeta meta = this.data.item.getItemMeta();
        meta.setCustomModelData(this.animation[this.frame]);
        this.data.item.setItemMeta(meta);
        this.frame++;

        return true;
    }
}
