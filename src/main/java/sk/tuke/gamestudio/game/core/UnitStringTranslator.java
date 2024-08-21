package sk.tuke.gamestudio.game.core;

import jakarta.persistence.Transient;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UnitStringTranslator {

    public String convertMapToString(Map<String, Integer> map) {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining("-"));
    }

    public Map<String, Integer> convertStringToMap(String str) {
        return Arrays.stream(str.split("-"))
                .map(entry -> entry.split(":"))
                .collect(Collectors.toMap(entry -> entry[0], entry -> Integer.valueOf(entry[1])));
    }
}
