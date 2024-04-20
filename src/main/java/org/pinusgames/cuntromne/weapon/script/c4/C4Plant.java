package org.pinusgames.cuntromne.weapon.script.c4;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.c4.C4Data;
import org.pinusgames.cuntromne.weapon.script.Animations;
import org.pinusgames.cuntromne.weapon.script.Script;

public class C4Plant extends Script {

    private boolean skip;

    @Override
    public void close() {
        super.close();
        this.data.player.removePotionEffect(PotionEffectType.SLOW);
        this.data.player.removePotionEffect(PotionEffectType.LEVITATION);
    }

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.data.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, PotionEffect.INFINITE_DURATION, 10, false, false, false));
            this.data.player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, PotionEffect.INFINITE_DURATION, 128, false, false, false));
            this.skip = false;
            Round.setAB(this.data.player, Component.text(""));
            this.data.player.setCooldown(this.data.item.getType(), 11);
            this.animation = Animations.animations.get("c4.plant");
            this.frame = 0;
            return true;
        }
        if(!this.data.player.isSneaking()) {
            ItemMeta meta = this.data.item.getItemMeta();
            meta.setCustomModelData(800);
            this.data.item.setItemMeta(meta);
            close();
            return false;
        }
        if(this.frame >= this.animation.length) {
            close();
            if(!C4Data.locationOnPlant( this.data.player.getLocation() ) ) {
                ItemMeta meta = this.data.item.getItemMeta();
                meta.setCustomModelData(800);
                this.data.item.setItemMeta(meta);
                return false;
            }
            C4Data.createC4( this.data.player.getLocation() );
            this.data.player.getInventory().setItemInMainHand(null);
            this.data.player.getInventory().setHeldItemSlot(0);
            return false;
        }
        if(this.frame == 8 && !this.skip) this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:weapon.ak.out", 2, (float) 0.6);
        if((this.frame == 20 || this.frame == 22 || this.frame == 29) && !this.skip) this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:c4.click", 2, (float) 1.25);
        this.skip = !this.skip;
        if(this.frame % 5 == 0) this.data.player.setCooldown( this.data.item.getType(), 11 );
        if(!this.skip) {
            ItemMeta meta = this.data.item.getItemMeta();
            meta.setCustomModelData(this.animation[this.frame]);
            this.data.item.setItemMeta(meta);
            this.frame++;
        }
        return true;
    }

}
