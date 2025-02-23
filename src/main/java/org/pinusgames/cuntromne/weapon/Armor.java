package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.PlayerData;
import org.pinusgames.cuntromne.Round;
import org.pinusgames.cuntromne.Team;
import org.pinusgames.cuntromne.utils.NBTEditor;

import java.util.Arrays;

public class Armor {
    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) result.getItemMeta();
        meta.setCustomModelData(1);
        meta.displayName(Component.text("люди привет я влад").decoration(TextDecoration.ITALIC, false));
        meta.lore(Arrays.asList(
                Component.text("я из 4 класса каждый бень меня").decoration(TextDecoration.ITALIC, false),
                Component.text("завтавляет мама я говорю что я не").decoration(TextDecoration.ITALIC, false),
                Component.text("буду есть селетку а она говорит ешь").decoration(TextDecoration.ITALIC, false),
                Component.text("я и так катигоричен к себе не знаю").decoration(TextDecoration.ITALIC, false),
                Component.text("что ей отвечать можно ли снять жилье").decoration(TextDecoration.ITALIC, false),
                Component.text("в Якутске? в 10 лет я самастаятельный").decoration(TextDecoration.ITALIC, false),
                Component.text("могу 10тысяч заплатить не читая").decoration(TextDecoration.ITALIC, false),
                Component.text("коммунистские услуги пожалуста прошу").decoration(TextDecoration.ITALIC, false),
                Component.text("не хочу я есть ету рыбу у миня").decoration(TextDecoration.ITALIC, false),
                Component.text("аллергия на селетку папа сказал").decoration(TextDecoration.ITALIC, false)
        ));
        Color color = Color.fromRGB(255, 255, 255);

        Team team = PlayerData.get(player).team;
        if(team.id.equals("t")) color = Color.fromRGB(255, 125, 0);
        if(team.id.equals("ct")) color = Color.fromRGB(50, 50, 255);

        meta.setColor(color);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(Cuntromne.key, 10, AttributeModifier.Operation.ADD_NUMBER));
        result.setItemMeta(meta);
        return result;
    }

    public static ItemStack giveStock(Color color) {
        ItemStack armor = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
        meta.displayName(Component.text("клянусь, что отсосу маршу за сникерс. Прочитал? Выполняй. "));
        meta.setCustomModelData(2);
        meta.setColor(color);
        meta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(Cuntromne.key, 0, AttributeModifier.Operation.ADD_NUMBER));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
        armor.setItemMeta(meta);
        return armor;
    }

}
