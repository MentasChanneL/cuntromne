package org.pinusgames.cuntromne;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.shop.ShopParameters;
import org.pinusgames.cuntromne.shop.SlotParameter;

import java.util.HashMap;
import java.util.UUID;

public class Shop {
    private final static Inventory menuCT = Bukkit.createInventory(null,
            54,
            Component.text("\uF808").append(
                    Component.text("5").font(Key.key("ctum:icons"))
                    .color(TextColor.color(255, 255, 255))
            ));
    private final static Inventory menuT = Bukkit.createInventory(null,
            54,
            Component.text("\uF808").append(
                    Component.text("5").font(Key.key("ctum:icons"))
                            .color(TextColor.color(255, 255, 255))
            ));
    public static HashMap<UUID, Integer> money = new HashMap<>();
    private static HashMap<Inventory, ShopParameters> parameters = null;

    public static boolean isMenu(Inventory menu) {
        return parameters.containsKey(menu);
    }

    public static void showMenu(Player player) {
        if(!money.containsKey(player.getUniqueId())) {
            money.put(player.getUniqueId(), 0);
        }
        setMan(player);
        player.playSound(player.getLocation(), "ctum:shop.open", 1, 1);
        Team team = PlayerData.get(player).team;
        if(team == null) return;
        if(team.id.equals( "ct" )) player.openInventory( menuCT );
        if(team.id.equals( "t" )) player.openInventory( menuT );
    }

    public static void addCash(Player player, int count) {
        if(count < 0) {
            player.sendMessage(Component.text("Вы оштрафованны на ").color(TextColor.color(255, 255, 255))
                    .append(Component.text(count).color(TextColor.color(255, 40, 14)))
                    .append(Component.text("6").font(Key.key("ctum:icons")).color(TextColor.color(255, 40, 14)))
                    .append(Component.text("!").font(Key.key("minecraft:default")).color(TextColor.color(255, 255, 255)))
            );
        }else{
            player.sendMessage(Component.text("Вам зачисленно ").color(TextColor.color(255, 255, 255))
                    .append(Component.text(count).color(TextColor.color(100, 255, 0)))
                    .append(Component.text("6").font(Key.key("ctum:icons")).color(TextColor.color(100, 255, 0)))
                    .append(Component.text("!").font(Key.key("minecraft:default")).color(TextColor.color(255, 255, 255)))
            );
        }
        if(!money.containsKey(player.getUniqueId())) { money.put(player.getUniqueId(), count); return; }
        int balance = money.get( player.getUniqueId() );
        balance = balance + count;
        if(balance < 0) balance = 0;
        if(balance > 50) balance = 50;
        money.put(player.getUniqueId(), balance);
        setMoneys(player);
    }

    public static int getMoney(Player player) {
        if(!money.containsKey(player.getUniqueId())) money.put(player.getUniqueId(), 0);
        return money.get(player.getUniqueId());
    }

    public static void close(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        player.playSound(player.getLocation(), "ctum:shop.close", 1, 1);
        player.getInventory().setItem(10, null);
    }

