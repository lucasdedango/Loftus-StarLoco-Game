package org.starloco.locos.database.data.game;

import com.zaxxer.hikari.HikariDataSource;
import org.starloco.locos.database.data.FunctionDAO;
import org.starloco.locos.entity.map.UnlockedMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * DAO managing unlocked maps for players.
 */
public class UnlockedMapData extends FunctionDAO<UnlockedMap> {

    public UnlockedMapData(HikariDataSource dataSource) {
        super(dataSource, "player_unlocked_maps");
    }

    @Override
    public void loadFully() {
        // nothing to preload
    }

    @Override
    public UnlockedMap load(int id) {
        // not used
        return null;
    }

    @Override
    public boolean insert(UnlockedMap entity) {
        add(entity.getPlayerId(), entity.getMapId());
        return true;
    }

    @Override
    public void delete(UnlockedMap entity) {
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement("DELETE FROM " + getTableName() + " WHERE `player_id` = ? AND `map_id` = ?;")) {
            p.setInt(1, entity.getPlayerId());
            p.setInt(2, entity.getMapId());
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        }
    }

    @Override
    public void update(UnlockedMap entity) {
        // same as insert
        insert(entity);
    }

    @Override
    public Class<?> getReferencedClass() {
        return UnlockedMapData.class;
    }

    /**
     * Add a map to the unlocked list for the given player.
     */
    public void add(int playerId, int mapId) {
        try (Connection c = getConnection();
             PreparedStatement p = c.prepareStatement("REPLACE INTO " + getTableName() + " (`player_id`, `map_id`) VALUES (?, ?);")) {
            p.setInt(1, playerId);
            p.setInt(2, mapId);
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        }
    }

    /**
     * Load all unlocked maps for the given player.
     */
    public Set<Integer> getMaps(int playerId) {
        Set<Integer> maps = new HashSet<>();
        try {
            getData("SELECT `map_id` FROM " + getTableName() + " WHERE `player_id` = " + playerId + ";", result -> {
                while (result.next()) {
                    maps.add(result.getInt("map_id"));
                }
            });
        } catch (SQLException e) {
            super.sendError(e);
        }
        return maps;
    }
}