package org.pinusgames.cuntromne.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 1) {
            result.add("!reload");
            result.add("lobby");
            result.add("login");
            result.add("t");
            result.add("ct");
            result.add("endgameentity");
            result.add("plantA");
            result.add("plantB");
            return result;
        }
        return null;
    }
}
