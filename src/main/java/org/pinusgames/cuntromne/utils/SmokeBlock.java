package org.pinusgames.cuntromne.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Vector;
import org.pinusgames.cuntromne.MaterialTags;

import java.util.Arrays;
import java.util.HashMap;
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

    public static void createSmoke(int size, Location position) {
        int i = 0;
        SmokeMap blocks = new SmokeMap();
        String log = "----";
        main: while(i < size) {
            i++;
            Location current = position.clone().toCenterLocation();
            //log = log + "\ni = i" + current;
            int max = 0;
            while (true) {
                max++;
                if(max > 1000) break main;
                SmokeBestResult result = getBestBlock(blocks, current);
                if (result.type == SmokeBestType.NOWAY) break main;
                if (result.type == SmokeBestType.GO_NEXT) {
                    //log = log + "\ngo next";
                    current = result.move;
                    continue;
                }
                if (result.type == SmokeBestType.OPEN_HERE) {
                    //log = log + "\nopen here";
                    SmokeBlock newBlock = getSmokeBlock(blocks, result.move);
                    blocks.put(new SmokeVector(result.move.toVector()), newBlock);
                    result.move.getWorld().spawnParticle(Particle.END_ROD, result.move, 0);
                    break;
                }
            }
            //log = log + "\n-";
        }
        //System.out.println(log + blocks);
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
                current.getWorld().spawnParticle(Particle.BLOCK_MARKER, current, 1, Material.RED_STAINED_GLASS.createBlockData());
                current.getWorld().spawnParticle(Particle.BLOCK_MARKER, locMember, 1, Material.LIME_STAINED_GLASS.createBlockData());
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

}