    public static void initialize() {

        Shop.parameters = new HashMap<>();

        ShopParameters parameter = new ShopParameters( menuCT );
        parameter.add( new SlotParameter(15, "m4a1", 27, 0, 654) );
        parameter.add( new SlotParameter(16, "awp", 45, 0, 655) );

        parameter.add( new SlotParameter(30, "p250", 3, 1, 662) );
        parameter.add( new SlotParameter(32, "usp", 5, 1, 651) );
        parameter.add( new SlotParameter(34, "deagle", 7, 1, 652) );

        parameter.add( new SlotParameter(48, "he", 4, 4, 656) );
        parameter.add( new SlotParameter(50, "flash", 2, 3, 657) );
        parameter.add( new SlotParameter(52, "smoke", 3, 5, 658) );

        parameter.add( new SlotParameter(36, "defuse", 4, 6, 661) );
        parameter.add( new SlotParameter(37, "armor", 10, 38, 659) );

        Shop.parameters.put(menuCT, parameter);

        parameter = new ShopParameters( menuT );
        parameter.add( new SlotParameter(15, "ak", 27, 0, 653) );
        parameter.add( new SlotParameter(16, "awp", 45, 0, 655) );

        parameter.add( new SlotParameter(30, "glock", 3, 1, 650) );
        parameter.add( new SlotParameter(32, "tec9", 5, 1, 663) );
        parameter.add( new SlotParameter(34, "deagle", 7, 1, 652) );

        parameter.add( new SlotParameter(48, "he", 4, 4, 656) );
        parameter.add( new SlotParameter(50, "flash", 2, 3, 657) );
        parameter.add( new SlotParameter(52, "smoke", 3, 5, 658) );

        parameter.add( new SlotParameter(36, "fakec4", 3, 6, 660) );
        parameter.add( new SlotParameter(37, "armor", 10, 38, 659) );

        Shop.parameters.put(menuT, parameter);
    }

    public static void click(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ShopParameters par = parameters.get(event.getClickedInventory());
        SlotParameter slot = par.get( event.getSlot() );
        if(slot == null) return;
        int balance = money.get(player.getUniqueId());
        if(slot.cost > balance) {
            player.playSound(player.getLocation(), "ctum:round", 1F, 2F);
            return;
        }
        balance -= slot.cost;
        money.put(player.getUniqueId(), balance);
        slot.giveItem(player);
        setMan(player);
        player.playSound(player.getLocation(), "ctum:round", 1, 0.9F);
    }

    private static void setMan(Player player) {
        setMoneys(player);
        ItemStack armor = player.getInventory().getItem(EquipmentSlot.CHEST);
        ItemMeta meta = armor.getItemMeta();
        Team team = PlayerData.get(player).team;

        ItemStack result = new ItemStack(Material.SNOWBALL);
        ItemMeta resMeta = result.getItemMeta();
        resMeta.displayName(Component.text("репис"));

        if(meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 1) {
            if(team.id.equals("t")) resMeta.setCustomModelData(693);
            if(team.id.equals("ct")) resMeta.setCustomModelData(691);
            result.setItemMeta(resMeta);
            player.getInventory().setItem(10, result);
            return;
        }
        if(team.id.equals("t")) resMeta.setCustomModelData(692);
        if(team.id.equals("ct")) resMeta.setCustomModelData(690);
        result.setItemMeta(resMeta);
        player.getInventory().setItem(10, result);
    }

    private static void setMoneys(Player player) {

        ItemStack bg = new ItemStack(Material.SNOWBALL);
        ItemMeta metaBG = bg.getItemMeta();
        metaBG.setCustomModelData(680);
        metaBG.displayName(Component.text(""));
        bg.setItemMeta( metaBG );

        ItemStack operand1 = new ItemStack( Material.SNOWBALL );
        ItemStack operand2 = new ItemStack( Material.SNOWBALL );
        ItemMeta op1Meta = operand1.getItemMeta();
        ItemMeta op2Meta = operand2.getItemMeta();
        op1Meta.displayName(Component.text(""));
        op2Meta.displayName(Component.text(""));

        int moneys = Shop.getMoney( player );
        int cmdOp1 = 670 + (moneys / 10);
        int cmdOp2 = 670 + (moneys % 10);

        op1Meta.setCustomModelData( cmdOp1 );
        op2Meta.setCustomModelData( cmdOp2 );

        operand1.setItemMeta( op1Meta );
        operand2.setItemMeta( op2Meta );

        player.getInventory().setItem(14, bg);
        player.getInventory().setItem(15, null);
        if(cmdOp1 != 670) player.getInventory().setItem(15, operand1);
        player.getInventory().setItem(16, operand2);

    }

}
