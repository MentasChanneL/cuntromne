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
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.actions.Action;
import org.pinusgames.cuntromne.actions.LostAction;
import org.pinusgames.cuntromne.actions.WinAction;
import org.pinusgames.cuntromne.donate.Knife;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.Armor;
import org.pinusgames.cuntromne.weapon.C4;
import org.pinusgames.cuntromne.weapon.Defuse;
import org.pinusgames.cuntromne.weapon.WeaponData;
import org.pinusgames.cuntromne.weapon.c4.C4Data;

import java.time.Duration;
import java.util.*;

public class Round {

    public static int roundID = -1;
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
        clearEntities();
        bossbar.setVisible(true);
        timeLeft = 2400;
        prepareLeft = 300;
        bombTimer = -1;
        Team.teamList.get("ct").wins = 0;
        Team.teamList.get("t").wins = 0;
        roundID = 0;
        Cuntromne.getInstance().resetWeaponID();
        lobbyDist();
        setScoreBossbar();
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }

        Set<Player> players = Team.teamList.get("ct").getMembers();
        players.addAll( Team.teamList.get("t").getMembers() );

        for(Player player : players) {
            player.getInventory().clear();
            player.setItemOnCursor(null);
            Shop.money.put(player.getUniqueId(), 9);
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            preparePlayer(player);
        }

        bossbarTxtCreator();
        actionBarReset();

        Team team = Team.teamList.get("t");
        if(!team.getMembers().isEmpty()) {
            Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
            random.getInventory().setItem(8, C4.give(random));
            team.teamMessage(Component.text( random.getName() ), Component.text("У меня БОМБАСТЕР!" ));
            C4Data.C4Handler = random.getUniqueId();
        }

        team = Team.teamList.get("ct");
        if(!team.getMembers().isEmpty()) {
            Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
            random.getInventory().setItem(6, Defuse.give(random));
        }

        schedulerActionbar = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::actionBarTick, 1, 1 ).getTaskId();
        schedulerTimer = Bukkit.getScheduler().runTaskTimer( Cuntromne.getInstance(), Round::tick, 1, 1 ).getTaskId();
    }

    public static void newRound() {
        if(Bukkit.getOnlinePlayers().isEmpty()) return;
        NBTEditor.clear();
        clearEntities();
        Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
        bossbar.setVisible(true);
        C4Data.defuses.clear();
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
        Set<Player> players = Team.teamList.get("ct").getMembers();
        players.addAll( Team.teamList.get("t").getMembers() );

        for(Player player : players) {
            mansBossbar.addViewer(player);
            bossbar.addPlayer(player);
            scoreBossbar.addViewer(player);
            preparePlayer(player);
        }

        actionBarReset();
        bossbarTxtCreator();

        Team team = Team.teamList.get("t");
        if(!team.getMembers().isEmpty()) {
            Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
            random.getInventory().setItem(8, C4.give(random));
            team.teamMessage(Component.text( random.getName() ), Component.text("У меня БОМБАСТЕР!" ));
            C4Data.C4Handler = random.getUniqueId();
        }

        team = Team.teamList.get("ct");
        if(!team.getMembers().isEmpty()) {
            Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
            random.getInventory().setItem(6, Defuse.give(random));
        }

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

        for(Player player : Team.teamList.get("spectator").getMembers()) {
            int ctCount = ct.getMembersCount(); int tCount = t.getMembersCount();
            if(ctCount <= tCount) {
                ct.addMember(player);
                continue;
            }
            t.addMember(player);
        }
        Team.teamList.get("spectator").clearMembers();
    }

    private static void preparePlayer(Player player) {
        Events.blockMove.add(player.getUniqueId());
        Team team = PlayerData.get(player).team;
        if( team.id.equals("ct") ) player.teleport( Config.ctspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
        if( team.id.equals("t") ) {
            player.getInventory().setItem(8, null);
            player.teleport( Config.tspawn.clone().add(Math.random() * 6 - 3, 0, Math.random() * 6 - 3) );
        }
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
        player.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, PotionEffect.INFINITE_DURATION, 250, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, 4, false, false));
    }

    private static void tick() {
        if(Round.bombTimer > -1) {
            if(Round.bombTimer == 900) bossbar.setTitle("БОМБАСТЕР");
            Round.bombTimer--;
            C4Data.bombPeek();
            if(Round.bombTimer < 1) {
                C4Data.explode();
                Bukkit.getScheduler().cancelTask( Round.schedulerTimer );
            }
            return;
        }
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
                if(!team.getMembers().isEmpty()) {
                    Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
                    team.teamMessage( Component.text( random.getName() ), Component.text("Пошли!" ) );
                    random.getLocation().getWorld().playSound(random.getLocation(), "ctum:player.t.battlecry" + (new Random().nextInt(4) + 1), 1, 1);
                }
                team = Team.teamList.get("ct");
                if(!team.getMembers().isEmpty()) {
                    Player random = team.getMember( new Random().nextInt( team.getMembers().size()) );
                    team.teamMessage( Component.text( random.getName() ), Component.text("За работу!" ) );
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
            if(ctlist.isEmpty()) {
                twin();
                return;
            }
            return;
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
                for(Player player :  t.getMembers()) {
                    Action action = new LostAction();
                    action.run(player);
                }
                for(Player player : ct.getMembers()) {
                    Action action = new WinAction();
                    action.run(player);
                }
            }
            if(ct.wins < t.wins) {
                Round.winTeam = t;
                for(Player player : ct.getMembers()) {
                    Action action = new LostAction();
                    action.run(player);
                }
                for(Player player : t.getMembers()) {
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
        if(Round.roundID > -1) newRound();
    }

    private static void twin() {
        Team ct = Team.teamList.get("ct");
        Team t = Team.teamList.get("t");
        Round.bombTimer = -3;
        t.wins++;
        for(Player player : t.getMembers()) {
            Shop.addCash(player, 15);
        }
        for(Player player : ct.getMembers()) {
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
        for(Player player : ct.getMembers()) {
            Shop.addCash(player, 15);
        }
        for(Player player : t.getMembers()) {
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
        for(Player player : ct.getMembers()) {
            Shop.addCash(player, 12);
        }
        for(Player player : t.getMembers()) {
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

        int alive = ct.getAliveMembers().size();
        int dead = ct.getMembersCount() - alive;
        int i = 0;
        StringBuilder c = new StringBuilder();
        while(i < dead) { c.append(":"); i++; }
        i = 0;
        while(i < alive) { c.append("1"); i++; }
        Component title = Component.text( c.toString() ).font( Key.key("ctum:icons") );

        c = new StringBuilder();
        alive = t.getAliveMembers().size();
        dead = t.getMembersCount() - alive;
        i = 0;
        while(i < alive) { c.append("2"); i++; }
        i = 0;
        while(i < dead) { c.append(";"); i++; }

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

    private static void clearEntities() {
        for(Entity ent : Config.ctspawn.getWorld().getEntities()) {
            if(ent instanceof ItemDisplay) ent.remove();
            if(ent instanceof Snowball) ent.remove();
            if(ent instanceof Item) ent.remove();
        }
    }

    public static void setAB(Player player, Component component) {
        actionBars.put(player, component);
    }

    public static List<Player> selectInGame() {
        List<Player> players = new ArrayList<>();
        for(Team team : Team.teamList.values()) {
            if(team.id.equals("spectator")) continue;
            players.addAll(team.getMembers());
        }
        return players;
    }

    public static void closeGame() {
        Round.timeLeft = 0;
        Round.prepareLeft = 0;
        Round.bombTimer = -3;
        Bukkit.getScheduler().cancelTask(Round.schedulerTimer);
        Round.schedulerTimer = -1;
        Bukkit.getScheduler().cancelTask(Round.schedulerActionbar);
        Round.schedulerActionbar = -1;
        Round.endGameEvent = false;
        Round.winTeam = null;
        Round.roundID = -1;
        for(int i = 0; i < bossbar.getPlayers().size(); i++) {
            scoreBossbar.removeViewer( bossbar.getPlayers().get(i) );
            mansBossbar.removeViewer( bossbar.getPlayers().get(i) );
            bossbar.removePlayer( bossbar.getPlayers().get(i) );
        }
        clearEntities();
        Team t = Team.teamList.get("t");
        Team ct = Team.teamList.get("ct");
        Team s = Team.teamList.get("spectator");
        Set<Player> players = t.getMembers();
        players.addAll( ct.getMembers() );
        for(Player player : players) {
            s.addMember(player);
            player.setGameMode( GameMode.SPECTATOR );
            player.getInventory().clear();
        }
        Bukkit.broadcast(Component.text("Игра остановлена..."));
    }

}
