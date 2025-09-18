package org.starloco.locos.database.data.game;

import com.zaxxer.hikari.HikariDataSource;
import org.starloco.locos.database.data.FunctionDAO;
import org.starloco.locos.game.world.TilemanDefaults;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class TilemanDefaultMapData extends FunctionDAO<Integer> {

    public TilemanDefaultMapData(HikariDataSource dataSource) {
        super(dataSource, "tileman_default_maps");
    }

    @Override
    public void loadFully() {
        reload();
    }

    public void reload() {
        TilemanDefaults.initialize(loadMapIds());
    }

    public Set<Integer> loadMapIds() {
        Set<Integer> mapIds = new HashSet<>();
        try {
            getData("SELECT `map_id` FROM " + getTableName() + ";", result -> {
                while (result.next()) {
                    mapIds.add(result.getInt("map_id"));
                }
            });
        } catch (SQLException e) {
            sendError(e);
        }
        return mapIds;
    }

    @Override
    public Integer load(int id) {
        return null;
    }

    @Override
    public boolean insert(Integer mapId) {
        if (mapId == null) {
            return false;
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "REPLACE INTO " + getTableName() + " (`map_id`) VALUES (?);")) {
            statement.setInt(1, mapId);
            execute(statement);
            reload();
            return true;
        } catch (SQLException e) {
            sendError(e);
        }
        return false;
    }

    @Override
    public void delete(Integer mapId) {
        if (mapId == null) {
            return;
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM " + getTableName() + " WHERE `map_id` = ?;")) {
            statement.setInt(1, mapId);
            execute(statement);
            reload();
        } catch (SQLException e) {
            sendError(e);
        }
    }

    @Override
    public void update(Integer mapId) {
        insert(mapId);
    }

    @Override
    public Class<?> getReferencedClass() {
        return TilemanDefaultMapData.class;
    }
}
