package org.pinusgames.cuntromne.weapon.c4;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Config;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.weapon.Explode;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class C4Data {

    public static Entity entity;
    public static Set<Entity> fakes = new HashSet<>();

    public static ItemDisplay createC4(Location spawnPos) {
        Location copy = spawnPos.clone();
        copy.setYaw(0); copy.setPitch(0);
        ItemDisplay entity = (ItemDisplay) spawnPos.getWorld().spawnEntity( copy, EntityType.ITEM_DISPLAY );
        ItemStack item = new ItemStack( Material.STICK );
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData( 850 );
        item.setItemMeta( meta );
        entity.setBillboard( Display.Billboard.VERTICAL );
        entity.setBrightness( new Display.Brightness(15, 15) );
        entity.setItemStack( item );
        C4Data.entity = entity;
        Bukkit.broadcast(Component.text(""));
        Bukkit.broadcast(Component.text("Внимание! Тапок заминирован!").color(TextColor.color(255, 159, 0)));
        Bukkit.broadcast(Component.text(""));
        spawnPos.getWorld().playSound(spawnPos, "ctum:c4.plant", 100, 1);
        spawnPos.getWorld().playSound(spawnPos, "ctum:c4.plant2", 1, 1);
        Round.bombTimer = 900;
        return entity;
    }

    public static ItemDisplay createC4Fake(Location spawnPos) {
        Location copy = spawnPos.clone();
        copy.setYaw(0); copy.setPitch(0);
        ItemDisplay entity = (ItemDisplay) spawnPos.getWorld().spawnEntity( copy, EntityType.ITEM_DISPLAY );
        ItemStack item = new ItemStack( Material.STICK );
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData( 850 );
        item.setItemMeta( meta );
        entity.setBillboard( Display.Billboard.VERTICAL );
        entity.setBrightness( new Display.Brightness(15, 15) );
        entity.setItemStack( item );
        spawnPos.getWorld().playSound(spawnPos, "ctum:c4.plant2", 1, 1);
        C4Data.fakes.add(entity);
        return entity;
    }

    public static boolean locationOnPlant(Location location) {
        int targetY = (int) location.getY();
        int plantAY = (int) Config.plantA.getY();
        int plantBY = (int) Config.plantB.getY();
        if(targetY != plantAY && targetY != plantBY) return false;
        Location target = location.clone();
        Location plantA = Config.plantA.clone();
        Location plantB = Config.plantB.clone();
        target.setY(0); plantA.setY(0); plantB.setY(0);
        if(target.distance( plantA ) < 5 || target.distance( plantB ) < 5) return true;
        return false;
    }

    public static void explode() {
        for(Entity ent : C4Data.fakes) {
            ent.remove();
        }
        C4Data.fakes.clear();

        Location loc = C4Data.entity.getLocation();
        loc.getWorld().playSound(loc, "ctum:c4.explode", 100, 1);
        loc.getWorld().playSound(loc, "ctum:c4.taunt" + (new Random().nextInt(3) + 1), 100, (float)(1 + Math.random() * 0.2));
        Location spawnPos = loc.clone();
        spawnPos.add(0, 4, 0);
        new Explode(spawnPos, 24, 2);
        spawnPos.createExplosion(12);

        Round.bombTimer = -2;
        Round.endTrigger();

        C4Data.entity.remove();
    }

    public static void bombPeek() {
        boolean peep = false;
        if(Round.bombTimer >= 600 && Round.bombTimer % 20 == 0) peep = true;
        if(Round.bombTimer < 600 && Round.bombTimer >= 400 && Round.bombTimer % 15 == 0) peep = true;
        if(Round.bombTimer < 400 && Round.bombTimer >= 200 && Round.bombTimer % 10 == 0) peep = true;
        if(Round.bombTimer < 200 && Round.bombTimer > 100 && Round.bombTimer % 5 == 0) peep = true;
        if(Round.bombTimer < 100 && Round.bombTimer % 3 == 0) peep = true;
        if(peep) C4Data.entity.getWorld().playSound( C4Data.entity.getLocation(), "ctum:weapon.flash.hit", 3F, (float)( 1.4 + (0.6 * ((double) Round.bombTimer / 900)) ) );
    }

    public static ItemDisplay getBomb(Location location) {
        ItemDisplay result = null;
        if(C4Data.entity == null) return null;
        for(Entity ent : location.getNearbyEntities(0.75, 0.75, 0.75)) {
            if(C4Data.fakes.contains( ent )) result = (ItemDisplay) ent;
            if(C4Data.entity.getUniqueId().equals( ent.getUniqueId() )) return (ItemDisplay) ent;
        }
        return result;
    }

}
