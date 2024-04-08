package org.pinusgames.cuntromne.actions;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.pinusgames.cuntromne.Config;
import org.pinusgames.cuntromne.Events;

public class LobbyAction implements Action{

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

        this.player.teleport(Config.lobby);
        Events.blockMove.remove(this.player.getUniqueId());
        this.player.setGameMode(GameMode.ADVENTURE);
        this.player.setHealth(20);
        this.player.setSaturation(20);
        cancel(ActionResult.GOOD_END);
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
