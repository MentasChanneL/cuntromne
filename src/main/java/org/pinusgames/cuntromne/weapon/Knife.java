package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.Look;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.script.Script;

public class Knife implements WeaponActions{


    @Override
    public void fire(WeaponData data) {

    }

    @Override
    public void reload(WeaponData data) {}

    @Override
    public void intro(WeaponData data) {
        data.player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        data.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 255, false, false));
    }

    @Override
    public void outro(WeaponData data) {
        data.player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
        data.player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, PotionEffect.INFINITE_DURATION, 250, false, false));
    }

    @Override
    public void review(WeaponData data) {}

    @Override
    public void shift(WeaponData data) {

    }

    @Override
    public Script getScript() {
        return null;
    }

    public static ItemStack give(Player player, int skin) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(skin);
        meta.displayName(Component.text("ВТЫК").decoration(TextDecoration.ITALIC, false));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier("damag", 50, AttributeModifier.Operation.ADD_NUMBER));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new Knife(), result, 0, 0, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}