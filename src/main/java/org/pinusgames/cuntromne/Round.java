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
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.donate.Knife;
import org.pinusgames.cuntromne.weapon.Armor;

import java.time.Duration;
import java.util.*;

public class Round {

    public static int roundID = 0;
    public static BossBar bossbar = Bukkit.createBossBar("00:00", BarColor.WHITE, BarStyle.SOLID);

    public static int timeLeft;
    public static int prepareLeft;
    public static HashMap<UUID, Team> teamList = new HashMap<>();
    public static Set<Player> lobby = new HashSet<>();
    public static HashMap<Player, Component> actionBars = new HashMap<>();
    public static int schedulerTimer = -1;
    public static int schedulerActionbar = -1;
    public static int bombTimer;
    public static int CTWins;
    public static int TWins;
    public static int CTcount;
    public static int Tcount;

    public static net.kyori.adventure.bossbar.BossBar mansBossbar = net.kyori.adventure.bossbar.BossBar.bossBar(Component.text(), 1, net.kyori.adventure.bossbar.BossBar.Color.WHITE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS);
    public static net.kyori.adventure.bossbar.BossBar scoreBossbar = net.kyori.adventure.bossbar.BossBar.bossBar(Component.text(), 1, net.kyori.adventure.bossbar.BossBar.Color.WHITE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS);

    public static void newGame() {
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        bombTimer = -1;
        CTWins = 0;
        TWins = 0;
        roundID = 0;
        Tcount = 0;
        CTcount = 0;
        lobbyDist();
        setScoreBossbar();
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        for(UUID target : new HashSet<>( teamList.keySet() )) {
            Player player = Bukkit.getPlayer(target);
            if( player == null || !player.isValid() ) {teamList.remove(target); continue;}
            player.getInventory().clear();
            player.setItemOnCursor(null);
            Shop.money.put(player.getUniqueId(), 8);
            Team team = teamList.get(target);
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            if(team.id.equals("ct")) {
                CTcount++;
                player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
            if(team.id.equals("t")) {
                Tcount++;
                player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
        }
        bossbarTxtCreator();
        actionBarReset();
        schedulerActionbar = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::actionBarTick, 1, 1 ).getTaskId();
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    public static void newRound() {
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        bombTimer = -1;
        Tcount = 0;
        CTcount = 0;
        roundID++;
        lobbyDist();
        setScoreBossbar();
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        for(UUID target : new HashSet<>( teamList.keySet() )) {
            Player player = Bukkit.getPlayer(target);
            if( player == null || !player.isValid() ) {teamList.remove(target); continue;}
            Team team = teamList.get(target);
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            if(team.id.equals("ct")) {
                CTcount++;
                player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
            if(team.id.equals("t")) {
                Tcount++;
                player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
                preparePlayer(player);
            }
        }
        actionBarReset();
        bossbarTxtCreator();
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    private static void setScoreBossbar() {
        scoreBossbar.name(
                Component.text(CTWins).color(TextColor.color(0, 100, 255))
                        .font(Key.key("ctum:round"))
                        .append(Component.text(" ").font(Key.key("minecraft:default")))
                        .append(Component.text("/").color(TextColor.color(255, 255, 255)).font(Key.key("ctum:round")))
                        .append(Component.text(" ").font(Key.key("minecraft:default")))
                        .append(Component.text(TWins).color(TextColor.color(255, 100, 0)).font(Key.key("ctum:round")))
        );
    }

    private static void lobbyDist() {
        for(Player player : lobby) {
            int countT = Round.getTeam("t").size();
            int countCT = Round.getTeam("ct").size();
            if(countCT < countT || countCT == countT) {
                teamList.put(player.getUniqueId(), Cuntromne.getInstance().ct);
            }
            if(countCT > countT) {
                teamList.put(player.getUniqueId(), Cuntromne.getInstance().t);
            }
        }
        lobby.clear();
    }

    private static void preparePlayer(Player player) {
        Team team = Round.teamList.get(player.getUniqueId());
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
                    player.sendActionBar(Component.text(""));
                }
                List<Player> team = Round.getTeam("t");
                if(!team.isEmpty()) {
                    Player random = team.get( new Random().nextInt(team.size()) );
                    Team.teamMessage("t", random.displayName().color(TextColor.color(255, 125, 0)).append(Component.text(" : Пошли!" )));
                    random.getLocation().getWorld().playSound(random.getLocation(), "ctum:player.t.battlecry" + (new Random().nextInt(4) + 1), 1, 1);
                }
                team = Round.getTeam("ct");
                if(!team.isEmpty()) {
                    Player random = team.get( new Random().nextInt(team.size()) );
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
        CTcount = sortAlive(Round.getTeam("ct")).size();
        Tcount = sortAlive(Round.getTeam("t")).size();
        bossbarTxtCreator();
        if(Round.bombTimer == -3) return;
        if(Round.bombTimer == -2) { twin(); return;}
        List<Player> ctlist = sortAlive( getTeam("ct") ) ;
        if(Round.bombTimer > -1) {
            if(!ctlist.isEmpty()) {
                twin();
                return;
            }
        }
        List<Player> tlist = sortAlive( getTeam("t") );
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

    private static void twin() {
        Round.bombTimer = -3;
        Round.TWins++;
        for(Player player : getTeam("t")) {
            Shop.addCash(player, 15);
        }
        for(Player player : getTeam("ct")) {
            Shop.addCash(player, 10);
        }
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), Round::newRound, 100);
        for(Player player : selectInGame()) {
            player.showTitle(Title.title(
                    Component.text("Экскремисты").color(TextColor.color(255, 150, 0)),
                    Component.text("Выйграли этот раунд!"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))
            ));
        }
    }

    private static void ctwin() {
        Round.bombTimer = -3;
        Round.CTWins++;
        for(Player player : getTeam("ct")) {
            Shop.addCash(player, 15);
        }
        for(Player player : getTeam("t")) {
            Shop.addCash(player, 10);
        }
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), Round::newRound, 100);
        for(Player player : selectInGame()) {
            player.showTitle(Title.title(
                    Component.text("Контрононисты").color(TextColor.color(0, 50, 255)),
                    Component.text("Выйграли этот раунд!"),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))
            ));
        }
    }

    private static void noone() {
        Round.bombTimer = -3;
        Round.CTWins++;
        Round.TWins++;
        for(Player player : getTeam("ct")) {
            Shop.addCash(player, 12);
        }
        for(Player player : getTeam("t")) {
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
        StringBuilder c = new StringBuilder();
        for(int i = 0; i < CTcount; i++) {
            c.append("1");
        }
        Component title = Component.text(c.toString()).font(Key.key("ctum:icons"));
        c = new StringBuilder();
        for(int i = 0; i < Tcount; i++) {
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

    public static List<Player> getTeam(String id) {
        List<Player> players = new ArrayList<>();
        for(UUID uuid : new HashSet<>(Round.teamList.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) {
                Round.teamList.remove(uuid);
                continue;
            }
            Team pteam = Round.teamList.get(uuid);
            if(pteam.id.equals(id)) {
                players.add(player);
            }
        }
        return players;
    }
    public static List<Player> selectInGame() {
        List<Player> players = new ArrayList<>();
        for(UUID uuid : new HashSet<>(Round.teamList.keySet())) {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) {
                Round.teamList.remove(uuid);
                continue;
            }
            players.add(player);
        }
        return players;
    }
    public static List<Player> sortAlive(List<Player> get) {
        List<Player> players = new ArrayList<>();
        for(Player player : get) {
            if(player.getGameMode() == GameMode.ADVENTURE) {
                players.add(player);
            }
        }
        return players;
    }

}
