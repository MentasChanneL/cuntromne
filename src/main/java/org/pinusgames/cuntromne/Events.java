package org.pinusgames.cuntromne;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.pinusgames.cuntromne.actions.ActionChain;
import org.pinusgames.cuntromne.actions.IntroAction;
import org.pinusgames.cuntromne.actions.JoinAction;
import org.pinusgames.cuntromne.actions.LobbyAction;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.Explode;
import org.pinusgames.cuntromne.weapon.projectile.Projectile;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileType;

import java.util.*;

public class Events implements Listener {

    public final Cuntromne instance;
    public static final Set<UUID> blockMove = new HashSet<>();

    public Events(Cuntromne plugin) {
        this.instance = plugin;
    }

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        if(blockMove.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        e.getPlayer().teleport( Config.login );
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            ActionChain.runActionChain(e.getPlayer(), new JoinAction(), new IntroAction(), new LobbyAction());
        }, 5);

    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack weapon = p.getInventory().getItemInMainHand();
            Object tag = NBTEditor.getItemTag(weapon, "cunt-weaponid");
            if (tag == null) return;
            int id = (int) tag;
            if (!this.instance.weapons.containsKey(id)) return;
            e.setCancelled(true);
            this.instance.weapons.get(id).fire();
            return;
        }
        if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Player p = e.getPlayer();
            ItemStack weapon = p.getInventory().getItemInMainHand();
            Object tag = NBTEditor.getItemTag(weapon, "cunt-weaponid");
            if (tag == null) return;
            int id = (int) tag;
            if (!this.instance.weapons.containsKey(id)) return;
            e.setCancelled(true);
            this.instance.weapons.get(id).review();
        }
    }

    @EventHandler
    public void switchHand(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if(Round.prepareLeft > 0) {
            e.setCancelled(true);
            Shop.showMenu(p);
            return;
        }
        ItemStack weapon = p.getInventory().getItemInMainHand();
        Object tag = NBTEditor.getItemTag(weapon, "cunt-weaponid"); if(tag == null) return; int id = (int) tag;
        if(!this.instance.weapons.containsKey( id )) return;
        e.setCancelled(true);
        this.instance.weapons.get( id ).reload();
    }

    @EventHandler
    public void drop(PlayerDropItemEvent e) { e.setCancelled(true); }

    @EventHandler
    public void changeSlot(PlayerItemHeldEvent e) {
        Player p = e.getPlayer();
        ItemStack newW = p.getInventory().getItem( e.getNewSlot() );
        ItemStack oldW = p.getInventory().getItem( e.getPreviousSlot() );
        Object tag = NBTEditor.getItemTag(newW, "cunt-weaponid");
        if(tag != null && this.instance.weapons.containsKey( (int) tag )) {
            int id = (int) tag;
            Bukkit.getScheduler().runTask(this.instance, () -> {
                this.instance.weapons.get(id).intro();
            });
        }
        tag = NBTEditor.getItemTag(oldW, "cunt-weaponid");
        if(tag != null && this.instance.weapons.containsKey( (int) tag )) {
            this.instance.weapons.get( (int) tag ).outro();
        }
    }
    @EventHandler
    public void projectileHit(ProjectileHitEvent e) {
        Entity ball = e.getEntity();
        if(!Cuntromne.getInstance().projectiles.containsKey(ball.getUniqueId())) return;
        Projectile data = Cuntromne.getInstance().projectiles.get( ball.getUniqueId() );

        if(data.type == ProjectileType.GRENADE_HE) {
            data.data++;
            Location loc = e.getEntity().getLocation();
            if(System.currentTimeMillis() - data.spawnTime > 2000 || data.data > 3) {
                loc.getWorld().playSound(loc, "ctum:explode" + (new Random().nextInt(3) + 1), 4, 1);
                loc.createExplosion(e.getEntity(), 3);
                Location expPos = loc.clone();
                expPos.setYaw(0); expPos.setPitch(0);
                new Explode(expPos);
                e.getEntity().remove();
                return;
            }
            loc.getWorld().playSound(loc, "ctum:weapon.he.hit", 1, 1);
            if(e.getHitBlock() != null) {
                Vector face = e.getHitBlockFace().getDirection();
                Vector move = e.getEntity().getVelocity();
                double dot = move.dot(face);
                Vector reflected = move.subtract(face.multiply(2 * dot));
                Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
                newP.setVelocity(reflected.multiply(0.8));
                e.getEntity().remove();
                return;
            }
            Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
            newP.setVelocity(new Vector(0, 0, 0));
            LivingEntity live = (LivingEntity) e.getHitEntity();
            live.damage(1, Bukkit.getPlayer(data.owner));
            e.getEntity().remove();
            return;
        }

        if(data.type == ProjectileType.GRENADE_FLASH) {
            Location loc = e.getEntity().getLocation();
            loc.getWorld().playSound(loc, "ctum:weapon.flash.hit", 1, 1);
            if(e.getHitBlock() != null) {
                Vector face = e.getHitBlockFace().getDirection();
                Vector move = e.getEntity().getVelocity();
                double dot = move.dot(face);
                Vector reflected = move.subtract(face.multiply(2 * dot));
                Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
                newP.setVelocity(reflected.multiply(0.8));
                e.getEntity().remove();
                return;
            }
            Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
            newP.setVelocity(new Vector(0, 0, 0));
            LivingEntity live = (LivingEntity) e.getHitEntity();
            live.damage(1, Bukkit.getPlayer(data.owner));
            e.getEntity().remove();
            return;
        }

        if(data.type != ProjectileType.BULLET) return;
        if(e.getHitBlock() != null) {
            Vector face = e.getHitBlockFace().getDirection();
            Location location = e.getHitBlock().getLocation().add(0.5, 0.5, 0.5).add(face.multiply(0.5));
            ball.remove();
            Material material = e.getHitBlock().getBlockData().getMaterial();
            location.getWorld().playSound(location, "ctum:bullet.wood", 1, (float)(0.9 - Math.random() * 0.4));
            if(MaterialTags.metal.contains(material)) {
                location.getWorld().playSound(location, "ctum:bullet.metal", 1, (float)(0.8 - Math.random() * 0.4));
                location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 10, 0.1, 0.1, 0.1);
                return;
            }
            if(MaterialTags.dirt.contains(material)) {
                location.getWorld().playSound(location, "ctum:bullet.default", 1, (float)(0.9 - Math.random() * 0.4));
                location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 5, e.getHitBlock().getBlockData());
                return;
            }
            location.getWorld().spawnParticle(Particle.BLOCK_CRACK, location, 5, e.getHitBlock().getBlockData());
            return;
        }
        if(e.getHitEntity() != null) {
            UUID victim = e.getHitEntity().getUniqueId();
            if(data.owner.equals(victim)) {
                e.setCancelled(true);
                return;
            }
            ball.remove();
            Entity victimEnt = e.getHitEntity();
            if(!(victimEnt instanceof LivingEntity)) return;
            long delta = System.currentTimeMillis() - data.spawnTime;
            float damage = data.damage * ( 1 - ((float) delta / 3000) );
            if(damage < 0) damage = 0;
            LivingEntity live = (LivingEntity) victimEnt;
            live.getWorld().playSound(live.getEyeLocation(), "ctum:bullet.wood", 1, (float)(0.9 - Math.random() * 0.4));
            live.getWorld().spawnParticle(Particle.BLOCK_CRACK, live.getLocation().add(0, 1, 0), 25, 0.25, 0.5, 0.25, Material.RED_CONCRETE.createBlockData());
            live.setNoDamageTicks(0);
            live.damage(damage, Bukkit.getEntity( data.owner ));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player target = (Player) e.getEntity();
        if(!Round.teamList.containsKey( target.getUniqueId() )) return;
        Team team = Round.teamList.get( target.getUniqueId() );
        if(team.id.equals("t")) {
            Location location = e.getEntity().getLocation();
            if(target.getHealth() - e.getDamage() <= 0) {
                location.getWorld().playSound(location, "ctum:player.t.death" + (new Random().nextInt(7) + 1), 1, 1);
                return;
            }
            location.getWorld().playSound(location, "ctum:player.t.hurt" + (new Random().nextInt(7) + 1), 1, 1);
            return;
        }
        if(team.id.equals("ct")) {
            Location location = e.getEntity().getLocation();
            if(target.getHealth() - e.getDamage() <= 0) {
                location.getWorld().playSound(location, "ctum:player.ct.death" + (new Random().nextInt(6) + 1), 1, 1);
                return;
            }
            location.getWorld().playSound(location, "ctum:player.ct.hurt" + (new Random().nextInt(4) + 1), 1, 1);
        }
    }

    @EventHandler
    public void clickInventory(InventoryClickEvent event) {
        if(event.getWhoClicked().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
        if(event.getClickedInventory() == Shop.menu) {
            Shop.click(event);
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent event) {
        if(event.getInventory() == Shop.menu) {
            Shop.close(event);
        }
    }

    @EventHandler
    public void explodeEntity(EntityExplodeEvent e) {
        List<Block> list = new ArrayList<>(e.blockList());
        e.blockList().clear();
        for(Block i : list) {
            i.getWorld().spawnParticle(Particle.BLOCK_CRACK, i.getLocation().add(0.5, 1, 0.5),20,0.25, 0.25, 0.25, i.getBlockData());
        }
    }
    @EventHandler
    public void explodeBlock(BlockExplodeEvent e) {
        List<Block> list = new ArrayList<>(e.blockList());
        e.blockList().clear();
        for(Block i : list) {
            i.getWorld().spawnParticle(Particle.BLOCK_CRACK, i.getLocation().add(0.5, 1, 0.5), 20, i.getBlockData());
        }
    }
    @EventHandler
    public void saturation(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

}
