package org.pinusgames.cuntromne;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.actions.Action;
import org.pinusgames.cuntromne.actions.LostAction;
import org.pinusgames.cuntromne.actions.WinAction;
import org.pinusgames.cuntromne.donate.Knife;
import org.pinusgames.cuntromne.weapon.Armor;

import java.time.Duration;
import java.util.*;

public class Round {

    public static int roundID = 0;
    public static BossBar bossbar = Bukkit.createBossBar("00:00", BarColor.WHITE, BarStyle.SOLID);

    public static int timeLeft;
    public static int prepareLeft;
    public static HashMap<Player, Component> actionBars = new HashMap<>();
    public static int schedulerTimer = -1;
    public static int schedulerActionbar = -1;
    public static int bombTimer = -3;
    public static boolean endGameEvent = false;
    public static Team winTeam;

    public static net.kyori.adventure.bossbar.BossBar mansBossbar = net.kyori.adventure.bossbar.BossBar.bossBar(Component.text(), 1, net.kyori.adventure.bossbar.BossBar.Color.WHITE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS);
    public static net.kyori.adventure.bossbar.BossBar scoreBossbar = net.kyori.adventure.bossbar.BossBar.bossBar(Component.text(), 1, net.kyori.adventure.bossbar.BossBar.Color.WHITE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS);

