package org.pinusgames.cuntromne.actions;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Team;

import java.time.Duration;

public class IntroAction implements Action {

    private ActionResult result;
    private Player player;
    private Action trigger;

    private byte ignore = 1;
    private int frame = 0;
    private int scheduler = 0;

    @Override
    public void setIrritate(Action trigger) {
        this.trigger = trigger;
    }

    @Override
    public void run(Player target) {
        target.playSound(target.getLocation(), "ctum:intro", 1, 1);
        this.player = target;
        this.result = ActionResult.IS_RUNNING;

        this.scheduler = Bukkit.getScheduler().runTaskTimer(Cuntromne.getInstance(), () -> {

            if(!target.isValid()) { cancel(ActionResult.BAD_END); return; }

            if(this.frame > 57) {
                target.showTitle(
                        Title.title( Component.text( ")" ).font(Key.key("ctum:intro")),
                                Component.text(")").font( Key.key("ctum:intro") ),
                                Title.Times.times( Duration.ofSeconds(0), Duration.ofSeconds(10), Duration.ofSeconds(0) )));
                Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
                    target.playSound(target.getLocation(), "ctum:logo", 1, 1);
                    Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
                        target.showTitle(
                                Title.title(Component.text("\u0090").font(Key.key("ctum:intro")),
                                        Component.text(")").font(Key.key("ctum:intro")),
                                        Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(5), Duration.ofSeconds(0))));
                        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
                            target.showTitle(
                                    Title.title(Component.text(")").font(Key.key("ctum:intro")),
                                            Component.text(""),
                                            Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(1))));
                            cancel(ActionResult.GOOD_END);
                        }, 80);
                    }, 110);
                }, 20);
                Bukkit.getScheduler().cancelTask(this.scheduler);
                return;
            }

            if(this.ignore != 0) {
                String unicodeString = "00" + (30 + this.frame);
                char unicodeChar = Character.toChars(Integer.parseInt(unicodeString, 16))[0];
                target.showTitle( Title.title(
                        Component.text( unicodeChar ).font( Key.key("ctum:intro") ),
                        Component.text(")").font( Key.key("ctum:intro") ),
                        Title.Times.times( Duration.ofSeconds(0), Duration.ofSeconds(1), Duration.ofSeconds(0) )));
                this.frame++;
            }
            this.ignore++;
            if(this.ignore > 1) {
                this.ignore = 0;
            }

        },1,1).getTaskId();

    }

    @Override
    public ActionResult getActionResult() {
        return this.result;
    }

    @Override
    public void cancel(ActionResult result) {
        Bukkit.getScheduler().cancelTask(this.scheduler);
        this.result = result;
        if(this.result == ActionResult.GOOD_END) irritate();
    }

    @Override
    public void irritate() {
        if(this.trigger != null) this.trigger.run(this.player);
    }

}
