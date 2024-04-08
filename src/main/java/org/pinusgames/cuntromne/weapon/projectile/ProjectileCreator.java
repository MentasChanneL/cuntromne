package org.pinusgames.cuntromne.weapon.projectile;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Evolution;
import org.pinusgames.cuntromne.Look;
import org.pinusgames.cuntromne.weapon.Explode;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;

public class ProjectileCreator {
    public static void createBullet(Location location, UUID owner, int damage) {
        Snowball bullet = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        bullet.setVelocity(location.getDirection().multiply(4));
        ItemStack item = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1);
        item.setItemMeta(meta);
        Cuntromne.getInstance().projectiles.put(bullet.getUniqueId(), new Projectile(System.currentTimeMillis(), owner, damage, bullet.getUniqueId(), ProjectileType.BULLET, 0));
        bullet.setItem(item);
    }

    public static Snowball createHE(Location location, UUID owner, int damage) {
        Snowball bullet = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        bullet.setVelocity(location.getDirection());
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(600);
        item.setItemMeta(meta);
        Cuntromne.getInstance().projectiles.put(bullet.getUniqueId(), new Projectile(System.currentTimeMillis(), owner, damage, bullet.getUniqueId(), ProjectileType.GRENADE_HE, 0));
        bullet.setItem(item);
        bullet.setShooter(Bukkit.getPlayer(owner));
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {

            Entity target = bullet;

            if(Evolution.hasGenerate(bullet.getUniqueId() )) {
                target = Bukkit.getEntity( Evolution.getEvo( bullet.getUniqueId() ) );
                Evolution.removeGenerate( bullet.getUniqueId() );
            }

            if(target == null) return;

            if(target.isValid()) {
                Location loc = target.getLocation();
                loc.getWorld().playSound(loc, "ctum:explode" + (new Random().nextInt(3) + 1), 4, 1);
                loc.createExplosion(target, 3);
                Location expPos = loc.clone();
                expPos.setYaw(0); expPos.setPitch(0);
                new Explode(expPos);
                target.remove();
            }
        }, 40);
        return bullet;
    }

    public static Snowball createFLASH(Location location, UUID owner, int damage) {
        Snowball bullet = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        bullet.setVelocity(location.getDirection());
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(602);
        item.setItemMeta(meta);
        Cuntromne.getInstance().projectiles.put(bullet.getUniqueId(), new Projectile(System.currentTimeMillis(), owner, damage, bullet.getUniqueId(), ProjectileType.GRENADE_FLASH, 0));
        bullet.setItem(item);
        bullet.setShooter(Bukkit.getPlayer(owner));
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {

            Entity target = bullet;

            if(Evolution.hasGenerate(bullet.getUniqueId() )) {
                target = Bukkit.getEntity( Evolution.getEvo( bullet.getUniqueId() ) );
                Evolution.removeGenerate( bullet.getUniqueId() );
            }

            if(target == null) return;

            if(target.isValid()) {
                for(Player player : Bukkit.getOnlinePlayers()) {

                    if(!Look.isPlayerLookingAtLocation(player, target.getLocation(), 0.5)) continue;
                    if(!Look.wayIsClear(player.getEyeLocation(), target.getLocation())) continue;
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false, false));
                    player.showTitle(Title.title(
                            Component.text(new Random().nextInt(8)).font(Key.key("ctum:flash")),
                            Component.text(")").font(Key.key("ctum:intro")),
                            Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    ));
                    player.playSound(player.getLocation(), "ctum:weapon.flash.pisk", 1, 0.8F);
                }
                target.getLocation().getWorld().spawnParticle(Particle.FLASH, target.getLocation(), 1);
                target.getLocation().getWorld().playSound(target.getLocation(), "ctum:weapon.flash", 3, 1);
                target.remove();
            }

        }, 40);
        return bullet;
    }

    public static Snowball cloneGrenade(Location location, Projectile data) {
        Snowball bullet = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        bullet.setVelocity(location.getDirection());
        Snowball origin = (Snowball) Bukkit.getEntity(data.entity);
        Evolution.addEvolution( data.entity, bullet.getUniqueId() );
        Cuntromne.getInstance().projectiles.put(bullet.getUniqueId(), new Projectile(data.spawnTime, data.owner, data.damage, bullet.getUniqueId(), data.type, data.data));
        bullet.setItem(origin.getItem());
        bullet.setShooter(Bukkit.getPlayer(data.owner));
        return bullet;
    }

}
