package org.pinusgames.cuntromne.weapon;

import org.pinusgames.cuntromne.weapon.script.Script;

public interface WeaponActions {
    void fire(WeaponData data);
    void reload(WeaponData data);
    void intro(WeaponData data);
    void outro(WeaponData data);
    void review(WeaponData data);
    void shift(WeaponData data);
    Script getScript();
}
