package org.pinusgames.cuntromne;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Shop {
    public static Inventory menu = Bukkit.createInventory(null,
            54,
            Component.text("\uF808").append(
                    Component.text("5").font(Key.key("ctum:icons"))
                    .color(TextColor.color(255, 255, 255))
            ));
    public static HashMap<UUID, Integer> money = new HashMap<>();
    public static HashMap<Integer, Integer> cost = null;
    public static HashMap<Integer, Shop.Give> giveMap = null;

    public static void showMenu(Player player) {
        if(cost == null) {
            initialize();
        }
        if(!money.containsKey(player.getUniqueId())) {
            money.put(player.getUniqueId(), 0);
        }
        setMan(player);
        player.openInventory(menu);
    }

    public static void addCash(Player player, int count) {
        if(!money.containsKey(player.getUniqueId())) { money.put(player.getUniqueId(), count); return; }
        int balance = money.get( player.getUniqueId() );
        balance = balance + count;
        money.put(player.getUniqueId(), balance);
    }

    public static void close(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.getInventory().setItem(10, null);
    }

    private static void initialize() {
        cost = new HashMap<>();
        cost.put(12, 2);
        cost.put(14, 3);
        cost.put(16, 8);
        cost.put(30, 27);
        cost.put(32, 27);
        cost.put(34, 45);
        cost.put(46, 10);
        cost.put(48, 4);
        cost.put(50, 3);
        //cost.put(52, 3);
        giveMap = new HashMap<>();
        giveMap.put(12, new Give(1, "glock"));
        giveMap.put(14, new Give(1, "usp"));
        giveMap.put(16, new Give(1, "deagle"));
        giveMap.put(30, new Give(0, "ak"));
        giveMap.put(32, new Give(0, "m4a1"));
        giveMap.put(34, new Give(0, "awp"));
        giveMap.put(46, new Give(38, "armor"));
        giveMap.put(48, new Give(3, "he"));
        giveMap.put(50, new Give(2, "flash"));
        //giveMap.put(52, new Give(4));
        menu.setItem(12, getShopItem(650, 2));
        menu.setItem(14, getShopItem(651, 3));
        menu.setItem(16, getShopItem(652, 8));
        menu.setItem(30, getShopItem(653, 27));
        menu.setItem(32, getShopItem(654, 27));
        menu.setItem(34, getShopItem(655, 45));
        menu.setItem(46, getShopItem(659, 10));
        menu.setItem(48, getShopItem(656, 4));
        menu.setItem(50, getShopItem(657, 3));
    }

    private static ItemStack getShopItem(int customModelData, int price) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(customModelData);
        List<Component> components = new ArrayList<>();
        components.add(
                Component.text(" " + price)
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
        return result;
    }

    public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        if(cost.containsKey( slot )) {
            int price = cost.get(slot);
            int balance = money.get(player.getUniqueId());
            if(price > balance) {
                player.playSound(player.getLocation(), "ctum:round", 1F, 2F);
                return;
            }
            balance -= price;
            money.put(player.getUniqueId(), balance);
            giveMap.get(slot).give(player);
            setMan(player);
            player.playSound(player.getLocation(), "ctum:round", 1, 0.9F);
        }
    }

    private static void setMan(Player player) {
        ItemStack armor = player.getInventory().getItem(EquipmentSlot.CHEST);
        ItemMeta meta = armor.getItemMeta();
        if(Round.teamList.containsKey(player.getUniqueId())) {
            Team team = Round.teamList.get(player.getUniqueId());

            if(meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
                if(team.id.equals("t")) {
                    ItemStack result = new ItemStack(Material.STICK);
                    ItemMeta resMeta = result.getItemMeta();
                    resMeta.setCustomModelData(693);
                    resMeta.displayName(Component.text("репис"));
                    result.setItemMeta(resMeta);
                    player.getInventory().setItem(10, result);
                }
                if(team.id.equals("ct")) {
                    ItemStack result = new ItemStack(Material.STICK);
                    ItemMeta resMeta = result.getItemMeta();
                    resMeta.setCustomModelData(691);
                    resMeta.displayName(Component.text("репис"));
                    result.setItemMeta(resMeta);
                    player.getInventory().setItem(10, result);
                }
                return;
            }

            if(team.id.equals("t")) {
                ItemStack result = new ItemStack(Material.STICK);
                ItemMeta resMeta = result.getItemMeta();
                resMeta.setCustomModelData(692);
                resMeta.displayName(Component.text("репис"));
                result.setItemMeta(resMeta);
                player.getInventory().setItem(10, result);
            }
            if(team.id.equals("ct")) {
                ItemStack result = new ItemStack(Material.STICK);
                ItemMeta resMeta = result.getItemMeta();
                resMeta.setCustomModelData(690);
                resMeta.displayName(Component.text("репис"));
                result.setItemMeta(resMeta);
                player.getInventory().setItem(10, result);
            }
        }
    }

    public static class Give {

        private final int slot;
        private final String item;

        public Give(int slot, String item) {
            this.slot = slot;
            this.item = item;
        }

        public void give(Player player) {
            int oldSlot = player.getInventory().getHeldItemSlot();
            int newSlot = 8;
            player.getInventory().setHeldItemSlot(8);
            ItemStack give = null;
            if(item.equals("ak")) give = Ak47.give(player);
            if(item.equals("deagle")) give = Deagle.give(player);
            if(item.equals("m4a1")) give = M4A1.give(player);
            if(item.equals("awp")) give = Awp.give(player);
            if(item.equals("glock")) give = Glock.give(player);
            if(item.equals("tester")) give = Tester.give(player);
            if(item.equals("he")) give = GrenadeHE.give(player);
            if(item.equals("flash")) give = GrenadeFlash.give(player);
            if(item.equals("armor")) {
                give = Armor.give(player);
                player.getWorld().playSound(player.getLocation(), "ctum:armor", 1, 1);
            }
            player.getInventory().setItem(slot, give);
            if(slot < 9) {
                newSlot = slot;
                player.getInventory().setHeldItemSlot(slot);
            }

            ItemStack newW = player.getInventory().getItem( newSlot );
            ItemStack oldW = player.getInventory().getItem( oldSlot );
            Object tag = NBTEditor.getItemTag(newW, "cunt-weaponid");
            if(tag != null && Cuntromne.getInstance().weapons.containsKey( (int) tag )) {
                int id = (int) tag;
                Bukkit.getScheduler().runTask(Cuntromne.getInstance(), () -> {
                    Cuntromne.getInstance().weapons.get(id).intro();
                });
            }
            tag = NBTEditor.getItemTag(oldW, "cunt-weaponid");
            if(tag != null && Cuntromne.getInstance().weapons.containsKey( (int) tag )) {
                Cuntromne.getInstance().weapons.get( (int) tag ).outro();
            }

        }
    }

}
