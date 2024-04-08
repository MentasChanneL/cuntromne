package org.pinusgames.cuntromne;

import java.util.HashMap;

public class TextFormatter {
    private final static HashMap<String, String> replacer = new HashMap<>();

    public static void init() {
        replacer.put(":klass:", "ಧ");
        replacer.put(":sosi:", "ನ");
    }

    public static String format(String text) {
        String result = text;
        for(String key : TextFormatter.replacer.keySet()) {
            if(text.contains( key )) {
                String replace = TextFormatter.replacer.get(key);
                result = result.replaceAll(key, replace);
            }
        }
        return result;
    }

}
