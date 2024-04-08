package org.pinusgames.cuntromne.actions;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionChain {
    private final List<Action> actions;

    public ActionChain(Action ... actions) {
        Action previous = null;
        this.actions = new ArrayList<>();
        for(Action action : actions) {
            this.actions.add(action);
            if(previous != null) {
                previous.setIrritate(action);
            }
            previous = action;
        }
    }

    public void run(Player player) {
        this.actions.get(0).run(player);
    }

    public static void runActionChain(Player player, Action ... actions) {
        ActionChain chain = new ActionChain(actions);
        chain.run(player);
    }

}
