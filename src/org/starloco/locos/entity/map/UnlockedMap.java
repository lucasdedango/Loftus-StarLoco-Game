package org.starloco.locos.entity.map;

/**
 * Simple record linking a player to a map he has unlocked.
 */
public class UnlockedMap {

    private final int playerId;
    private final int mapId;

    public UnlockedMap(int playerId, int mapId) {
        this.playerId = playerId;
        this.mapId = mapId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getMapId() {
        return mapId;
    }
}