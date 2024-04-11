package org.pinusgames.cuntromne.smoke;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.MaterialTags;
import org.pinusgames.cuntromne.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmokeBlock {
    public boolean hasAir;
    public int members;
    public Location location;

    public SmokeBlock(boolean hasAir, int members, Location location) {
        this.hasAir = hasAir;
        this.members = members;
        this.location = location;
    }

    public static void createSmoke(int size, Location position, ItemStack item) {
        int i = 0;
        SmokeMap blocks = new SmokeMap();
        List<ItemDisplay> visuals = new ArrayList<>();
        main: while(i < size) {
            i++;
            Location current = position.clone().toCenterLocation();
            int max = 0;
            while (true) {
                max++;
                if(max > 1000) break main;
                SmokeBestResult result = getBestBlock(blocks, current);
                if (result.type == SmokeBestType.NOWAY) break main;
                if (result.type == SmokeBestType.GO_NEXT) {
                    current = result.move;
                    continue;
                }
                if (result.type == SmokeBestType.OPEN_HERE) {
                    SmokeBlock newBlock = getSmokeBlock(blocks, result.move);
                    blocks.put(new SmokeVector(result.move.toVector()), newBlock);
                    visuals.add( createVisual(result.move, (int)(i * (20d / size)), item) );
                    break;
                }
            }

        }

        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
            for(ItemDisplay display : visuals) {

                float rollRad = (float) Math.toRadians(Math.random() * 360);
                float pitchRad = (float) Math.toRadians(Math.random() * 360);
                float yawRad = (float) Math.toRadians(Math.random() * 360);

                Matrix3f matrix = new Matrix3f();
                matrix.rotateX(pitchRad);
                Matrix3f tempMatrix = new Matrix3f();
                tempMatrix.rotateY(yawRad);
                matrix.mul(tempMatrix);
                tempMatrix.rotateZ(rollRad);
                matrix.mul(tempMatrix);

                AxisAngle4f axisAngle = new AxisAngle4f();
                axisAngle.set(matrix);

                display.setTransformation(new Transformation(
                        new Vector3f(0, 0, 0),
                        axisAngle,
                        new Vector3f(1, 1, 1),
                        new AxisAngle4f(0, 0, 0, 0)
                ));
            }
        }, 5);
        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {

            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta meta = stick.getItemMeta();
            meta.setCustomModelData(606);
            new Timer(() -> {
                meta.setCustomModelData(meta.getCustomModelData() + 1);
                stick.setItemMeta(meta);
                for (ItemDisplay display : visuals) {
                    if (display.isValid()) {
                        display.setItemStack(stick);
                    }
                }
            }, 10, 2);


            Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {
                for(ItemDisplay display : visuals) {
                    if(display.isValid()) {
                        display.remove();
                    }
                }
            }, 20);
        }, 400);

    }

    public static SmokeBestResult getBestBlock(SmokeMap map, Location current) {
        List<Location> members = Arrays.asList(
                current.clone().add(1, 0, 0),
                current.clone().add(-1, 0, 0),
                current.clone().add(0, 0, 1),
                current.clone().add(0, 0, -1),
                current.clone().add(0, 1, 0),
                current.clone().add(0, -1, 0)
        );
        SmokeBlock favorite = null;
        for(Location locMember : members) {
            SmokeVector member = new SmokeVector( locMember.toVector() );
            if( !map.isContains( member ) && MaterialTags.smokeWhiteList.contains( locMember.getBlock().getType() ) ) {
                return new SmokeBestResult(SmokeBestType.OPEN_HERE, locMember);
            }
            if(!map.isContains( member )) continue;
            SmokeBlock block = map.get( member );
            if(!block.hasAir) continue;
            if(favorite == null || block.members < favorite.members) favorite = block;
        }
        if(favorite == null) return new SmokeBestResult(SmokeBestType.NOWAY, null);
        favorite.members++;
        return new SmokeBestResult(SmokeBestType.GO_NEXT, favorite.location);
    }

    public static SmokeBlock getSmokeBlock(SmokeMap map, Location current) {
        List<Location> members = Arrays.asList(
                current.clone().add(1, 0, 0),
                current.clone().add(-1, 0, 0),
                current.clone().add(0, 0, 1),
                current.clone().add(0, 0, -1),
                current.clone().add(0, 1, 0),
                current.clone().add(0, -1, 0)

        );
        SmokeBlock result = new SmokeBlock(false, 0, current);
        for(Location locMember : members) {
            SmokeVector member = new SmokeVector( locMember.toVector() );
            if( MaterialTags.smokeWhiteList.contains( locMember.getBlock().getType() ) ) {
                result.hasAir = true;
            }
            if(!map.isContains( member )) continue;
            result.members++;
            SmokeBlock block = map.get( member );
            if(block.hasAir) result.hasAir = true;
        }
        return result;
    }

    public static ItemDisplay createVisual(Location pos, int delay, ItemStack item) {
        Location spawn = pos.clone();
        spawn.setYaw((float)(Math.random() * 360));
        spawn.setPitch((float)(Math.random() * 180) - 90);
        ItemDisplay result = (ItemDisplay) pos.getWorld().spawnEntity(spawn, EntityType.ITEM_DISPLAY);
        result.setTransformation(new Transformation(
                new Vector3f(0, 0, 0),
                new AxisAngle4f(0, 0, 0, 0),
                new Vector3f(0, 0, 0),
                new AxisAngle4f(0, 0, 0, 0)
        ));
        result.setInterpolationDelay(delay);
        result.setInterpolationDuration(10);
        result.setItemStack(item);

        Bukkit.getScheduler().runTaskLater(Cuntromne.getInstance(), () -> {

            result.setInterpolationDuration(500);
            float rollRad = (float) Math.toRadians(Math.random() * 360);
            float pitchRad = (float) Math.toRadians(Math.random() * 360);
            float yawRad = (float) Math.toRadians(Math.random() * 360);
            Matrix3f matrix = new Matrix3f();
            matrix.rotateX(pitchRad);
            Matrix3f tempMatrix = new Matrix3f();
            tempMatrix.rotateY(yawRad);
            matrix.mul(tempMatrix);
            tempMatrix.rotateZ(rollRad);
            matrix.mul(tempMatrix);
            AxisAngle4f axisAngle = new AxisAngle4f();
            axisAngle.set(matrix);
            result.setTransformation(new Transformation(
                    new Vector3f(0, 0, 0),
                    axisAngle,
                    new Vector3f(1, 1, 1),
                    new AxisAngle4f(0, 0, 0, 0)
            ));
        }, 10 + delay);

        return result;
    }

}
