package org.pinusgames.cuntromne.weapon;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WeaponData {

    public Player player;
    public ItemStack item;
    public int ammo;
    public int maxAmmo;
    public int ammoContainer;
    public final WeaponActions actions;
    public final int weaponId;

    public WeaponData(WeaponActions actions, ItemStack item, int maxAmmo, int ammoContainer, int weaponId) {
        this.actions = actions;
        this.item = item;
        this.maxAmmo = maxAmmo;
        this.ammoContainer = ammoContainer;
        this.ammo = maxAmmo;
        this.weaponId = weaponId;
    }

    public void fire() {this.actions.fire(this);}
    public void reload() {this.actions.reload(this);}
    public void intro() {this.actions.intro(this);}
    public void outro() {this.actions.outro(this);}
    public void review() {this.actions.review(this);}

}
