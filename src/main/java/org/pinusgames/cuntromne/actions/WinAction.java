package org.pinusgames.cuntromne.actions;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.pinusgames.cuntromne.Config;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Events;
import org.pinusgames.cuntromne.Round;

import java.time.Duration;

public class WinAction implements Action{

    private ActionResult result;
    private Player player;
    private Action trigger;

    @Override
    public void setIrritate(Action trigger) {
        this.trigger = trigger;
    }

    @Override
    public void run(Player target) {
        this.player = target;
        this.player.teleport(Config.login);
        Events.blockMove.remove(this.player.getUniqueId());
        this.player.setGameMode(GameMode.SPECTATOR);
        this.player.setSpectatorTarget(Bukkit.getEntity( Config.endgameentity ));
        this.player.setHealth(20);
        this.player.setSaturation(20);
        this.player.playSound(this.player.getLocation(), "ctum:win", 1, 1);
        Round.setAB(this.player, Component.text("8").font(Key.key("ctum:icons")));
        Events.blockMove.add(this.player.getUniqueId());

        int repeat = Bukkit.getScheduler().runTaskTimer(Cuntromne.getInstance(), () -> {
            if(!player.isValid()) cancel(ActionResult.BAD_END);
            this.player.showTitle(Title.title(
                    Component.text(""),
                    Component.keybind("key.sneak").color(TextColor.color(255, 200, 0))
                            .append(Component.text(", чтобы посмеятся над лузерами")),
                    Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(2))
            ));
        },1,5).getTaskId();

        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
            Bukkit.getScheduler().cancelTask(repeat);
            cancel(ActionResult.GOOD_END);
        }, 140);
    }

    @Override
    public ActionResult getActionResult() {
        return this.result;
    }

    @Override
    public void cancel(ActionResult result) {
        this.result = result;
        if(this.result == ActionResult.GOOD_END) irritate();
    }

    @Override
    public void irritate() {
        if(this.trigger != null) this.trigger.run(this.player);
    }
}
