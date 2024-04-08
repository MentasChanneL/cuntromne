package org.pinusgames.cuntromne.actions;

import org.bukkit.entity.Player;

public interface Action {

    void setIrritate(Action trigger);
    void run(Player target);
    ActionResult getActionResult();
    void cancel(ActionResult result);
    void irritate();
}

