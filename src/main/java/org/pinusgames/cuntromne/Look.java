package org.pinusgames.cuntromne;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Look {
    public static boolean isPlayerLookingAtLocation(Player player, Location location, double threshold) {
        // Получаем направление, в котором смотрит игрок
        Vector playerDirection = player.getLocation().getDirection();

        // Получаем вектор от игрока до точки
        Vector toLocation = location.toVector().subtract(player.getLocation().toVector()).normalize();

        // Проверяем угол между векторами, чтобы понять, смотрит ли игрок на эту точку
        double dot = playerDirection.dot(toLocation);

        // Устанавливаем пороговое значение для точности
        return dot > threshold;
    }

    public static boolean wayIsClear(Location loc1, Location loc2) {
        World world = loc1.getWorld();
        Vector startVec = loc1.toVector();
        Vector endVec = loc2.toVector();

        Vector direction = endVec.clone().subtract(startVec).normalize();
        RayTraceResult result = world.rayTraceBlocks( loc1, direction, loc1.distance( loc2 ) );
        return result == null;
    }

}
