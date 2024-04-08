package org.pinusgames.cuntromne.weapon.projectile;

import java.util.UUID;

public class Projectile {
    public final long spawnTime;
    public final UUID owner;
    public final int damage;
    public final UUID entity;
    public final ProjectileType type;
    public int data;

    public Projectile(long spawnTime, UUID owner, int damage, UUID entity, ProjectileType type, int data) {
        this.spawnTime = spawnTime;
        this.owner = owner;
        this.damage = damage;
        this.entity = entity;
        this.type = type;
        this.data = data;
    }
}
