package org.pinusgames.cuntromne.weapon.script.awp;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class AwpReload extends Script {

    private boolean next;

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            if(this.data.ammoContainer < 1 || this.data.ammo >= this.data.maxAmmo) { close();return false; }
            this.next = false;
            this.animation = Animations.animations.get("awp.reload");
            this.frame = 0;
            this.data.player.setCooldown( this.data.item.getType(), 11 );
            return true;
        }
        if(this.frame >= this.animation.length) {
            if(this.data.ammoContainer > 0 && this.data.ammo < this.data.maxAmmo) {
                int need = this.data.maxAmmo - this.data.ammo;
                if(need > this.data.ammoContainer) {
                    need = this.data.ammoContainer;
                    this.data.ammoContainer = 0;
                }else {
                    this.data.ammoContainer -= need;
                }
                this.data.ammo += need;
            }
            this.data.player.setCooldown(this.data.item.getType(), 0);
            Round.setAB(this.data.player, Component.text("☰ " + this.data.ammo + " / " + this.data.ammoContainer));
            close();
            return false;
        }

        if(this.frame % 5 == 0) this.data.player.setCooldown( this.data.item.getType(), 11 );

        if(this.frame == 4 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.out", 1, 1);
        }
        if(this.frame == 11 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.in", 1, 1);
        }
        if(this.frame == 28 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.awp.intro", 1, 1);
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
