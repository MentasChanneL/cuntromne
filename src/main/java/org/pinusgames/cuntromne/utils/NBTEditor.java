package org.pinusgames.cuntromne.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

public final class NBTEditor {

    private static int id = 0;
    private static final HashMap<String, DataContainer> data = new HashMap<>();

    public static void clear() {
        id = 0;
        data.clear();
    }

    public static Object getItemTag(ItemStack item, String key) {
        String id = getIdNullable(item);
        if(id == null) return null;
        DataContainer container = data.get(id);
        if(container == null) return null;
        return container.get(key);
    }

    public static ItemStack setItemTag(ItemStack item, Object value, String ... keys) {
        String id = getId(item);
        DataContainer container = data.get(id);
        if(container == null) {
            container = new DataContainer();
            data.put(id, container);
        }
        for(String key : keys) {
            container.add(key, value);
        }
        return item;
    }

    private static String getId(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        try {
            return PlainTextComponentSerializer.plainText().serialize( meta.lore().get(0) );
        }catch (Throwable e) {
            int idc = id++;
            meta.lore( List.of(Component.text(idc)) );
            item.setItemMeta(meta);
            return String.valueOf( idc );
        }
    }

    private static String getIdNullable(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        try {
            return PlainTextComponentSerializer.plainText().serialize( meta.lore().get(0) );
        }catch (Throwable e) {
            return null;
        }
    }

    private static class DataContainer {

        public HashMap<String, Object> nbt = new HashMap<>();

        public void add(String key, Object value) {
            this.nbt.put(key, value);
        }
        public void remove(String key) {
            this.nbt.remove(key);
        }
        public Object get(String key) {
            return this.nbt.get(key);
        }
    }
}