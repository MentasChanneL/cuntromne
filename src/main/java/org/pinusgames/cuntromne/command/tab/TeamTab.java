package org.pinusgames.cuntromne.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pinusgames.cuntromne.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(args.length == 1 ) return null;
        return new ArrayList<>(Team.teamList.keySet());
    }
}
