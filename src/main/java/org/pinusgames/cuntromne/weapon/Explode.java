package org.pinusgames.cuntromne.weapon;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.pinusgames.cuntromne.Cuntromne;

import java.util.UUID;

public class Explode {

    public final UUID entity;
    public int frame;
    public int task;

    public Explode(Location spawnPos, int size, int speed) {
        ItemDisplay id = (ItemDisplay) spawnPos.getWorld().spawnEntity(spawnPos, EntityType.ITEM_DISPLAY);
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(700);
        item.setItemMeta(meta);
        id.setTransformation(
                new Transformation(new Vector3f(0, (float)((double)size / 2 + 0.5), 0),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(size, size, size),
                new AxisAngle4f(0, 0, 0, 0))
        );
        id.setBillboard(Display.Billboard.VERTICAL);
        id.setBrightness(new Display.Brightness(15, 15));
        id.setItemStack(item);
        id.addScoreboardTag("session");
        spawnPos.getWorld().spawnParticle(Particle.LAVA, spawnPos, 10);
        this.entity = id.getUniqueId();
        this.task = 0;
        this.frame = 0;
        this.task = Bukkit.getScheduler().runTaskTimer(Cuntromne.getInstance(), () -> {
            this.frame++;
            if(this.frame > 16) {
                Bukkit.getScheduler().cancelTask(this.task);
                id.remove();
                return;
            }
            meta.setCustomModelData(700 + this.frame);
            item.setItemMeta(meta);
            id.setItemStack(item);
        },1,speed).getTaskId();
    }

}
