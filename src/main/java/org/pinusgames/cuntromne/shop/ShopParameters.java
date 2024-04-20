package org.pinusgames.cuntromne.shop;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopParameters {
    private final HashMap<Integer, SlotParameter> slots;
    private final Inventory menu;

    public ShopParameters(Inventory menu) {
        this.slots = new HashMap<>();
        this.menu = menu;
    }

    public void add(SlotParameter slot) {
        this.slots.put(slot.slot, slot);

        ItemStack result = new ItemStack(Material.SNOWBALL);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData( slot.cmd );
        List<Component> components = new ArrayList<>();
        components.add(
                Component.text(" " + slot.cost)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(255, 255, 255))
                        .append(
                                Component.text("6")
                                        .font(Key.key("ctum:icons")))
                        .append(
                                Component.text(" ")
                                        .font(Key.key("default"))
                        )
        );
        components.add(Component.text(" "));
        meta.lore( components );
        meta.displayName(Component.text(""));
        result.setItemMeta(meta);
        this.menu.setItem(slot.slot, result);
    }

    public SlotParameter get(int slot) {
        if(!this.slots.containsKey( slot )) return null;
        return this.slots.get( slot );
    }

}