    public static void newGame() {
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        bombTimer = -1;
        Team.teamList.get("ct").wins = 0;
        Team.teamList.get("t").wins = 0;
        roundID = 0;
        lobbyDist();
        setScoreBossbar();
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        for(Player player : Team.teamList.get("ct").members) {
            player.getInventory().clear();
            player.setItemOnCursor(null);
            Shop.money.put(player.getUniqueId(), 8);
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
            preparePlayer(player);
        }
        for(Player player : Team.teamList.get("t").members) {
            player.getInventory().clear();
            player.setItemOnCursor(null);
            Shop.money.put(player.getUniqueId(), 8);
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
            preparePlayer(player);
        }
        bossbarTxtCreator();
        actionBarReset();
        schedulerActionbar = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::actionBarTick, 1, 1 ).getTaskId();
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    public static void newRound() {
        if(Bukkit.getOnlinePlayers().isEmpty()) return;
        for(Entity t : Config.ctspawn.getWorld().getEntities()) {
            if(t instanceof Snowball) t.remove();
        }
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        bombTimer = -1;
        roundID++;
        lobbyDist();
        setScoreBossbar();
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        for(Player player : Team.teamList.get("ct").members) {
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
            preparePlayer(player);
        }
        for(Player player : Team.teamList.get("t").members) {
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
            preparePlayer(player);
        }
        actionBarReset();
        bossbarTxtCreator();
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    private static void setScoreBossbar() {
        scoreBossbar.name(
                Component.text(Team.teamList.get("ct").wins).color(TextColor.color(0, 100, 255))
                        .font(Key.key("ctum:round"))
                        .append(Component.text(" ").font(Key.key("minecraft:default")))
                        .append(Component.text("/").color(TextColor.color(255, 255, 255)).font(Key.key("ctum:round")))
                        .append(Component.text(" ").font(Key.key("minecraft:default")))
                        .append(Component.text(Team.teamList.get("t").wins).color(TextColor.color(255, 100, 0)).font(Key.key("ctum:round")))
        );
    }

    private static void lobbyDist() {
        Team t = Team.teamList.get("t");
        Team ct = Team.teamList.get("ct");
        for(Player player : Team.teamList.get("spectator").members) {
            if(ct.getMembersCount() < t.getMembersCount() || ct.getMembersCount() == t.getMembersCount()) {
                ct.addMember(player);
                continue;
            }
            if(ct.getMembersCount() > t.getMembersCount()) {
                t.addMember(player);
            }
        }
        Team.teamList.get("spectator").members.clear();
    }

    private static void preparePlayer(Player player) {
        Events.blockMove.add(player.getUniqueId());
        Team team = PlayerData.get(player).team;
        ItemStack chest = player.getInventory().getItem(EquipmentSlot.CHEST);
        ItemStack knife = Knife.getKnife(player.getUniqueId());
        if(!chest.hasItemMeta() || !chest.getItemMeta().hasCustomModelData() ) {
            if(knife != null) {
                player.getInventory().setItem(2, knife);
            }
            if(team.id.equals("ct")) {
                if(knife == null) player.getInventory().setItem(2, org.pinusgames.cuntromne.weapon.Knife.give(player, 1001));
                player.getInventory().setItem(EquipmentSlot.CHEST, Armor.giveStock( Color.fromRGB(50, 50, 250) ));
            }
            if(team.id.equals("t")) {
                if(knife == null) player.getInventory().setItem(2, org.pinusgames.cuntromne.weapon.Knife.give(player, 1000));
                player.getInventory().setItem(EquipmentSlot.CHEST, Armor.giveStock( Color.fromRGB(250, 125, 0) ));
            }
        }
        player.setGameMode(GameMode.ADVENTURE);
        player.playSound(player.getLocation(), "ctum:round", 1, 1);
        player.setHealth(20);
        player.setAbsorptionAmount(20);
        player.clearActivePotionEffects();
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 250, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, 4, false, false));
    }

    private static void tick() {
        bossbar();
        if(Round.prepareLeft > 0) {

            for(Player player : selectInGame()) {
                player.sendActionBar(
                        Component.text("Нажмите ")
                                .append(Component.keybind("key.swapOffhand").color(TextColor.color(255, 223, 18)))
                                .append(Component.text(" для закупа.").color(TextColor.color(255, 255, 255)))
                );
            }

            Round.prepareLeft--;
            if(Round.prepareLeft == 0) {
                for(Player player : selectInGame()) {
                    Events.blockMove.remove(player.getUniqueId());
                    player.sendActionBar(Component.text(""));
                }
                Team team = Team.teamList.get("t");
                if(!team.members.isEmpty()) {
                    Player random = team.getMember( new Random().nextInt(team.members.size()) );
                    Team.teamMessage("t", random.displayName().color(TextColor.color(255, 125, 0)).append(Component.text(" : Пошли!" )));
                    random.getLocation().getWorld().playSound(random.getLocation(), "ctum:player.t.battlecry" + (new Random().nextInt(4) + 1), 1, 1);
                }
                team = Team.teamList.get("ct");
                if(!team.members.isEmpty()) {
                    Player random = team.getMember( new Random().nextInt(team.members.size()) );
                    Team.teamMessage("ct", random.displayName().color(TextColor.color(50, 50, 255)).append(Component.text(" : За работу!" )));
                    random.getLocation().getWorld().playSound(random.getLocation(), "ctum:player.ct.battlecry" + (new Random().nextInt(4) + 1), 1, 1);
                }
            }
            return;
        }
        if(Round.timeLeft > 0) {
            Round.timeLeft--;
            if(timeLeft == 0 && Round.bombTimer != -3) {
                ctwin();
            }
            return;
        }
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
    }

    public static void endTrigger() {
        bossbarTxtCreator();
        if(Round.bombTimer == -3) return;
        if(Round.bombTimer == -2) { twin(); return;}
        Set<Player> ctlist = Team.teamList.get("ct").getAliveMembers() ;
        if(Round.bombTimer > -1) {
            if(!ctlist.isEmpty()) {
                twin();
                return;
            }
        }
        Set<Player> tlist = Team.teamList.get("t").getAliveMembers();
        if(ctlist.isEmpty() && tlist.isEmpty()) {
            noone();
            return;
        }
        if(!ctlist.isEmpty() && tlist.isEmpty()) {
            ctwin();
            return;
        }
        if(ctlist.isEmpty()) {
            twin();
        }
    }

    private static void endGame() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        if(ct.wins > 15 || t.wins > 15) {
            Round.bombTimer = -3;
            Round.endGameEvent = true;
            Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
            if(ct.wins > t.wins) {
                Round.winTeam = ct;
                for(Player player :  t.members) {
                    Action action = new LostAction();
                    action.run(player);
                }
                for(Player player : ct.members) {
                    Action action = new WinAction();
                    action.run(player);
                }
            }
            if(ct.wins < t.wins) {
                Round.winTeam = t;
                for(Player player : ct.members) {
                    Action action = new LostAction();
                    action.run(player);
                }
                for(Player player : t.members) {
                    Action action = new WinAction();
                    action.run(player);
                }
            }
            Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
                for (Player player : selectInGame()) {
                    setAB(player, Component.text(""));
                }
                List<Player> sufle = selectInGame();
                Collections.shuffle(sufle);
                Team.teamList.get("spectator").addMembers(sufle);
                Round.endGameEvent = false;
                newGame();
            }, 200);
            return;
        }
        newRound();
    }

    private static void twin() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        Round.bombTimer = -3;
        t.wins++;
        for(Player player : t.members) {
            Shop.addCash(player, 15);
        }
        for(Player player : ct.members) {
            Shop.addCash(player, 10);
        }
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), Round::endGame, 100);
        for(Player player : selectInGame()) {
            player.showTitle(Title.title(
                    Component.text("Экскремисты").color(TextColor.color(255, 150, 0)),
                    Component.text("Выйграли этот раунд!"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))
            ));
        }
    }

    private static void ctwin() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        Round.bombTimer = -3;
        ct.wins++;
        for(Player player : ct.members) {
            Shop.addCash(player, 15);
        }
        for(Player player : t.members) {
            Shop.addCash(player, 10);
        }
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), Round::endGame, 100);
        for(Player player : selectInGame()) {
            player.showTitle(Title.title(
                    Component.text("Контрононисты").color(TextColor.color(0, 50, 255)),
                    Component.text("Выйграли этот раунд!"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))
            ));
        }
    }

    private static void noone() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        Round.bombTimer = -3;
        ct.wins++;
        t.wins++;
        for(Player player : ct.members) {
            Shop.addCash(player, 12);
        }
        for(Player player : t.members) {
            Shop.addCash(player, 12);
        }
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), Round::newRound, 100);
        for(Player player : selectInGame()) {
            player.showTitle(Title.title(
                    Component.text(""),
                    Component.text("Ничья!"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))
            ));
        }
    }

    private static void bossbar() {
        if(Round.prepareLeft > 0) {
            String display = String.valueOf(prepareLeft / 20);
            if (display.length() < 2) display = "0" + display;
            bossbar.setTitle("00:" + display);
            return;
        }
        if(Round.timeLeft > 0) {
            int seconds = timeLeft / 20;
            int minutes = seconds / 60;
            seconds = seconds - (minutes * 60);
            String sd = String.valueOf(seconds);
            String dm = String.valueOf(minutes);
            if(seconds < 10) sd = "0" + sd;
            if(minutes < 10) dm = "0" + dm;
            bossbar.setTitle(dm + ":" + sd);
        }
    }

    private static void bossbarTxtCreator() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        StringBuilder c = new StringBuilder();
        for(int i = 0; i < ct.getMembersCount(); i++) {
            c.append("1");
        }
        Component title = Component.text(c.toString()).font(Key.key("ctum:icons"));
        c = new StringBuilder();
        for(int i = 0; i < t.getMembersCount(); i++) {
            c.append("2");
        }
        title = title.append(Component.text("                       ").font(Key.key("minecraft:default"))).append(Component.text(c.toString()).font(Key.key("ctum:icons")));
        mansBossbar.name(title);
    }

    private static void actionBarTick() {
        for(Player player : actionBars.keySet()) {
            player.sendActionBar( actionBars.get(player) );
        }
    }
    private static void actionBarReset() {
        for(Player player : new HashSet<>(actionBars.keySet())) {
            actionBars.put(player, Component.text(""));
        }
    }

    public static void setAB(Player player, Component component) {
        actionBars.put(player, component);
    }

    public static List<Player> selectInGame() {
        List<Player> players = new ArrayList<>();
        for(Team team : Team.teamList.values()) {
            if(team.id.equals("spectator")) continue;
            players.addAll(team.members);
        }
        return players;
    }

}
