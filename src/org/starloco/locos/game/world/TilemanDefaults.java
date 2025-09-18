package org.starloco.locos.game.world;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class TilemanDefaults {

    private static volatile Set<Integer> defaultUnlockedMaps = Collections.emptySet();

    private TilemanDefaults() {
        throw new IllegalStateException("Utility class");
    }

    public static void initialize(Collection<Integer> mapIds) {
        Set<Integer> mapSet = new HashSet<>();
        if (mapIds != null) {
            for (Integer mapId : mapIds) {
                if (mapId != null) {
                    mapSet.add(mapId);
                }
            }
        }
        defaultUnlockedMaps = Collections.unmodifiableSet(mapSet);
    }

    public static boolean isDefaultUnlockedMap(int mapId) {
        return defaultUnlockedMaps.contains(mapId);
    }

    public static Set<Integer> getDefaultUnlockedMaps() {
        return defaultUnlockedMaps;
    }
}
