package org.pinusgames.cuntromne.command.tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Givewptab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        if(args.length == 2) {
            result.add("ak");
            result.add("deagle");
            result.add("m4a1");
            result.add("awp");
            result.add("glock");
            result.add("tester");
            result.add("he");
            result.add("flash");
            result.add("knife");
            result.add("smoke");
            return result;
        }
        return null;
    }
}
