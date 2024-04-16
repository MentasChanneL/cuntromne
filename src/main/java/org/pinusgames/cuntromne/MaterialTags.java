package org.pinusgames.cuntromne;

import org.bukkit.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MaterialTags {
    public static final Set<Material> metal = new HashSet<>( Arrays.asList(
            Material.IRON_BLOCK,
            Material.IRON_DOOR,
            Material.IRON_BARS,
            Material.COPPER_BLOCK,
            Material.IRON_TRAPDOOR,
            Material.BARRIER,
            Material.ANVIL,
            Material.CHIPPED_ANVIL,
            Material.DAMAGED_ANVIL,
            Material.OXIDIZED_COPPER,
            Material.EXPOSED_COPPER,
            Material.WEATHERED_COPPER,
            Material.WAXED_COPPER_BLOCK,
            Material.WAXED_OXIDIZED_COPPER,
            Material.WAXED_EXPOSED_COPPER,
            Material.WAXED_WEATHERED_COPPER,
            Material.QUARTZ_PILLAR,
            Material.POLISHED_DIORITE,
            Material.POLISHED_BASALT
    ) );
    public static final Set<Material> dirt = new HashSet<>( Arrays.asList(
            Material.DIRT,
            Material.COARSE_DIRT,
            Material.SAND,
            Material.GRASS_BLOCK,
            Material.PODZOL,
            Material.BASALT
    ) );

    public static final Set<Material> smokeWhiteList = new HashSet<>( Arrays.asList(
            Material.AIR,
            Material.CAVE_AIR,
            Material.VOID_AIR,
            Material.IRON_BARS,
            Material.GRASS,
            Material.BARRIER,
            Material.GRAY_CARPET
    ) );
}
