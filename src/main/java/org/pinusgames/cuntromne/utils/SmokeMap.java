package org.pinusgames.cuntromne.utils;

import java.util.ArrayList;
import java.util.List;

public class SmokeMap {
    public List<SmokeVector> keys;
    public List<SmokeBlock> values;

    public SmokeMap() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void put(SmokeVector key, SmokeBlock value) {
        int index = contains(key);
        if(index == -1) {
            this.keys.add(key);
            this.values.add(value);
            return;
        }
        this.keys.set(index, key);
        this.values.set(index, value);
    }

    public SmokeBlock get(SmokeVector key) {
        int index = contains(key);
        return values.get(index);
    }

    private int contains(SmokeVector key) {
        int i = 0;
        for(SmokeVector vector : this.keys) {
            if(vector.isEqual( key )) return i;
            i++;
        }
        return -1;
    }

    public boolean isContains(SmokeVector key) {
        for(SmokeVector vector : this.keys) {
            if(vector.isEqual( key )) return true;
        }
        return false;
    }

}
