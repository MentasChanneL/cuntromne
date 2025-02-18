package org.pinusgames.cuntromne.weapon.script.defuse;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.c4.C4Data;
import org.pinusgames.cuntromne.weapon.script.Script;

public class DefuseDefuse extends Script {

    private ItemDisplay bomb;

    @Override
    public void close() {
        super.close();
        this.data.player.removePotionEffect(PotionEffectType.SLOWNESS);
        this.data.player.removePotionEffect(PotionEffectType.LEVITATION);
        this.data.player.setVelocity(new Vector(0, 0, 0));
    }

    @Override
    public boolean step() {
        if(!super.step()) return false;
        if( this.frame == -1 ) {
            this.bomb = C4Data.getBomb( data.player.getLocation() );
            if(bomb == null) { close(); return false; }
            this.data.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 10, false, false, false));
            this.data.player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, PotionEffect.INFINITE_DURATION, 128, false, false, false));
            Round.setAB(this.data.player, Component.text(""));
            this.data.player.setCooldown(this.data.item.getType(), 11);
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:c4.defuse", 2, 1);
            this.frame = 0;
            return true;
        }
        if(!this.data.player.isSneaking() || this.data.player.getGameMode() == GameMode.SPECTATOR) {
            Round.setAB(this.data.player, Component.text(""));
            close();
            return false;
        }
        if(Math.random() < 0.01 && Round.bombTimer < 400) this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:fart", 1f, (float)(0.8 + Math.random() * 0.4));
        Round.setAB(this.data.player, Component.text("Разминировние... " + (int)( ((double)this.frame / 85) * 100 ) + "%")
                .color(TextColor.color(255 - (this.frame * 3), this.frame * 3, 0)));
        if(this.frame == 20 && C4Data.fakes.contains( this.bomb )) {
            this.bomb.remove();
            Round.setAB(this.data.player, Component.text(""));
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:c4.fake", 1, 1);
            close();
            return false;
        }
        if(this.frame >= 85) {
            this.data.player.getWorld().playSound(this.data.player.getLocation(), "ctum:c4.defused", 100, 1);
            Round.bombTimer = -1;
            Round.timeLeft = 1;
            close();
            Round.setAB(this.data.player, Component.text(""));
            return false;
        }
        if(this.frame % 5 == 0) this.data.player.setCooldown( this.data.item.getType(), 11 );
        this.frame++;

        return true;
    }

}
