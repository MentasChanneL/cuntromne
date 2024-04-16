package org.pinusgames.cuntromne;

import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.block.Block;
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
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.pinusgames.cuntromne.actions.ActionChain;
import org.pinusgames.cuntromne.actions.IntroAction;
import org.pinusgames.cuntromne.actions.JoinAction;
import org.pinusgames.cuntromne.actions.LobbyAction;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.smoke.SmokeBlock;
import org.pinusgames.cuntromne.utils.Timer;
import org.pinusgames.cuntromne.weapon.Explode;
import org.pinusgames.cuntromne.weapon.projectile.Projectile;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileType;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;

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
            return;
        }
        Player player = e.getPlayer();
        if(SmokeBlock.SmokeIsNearby(player.getEyeLocation())) {
            if (!SmokeBlock.inSmoke.contains(player)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, PotionEffect.INFINITE_DURATION, 0, false, false));
                SmokeBlock.inSmoke.add(player);
            }
        }else if(SmokeBlock.inSmoke.contains(player)) {
            player.removePotionEffect(PotionEffectType.DARKNESS);
            SmokeBlock.inSmoke.remove(player);
        }
    }

    @EventHandler
    public void stopSpectate(PlayerStopSpectatingEntityEvent e) {
        if(Round.endGameEvent ) {
            e.setCancelled(true);
            if(e.getPlayer().getCooldown(Material.STRING) > 0) return;
            e.getPlayer().setCooldown(Material.STRING, 15);

            if(Round.winTeam.id.equals( PlayerData.get( e.getPlayer() ).team.id )) {
                e.getPlayer().playSound(e.getPlayer().getLocation(), "ctum:taunt", 0.4F, 1);
                Bukkit.broadcast(Component.text(e.getPlayer().getName()).color(TextColor.color(150, 200, 14))
                        .append(Component.text(" насмехается над лузерами!").color(TextColor.color(44, 209, 79)))
                );
                for(Player player : Round.selectInGame()) {
                    if(!Round.winTeam.id.equals( PlayerData.get( player ).team.id )) {
                        player.playSound(player.getLocation(), "ctum:taunt", 1, 1);
                        player.showTitle( Title.title(
                                Component.text("4").font(Key.key("ctum:icons"))
                                        .append( Component.text("   ").font(Key.key("minecraft:default")) ),
                                Component.text(""),
                                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))
                        ));
                    }
                }
            }
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        try {
            PlayerData.createData(e.getPlayer());
        }catch (Exception ex) {
            ex.printStackTrace();
            e.getPlayer().kick(Component.text("Произошел пранк. Обратитесь к 2м3ы @2m3v в дс"));
            return;
        }
        Team.teamList.get("spectator").addMember( e.getPlayer() );
        Round.actionBars.put(e.getPlayer(), Component.text(""));
        e.getPlayer().setCustomChatCompletions(Arrays.asList("ಧ", "ನ", "బ", "భ"));
        e.getPlayer().teleport( Config.login );
        e.getPlayer().getInventory().clear();
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            ActionChain.runActionChain(e.getPlayer(), new JoinAction(), new IntroAction(), new LobbyAction());
        }, 20);

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
        Object tag = NBTEditor.getItemTag(oldW, "cunt-weaponid");
        if(tag != null && this.instance.weapons.containsKey( (int) tag )) {
            this.instance.weapons.get( (int) tag ).outro();
        }
        Round.actionBars.put(e.getPlayer(), Component.text(""));
        tag = NBTEditor.getItemTag(newW, "cunt-weaponid");
        if(tag != null && this.instance.weapons.containsKey( (int) tag )) {
            int id = (int) tag;
            Bukkit.getScheduler().runTask(this.instance, () -> this.instance.weapons.get(id).intro());
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
            if(live != null) { live.damage(1, Bukkit.getPlayer(data.owner)); }
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
            if(live != null) { live.damage(1, Bukkit.getPlayer(data.owner)); }
            e.getEntity().remove();
            return;
        }

        if(data.type == ProjectileType.GRENADE_SMOKE) {
            data.data++;
            Location loc = e.getEntity().getLocation();
            if(data.data > 5) {
                ItemStack visual = new ItemStack(Material.STICK);
                ItemMeta meta = visual.getItemMeta();
                meta.setCustomModelData(606);
                visual.setItemMeta(meta);
                SmokeBlock.createSmoke(126, loc, visual);
                loc.getWorld().playSound(loc, "ctum:weapon.smoke", 1, 1);
                e.getEntity().remove();
                return;
            }
            loc.getWorld().playSound(loc, "ctum:weapon.flash.hit", 1, 0.9F);
            if(e.getHitBlock() != null) {
                Vector face = e.getHitBlockFace().getDirection();
                Vector move = e.getEntity().getVelocity();
                double dot = move.dot(face);
                Vector reflected = move.subtract(face.multiply(2 * dot));
                Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
                newP.setVelocity(reflected.multiply(0.65));
                e.getEntity().remove();
                return;
            }
            Snowball newP = ProjectileCreator.cloneGrenade(loc, data);
            newP.setVelocity(new Vector(0, 0, 0));
            LivingEntity live = (LivingEntity) e.getHitEntity();
            if(live != null) { live.damage(1, Bukkit.getPlayer(data.owner)); }
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
        if(target.getGameMode() == GameMode.SPECTATOR) {e.setCancelled(true); return;}
        Team team = PlayerData.get(target).team;
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

    @EventHandler(priority = EventPriority.LOW)
    public void playerKill(EntityDamageByEntityEvent e) {
        Bukkit.broadcastMessage(e.getEntity().getType() + " " + e.getDamager().getType());
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;
        Player victim = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();
        Team id1 = PlayerData.get(victim).team;
        Team id2 = PlayerData.get(damager).team;
        if(id1.id.equals( id2.id )) {
            Shop.addCash(damager, (int)e.getDamage() * -1);
            damager.showTitle(Title.title(
                    Component.text("ТЫ ЧЕ"),
                    Component.text("Зочем ранил союзника?"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(0))
            ));
            e.setDamage(e.getDamage() / 2);
            Timer.runLater(() -> {
                if (victim.getGameMode() == GameMode.SPECTATOR) {
                    Bukkit.broadcast(
                            damager.displayName().color( id2.color )
                                    .append(Component.text(" и "))
                                    .append(victim.displayName().color( id1.color ))
                                    .append(Component.text(" жёско засосались со слюнями").color( TextColor.color(255, 255, 255) ))
                    );
                }
            }, 1);
            return;
        }
        Timer.runLater(() -> {
            if(victim.getGameMode() == GameMode.SPECTATOR) {
                Shop.addCash(damager, 4);
                Bukkit.broadcast(
                        damager.displayName().color( id2.color )
                                .append(Component.text(" трахаль ").color( TextColor.color(255, 255, 255) ))
                                .append(victim.displayName().color( id1.color ))
                );
            }
        }, 1);
    }

    @EventHandler
    public void death(EntityDeathEvent e) {
        if(!(e.getEntity() instanceof Player)) return;
        Player target = (Player) e.getEntity();
        if(target.getGameMode() == GameMode.SPECTATOR) {e.setCancelled(true); return;}
        if(!PlayerData.get(target).team.id.equals("t") && !PlayerData.get(target).team.id.equals("ct")) return;
        e.setCancelled(true);
        target.setGameMode(GameMode.SPECTATOR);
        target.showTitle(Title.title(
                Component.text(""),
                Component.text("вы умер :("),
                Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));
        target.getInventory().clear();
        target.setItemOnCursor(null);
        Round.endTrigger();
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

    @EventHandler
    public void shift(PlayerToggleSneakEvent e) {
        Player p = e.getPlayer();
        ItemStack weapon = p.getInventory().getItemInMainHand();
        Object tag = NBTEditor.getItemTag(weapon, "cunt-weaponid");
        if (tag == null) return;
        int id = (int) tag;
        if (!this.instance.weapons.containsKey(id)) return;
        e.setCancelled(true);
        e.getPlayer().setSneaking( e.isSneaking() );
        this.instance.weapons.get(id).shift();
    }

    @EventHandler
    public void leave(PlayerQuitEvent e) {
        try {
            PlayerData.get(e.getPlayer()).team.removeMember(e.getPlayer());
        }catch (Exception ignore) {}
        PlayerData.remove( e.getPlayer() );
        Round.actionBars.remove( e.getPlayer() );
        Round.endTrigger();
    }

}
