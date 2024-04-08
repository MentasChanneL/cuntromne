package org.pinusgames.cuntromne.actions;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.pinusgames.cuntromne.Events;

public class JoinAction implements Action{

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

        if(!this.player.isValid()) {
            cancel(ActionResult.BAD_END);
            return;
        }

        Events.blockMove.add(target.getUniqueId());

        this.player.setGameMode(GameMode.SPECTATOR);
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
