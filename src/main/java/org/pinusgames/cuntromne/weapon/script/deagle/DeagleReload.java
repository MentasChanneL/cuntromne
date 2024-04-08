package org.pinusgames.cuntromne.weapon.script.deagle;

import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class DeagleReload extends Script {

    private boolean next;

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            if(this.data.ammoContainer < 1 || this.data.ammo >= this.data.maxAmmo) { close();return false; }
            this.next = false;
            this.animation = Animations.animations.get("deagle.reload");
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
            close();
            return false;
        }

        if(this.frame % 5 == 0) this.data.player.setCooldown( this.data.item.getType(), 11 );

        if(this.frame == 9 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.deagle.out2", 1, 1);
        }
        if(this.frame == 13 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.deagle.out1", 1, 1);
        }
        if(this.frame == 19 && this.next) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.deagle.in", 1, 1);
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
