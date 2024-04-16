package org.pinusgames.cuntromne;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Team {

    public static final HashMap<String, Team> teamList = new HashMap<>();

    public String name = "";
    public final String id;
    public TextColor color;
    public Set<Player> members;
    public int wins;
    public org.bukkit.scoreboard.Team sbTeam;

    public Team(String id, String name, TextColor color, ChatColor teamColor) {
        this.id = id;
        this.name = name;
        this.members = new HashSet<>();
        this.color = color;
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.sbTeam = scoreboard.getTeam(id);
        if (this.sbTeam == null) {
            this.sbTeam = scoreboard.registerNewTeam(id);
        }
        this.sbTeam.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.FOR_OWN_TEAM);
        this.sbTeam.setColor(teamColor);
        Team.teamList.put(id, this);
    }

    public void addMember(Player player) {
        this.members.add(player);
        if(PlayerData.get(player).team != null) PlayerData.get(player).team.removeMember(player);
        PlayerData.get(player).team = this;
    }

    public void removeMember(Player player) {
        this.members.remove(player);
        PlayerData.get(player).team = null;
    }

    public int getMembersCount() { return this.members.size(); }

    public void addMembers(List<Player> list) {
        for(Player player : list) {
            addMember(player);
        }
    }

    public Player getMember(int index) {
        Player[] list = new Player[members.size()];
        int i = 0;
        for(Player player : members) {
            list[i] = player;
            i++;
        }
        return list[index];
    }

    public int getAliveMembersCount() {
        int count = 0;
        for(Player player : this.members) {
            if(player.getGameMode() == GameMode.ADVENTURE) count++;
        }
        return count;
    }

    public Set<Player> getAliveMembers() {
        Set<Player> result = new HashSet<>();
        for(Player player : this.members) {
            if(player.getGameMode() == GameMode.ADVENTURE) result.add(player);
        }
        return result;
    }

    public void teamMessage(Component message) {
        for(Player player : this.members) {
            player.sendMessage(Component.text("(Командный) ").append(message));
        }
    }

    public static void teamMessage(String id, Component message) {
        Set<Player> listeners = Team.teamList.get(id).members;
        for(Player player : listeners) {
            player.sendMessage(Component.text("(Командный) ").append(message));
        }
    }

}
