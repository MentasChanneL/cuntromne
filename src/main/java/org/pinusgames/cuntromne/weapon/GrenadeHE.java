package org.pinusgames.cuntromne.weapon;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.pinusgames.cuntromne.Cuntromne;
import org.pinusgames.cuntromne.utils.NBTEditor;
import org.pinusgames.cuntromne.weapon.projectile.ProjectileCreator;
import org.pinusgames.cuntromne.weapon.script.Script;

public class GrenadeHE implements WeaponActions{

    @Override
    public void fire(WeaponData data) {
        if(data.player.getCooldown( data.item.getType() ) > 0) return;
        data.player.setCooldown( data.item.getType(), 1 );
        ProjectileCreator.createHE(data.player.getEyeLocation(), data.player.getUniqueId(), 15);
        data.player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        data.player.getWorld().playSound(data.player.getLocation(), "ctum:weapon.deagle.out2", 1 ,2);
    }

    @Override
    public void reload(WeaponData data) {}

    @Override
    public void intro(WeaponData data) {}

    @Override
    public void outro(WeaponData data) {}

    @Override
    public void review(WeaponData data) {
        if(data.player.getCooldown( data.item.getType() ) > 0) return;
        data.player.setCooldown( data.item.getType(), 1 );
        Snowball snowball = ProjectileCreator.createHE(data.player.getEyeLocation(), data.player.getUniqueId(), 15);
        snowball.setVelocity( snowball.getVelocity().multiply(0.5) );
        data.player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        data.player.getWorld().playSound(data.player.getLocation(), "ctum:weapon.deagle.out2", 1 ,2);
    }

    @Override
    public void shift(WeaponData data) {

    }

    @Override
    public Script getScript() {
        return null;
    }

    public static ItemStack give(Player player) {
        ItemStack result = new ItemStack(Material.STICK);
        ItemMeta meta = result.getItemMeta();
        meta.setCustomModelData(601);
        meta.displayName(Component.text("Сидит бабушка на лавочке, к ней подходит Мэддисон и говорит: \"бабушка, не подскажешь сколько время?\" Бабушка такая смотрит на него и говорит: \"Это не у меня спрашивать надо сколько время, а у тебя\". Мэддисон такой почесал голову и говорит: \"Ну блядь, да я забыл дома телефон и ключи и часы\". Бабушка такая почесала свою голову тоже и говорит: \"Суууука....причем здесь ключи нахуй?").decoration(TextDecoration.ITALIC, false));
        result.setItemMeta(meta);
        int id = Cuntromne.getInstance().getWeaponID();
        WeaponData data = new WeaponData(new GrenadeHE(), result, 0, 0, id);
        data.player = player;
        Cuntromne.getInstance().weapons.put(id, data);
        result = NBTEditor.setItemTag(result, id, "cunt-weaponid");
        return result;
    }

}