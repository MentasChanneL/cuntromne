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

    public String name;
    public final String id;
    public TextColor color;
    private Set<UUID> members;
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
        this.members.add( player.getUniqueId() );
        this.sbTeam.addPlayer( player );
        if(PlayerData.get( player ).team != null) PlayerData.get(player).team.removeMember(player);
        PlayerData.get(player).team = this;
    }

    public void removeMember(Player player) {
        this.members.remove( player.getUniqueId() );
        PlayerData.get(player).team = null;
    }

    public int getMembersCount() { return this.members.size(); }

    public void addMembers(List<Player> list) {
        for(Player player : list) {
            addMember(player);
        }
    }

    public Player getMember(int index) {
        UUID[] list = new UUID[members.size()];
        int i = 0;
        for(UUID player : members) {
            list[i] = player;
            i++;
        }
        return Bukkit.getPlayer( list[index] );
    }

    public Set<Player> getMembers() {
        Set<Player> result = new HashSet<>();
        Player player;
        for(UUID uuid : this.members) {
            player = Bukkit.getPlayer( uuid );
            if(player == null) continue;
            result.add(player);
        }
        return result;
    }

    public void clearMembers() {
        members.clear();
    }

    public Set<Player> getAliveMembers() {
        Set<Player> result = new HashSet<>();
        Player player;
        for(UUID uuid : this.members) {
            player = Bukkit.getPlayer( uuid );
            if(player == null) continue;
            if(player.getGameMode() == GameMode.ADVENTURE) result.add(player);
        }
        return result;
    }

    public void teamMessage(Component author, Component message) {
        Player player;
        for(UUID uuid : this.members) {
            player = Bukkit.getPlayer( uuid );
            if(player == null) continue;
            player.sendMessage(Component.text("(Командный) ")
                    .append(author.color( this.color ))
                    .append(Component.text( " > " ))
                    .append(message.color( TextColor.color(255, 255, 255) )));
        }
    }

}
