package org.starloco.locos.database.data.login;

import com.mysql.jdbc.Statement;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang.NotImplementedException;
import org.starloco.locos.client.Account;
import org.starloco.locos.client.Player;
import org.starloco.locos.database.DatabaseManager;

import org.starloco.locos.database.data.FunctionDAO;
import org.starloco.locos.database.data.game.GuildMemberData;
import org.starloco.locos.database.data.game.QuestProgressData;
import org.starloco.locos.database.data.game.UnlockedMapData;
import org.starloco.locos.game.world.World;
import org.starloco.locos.kernel.Config;
import org.starloco.locos.kernel.Constant;
import org.starloco.locos.kernel.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public class PlayerData extends FunctionDAO<Player> {

    private static final int[] DEFAULT_UNLOCKED_MAPS = {10294, 10327, 10273, 10337, 10258, 10295, 10359, 10360, 10361, 10362, 10363, 10364, 325, 625, 979, 1113, 1298, 1443, 1549, 1687, 1767, 1768, 1769, 1770, 1771, 1772, 1773, 1774, 1780, 1783, 1784, 1785, 1786, 1787, 1788, 1789, 1790, 1795, 1836, 1842, 2073, 2074, 2075, 2076, 2077, 2079, 2080, 2081, 2082, 2083, 2084, 2085, 2086, 2087, 2088, 2089, 2090, 2091, 2094, 2096, 2097, 2098, 2099, 2100, 2102, 2104, 2105, 2110, 2111, 2112, 2113, 2114, 2115, 2116, 2117, 2118, 2119, 2163, 2985, 4786, 4919, 6684, 6691, 6692, 6693, 6709, 6710, 6720, 6721, 6722, 6723, 6811, 6814, 6815, 6816, 6817, 6818, 6819, 6820, 6821, 6822, 6823, 6831, 6832, 6833, 6834, 6835, 6836, 6840, 6902, 6903, 6954, 7453, 7519, 7520, 7521, 7522, 7523, 7524, 7525, 7526, 7532, 7537, 7858, 8203, 8282, 8290, 8291, 8293, 8294, 8295, 8296, 8303, 8304, 8305, 8306, 8310, 8311, 8313, 8314, 8317, 8321, 8322, 8326, 8327, 8328, 8329, 8330, 8331, 8349, 8351, 8357, 8497, 8541, 8542, 8543, 8544, 8545, 8546, 8547, 8548, 8549, 8709, 8710, 8711, 8713, 8714, 8715, 8716, 8718, 8719, 8721, 8722, 8723, 8724, 8950, 8952, 8953, 8954, 8963, 8964, 8965, 8966, 8967, 8968, 8969, 8977, 8978, 8979, 8980, 8981, 8982, 8983, 8984, 9121, 9122, 9125, 9126, 9521, 9522, 9523, 9524, 9525, 9528, 9529, 9530, 9531, 9532, 9533, 9534, 9535, 9538, 9578, 9579, 9580, 9581, 9582, 9583, 9584, 9585, 9586, 9587, 9588, 9590, 9591, 9592, 9593, 9594, 9595, 9596, 9597, 9598, 9599, 9600, 9601, 9602, 9603, 9638, 9645, 9646, 9647, 9648, 9649, 9650, 9651, 9652, 9653, 9654, 9655, 9656, 9657, 9658, 9659, 9716, 9717, 9723, 9724, 9750, 9751, 9752, 9753, 9755, 9757, 9758, 9759, 9760, 9765, 9766, 9767, 9768, 9769, 9770, 9771, 9772, 9773, 9774, 9775, 9778, 9779, 9780, 9781, 9782, 9784, 9864, 9877, 9879, 9880, 9946, 9981, 9989, 9993, 10019, 10020, 10021, 10022, 10023, 10024, 10025, 10098, 10099, 10100, 10101, 10102, 10103, 10106, 10107, 10109, 10125, 10141, 10142, 10143, 10144, 10145, 10146, 10147, 10148, 10149, 10153, 10154, 10156, 10157, 10158, 10159, 10160, 10161, 10162, 10163, 10164, 10165, 10176, 10191, 10192, 10193, 10194, 10195, 10196, 10197, 10198, 10199, 10200, 10201, 10202, 10203, 10204, 10205, 10210, 10211, 10212, 10213, 10234, 10235, 10256, 10257, 10360, 10361, 10362, 10363, 10364, 10417, 10418, 10419, 10509, 10693, 10700, 10807, 10808, 10809, 10810, 10811, 10812, 10814, 10815, 11066, 11067, 11068, 11069, 11071, 11072, 11074, 11075, 11076, 11077, 11078, 11079, 11095, 11101, 11108, 11111, 11113, 11115, 11121, 11127, 11128, 11133, 11136, 11234, 11235, 11259, 11262, 11263, 11264, 11265, 11266, 11267, 11517, 11878, 11879, 11880, 11881, 11882, 11883, 11884, 11885, 11886, 11887, 11888, 11889, 11890, 11891, 11892, 11927, 11931, 11932, 11933, 11936, 11937, 11938, 11939, 11943, 11944, 11945, 11946, 11947, 11948, 11949, 11950, 11951, 11953, 11954, 11955, 11956, 11957, 11958, 11959, 11960, 11961, 11965, 11967, 11968, 11969, 11970, 11971, 11972, 11976, 18545, 20000, 20001, 20002, 20003, 21000, 21001, 21002, 21003, 22001, 22002, 22003, 25006, 25012, 25019, 26000, 26001, 26002, 26003, 26004, 26101, 26102, 26103, 28005, 28026, 28027, 28028, 31000, 31001, 31002, 31003, 31004, 31005, 31006, 31007, 31008, 31009, 31010, 31011, 31012, 31013, 31014, 31015, 31016, 31017, 31018, 31019, 31020, 31021, 31022, 31023, 31024, 31025, 31026, 31027, 31028, 31029, 31030, 31050, 31051, 31052};

    public PlayerData(HikariDataSource dataSource) {
        super(dataSource, "world_players");
    }

    @Override
    public void loadFully() {
        throw new NotImplementedException("cannot load all players at once");
    }

    private Player buildFromResultSet(ResultSet result) throws SQLException {
        Player player = new Player(result.getInt("id"), result.getString("name"), result.getInt("groupe"), result.getInt("sexe"),
                result.getInt("class"), result.getInt("color1"), result.getInt("color2"), result.getInt("color3"), result.getLong("kamas"),
                result.getInt("spellboost"), result.getInt("capital"), result.getInt("energy"), result.getInt("level"), result.getLong("xp"),
                result.getInt("size"), result.getInt("gfx"), result.getByte("alignement"), result.getInt("account"), this.getStats(result),
                result.getByte("seeFriend"), result.getByte("seeAlign"), result.getByte("seeSeller"), result.getString("canaux"),
                result.getShort("map"), result.getInt("cell"), result.getString("objets"), result.getString("storeObjets"),
                result.getInt("pdvper"), result.getString("spells"), result.getString("savepos"), result.getString("jobs"),
                result.getInt("mountxpgive"), result.getInt("mount"), result.getInt("honor"), result.getInt("deshonor"),
                result.getInt("alvl"), result.getString("zaaps"), result.getByte("title"), result.getInt("wife"),
                result.getString("morphMode"), result.getString("allTitle"), result.getString("emotes"), result.getLong("prison"),
                false, result.getString("parcho"), result.getLong("timeDeblo"), result.getBoolean("noall"),
                result.getString("deadInformation"), result.getByte("deathCount"), result.getLong("totalKills"));
        player.setTilemanCredits(result.getInt("tileman_credits"));
        player.setTilemanCreditXp(result.getLong("tileman_credit_xp"));
        return player;
    }

    @Override
    public Player load(int id) {
        Player oldPlayer = World.world.getPlayer(id);
        try {
            Player player = getData("SELECT * FROM " + getTableName() + " WHERE id = '" + id + "' AND server = " + Config.gameServerId + ";", result -> {
                if(!result.next())return null;
                return buildFromResultSet(result);
            });
            if(oldPlayer != null)
                player.setLastFightForEndFightAction(oldPlayer.getLastFight());

            player.VerifAndChangeItemPlace();

            player.setUnlockedMaps(DatabaseManager.get(UnlockedMapData.class).getMaps(player.getId()));

            DatabaseManager.get(QuestProgressData.class).load(player.getId());

            // Find player's guild
            World.world.getGuilds().values().stream().map(g -> g.getMember(id)).findFirst().ifPresent(player::setGuildMember);

            // Add to world
            World.world.addPlayer(player);

            return player;
        } catch (SQLException e) {
            super.sendError(e);
            Main.stop("unknown");
        }
        return null;
    }

    @Override
    public boolean insert(Player entity) {
        PreparedStatement statement = null;
        boolean ok = true;
        try {
            Connection connection = this.getConnection();
            String sql = "INSERT INTO " + getTableName() + "(`name`, `sexe`, `class`, `color1`, `color2`, `color3`, `kamas`, `spellboost`, `capital`, `energy`, `level`, `xp`, `size`, `gfx`, `account`, `cell`, `map`, `spells`, `objets`, `storeObjets`, `morphMode`, `tileman_credits`, `tileman_credit_xp`, `server`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'','','0',?,?,?,?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getSexe());
            statement.setInt(3, entity.getClasse());
            statement.setInt(4, entity.getColor1());
            statement.setInt(5, entity.getColor2());
            statement.setInt(6, entity.getColor3());
            statement.setLong(7, entity.getKamas());
            statement.setInt(8, entity.get_spellPts());
            statement.setInt(9, entity.getCapital());
            statement.setInt(10, entity.getEnergy());
            statement.setInt(11, entity.getLevel());
            statement.setLong(12, entity.getExp());
            statement.setInt(13, entity.get_size());
            statement.setInt(14, entity.getGfxId());
            statement.setInt(15, entity.getAccID());
            statement.setInt(16, entity.getCurCell().getId());
            statement.setInt(17, entity.getCurMap().getId());
            statement.setString(18, entity.encodeSpellsToDB());
            statement.setInt(19, entity.getTilemanCredits());
            statement.setLong(20, entity.getTilemanCreditXp());
            statement.setInt(21, Config.gameServerId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows != 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                        for (int mapId : DEFAULT_UNLOCKED_MAPS) {
                            entity.unlockMap(mapId);
                        }
                    } else {
                        ok = false;
                    }
                }
            } else {
                ok = false;
            }
        } catch (SQLException e) {
            super.sendError(e);
            ok = false;
        } finally {
            close(statement);
        }
        return ok;
    }

    @Override
    public void delete(Player entity) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("DELETE FROM " + getTableName() + " WHERE id = ?");
            p.setInt(1, entity.getId());
            execute(p);

            ObjectData dao = (ObjectData) DatabaseManager.get(ObjectData.class);
            if (!entity.getItemsIDSplitByChar(",").equals(""))
                for(String id : entity.getItemsIDSplitByChar(",").split(","))
                    dao.delete(World.world.getGameObject(Integer.parseInt(id)));
            if (!entity.getStoreItemsIDSplitByChar(",").equals(""))
                for(String id : entity.getStoreItemsIDSplitByChar(",").split(","))
                    dao.delete(World.world.getGameObject(Integer.parseInt(id)));
            if (entity.getMount() != null)
                ((MountData) DatabaseManager.get(MountData.class)).update(entity.getMount());
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    @Override
    public void update(Player entity) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `kamas`= ?, `spellboost`= ?, `capital`= ?, `energy`= ?, `level`= ?, `xp`= ?, `size` = ?, `gfx`= ?, `alignement`= ?, `honor`= ?, `deshonor`= ?, `alvl`= ?, `vitalite`= ?, `force`= ?, `sagesse`= ?, `intelligence`= ?, `chance`= ?, `agilite`= ?, `seeFriend`= ?, `seeAlign`= ?, `seeSeller`= ?, `canaux`= ?, `map`= ?, `cell`= ?, `pdvper`= ?, `spells`= ?, `objets`= ?, `storeObjets`= ?, `savepos`= ?, `zaaps`= ?, `jobs`= ?, `mountxpgive`= ?, `mount`= ?, `title`= ?, `wife`= ?, `morphMode`= ?, `allTitle` = ?, `emotes` = ?, `prison` = ?, `parcho` = ?, `timeDeblo` = ?, `noall` = ?, `deadInformation` = ?, `deathCount` = ?, `totalKills` = ?, `tileman_credits` = ?, `tileman_credit_xp` = ? WHERE `id` = ? LIMIT 1");p.setLong(1, entity.getKamas());
            p.setInt(2, entity.get_spellPts());
            p.setInt(3, entity.getCapital());
            p.setInt(4, entity.getEnergy());
            p.setInt(5, entity.getLevel());
            p.setLong(6, entity.getExp());
            p.setInt(7, entity.get_size());
            p.setInt(8, entity.getGfxId());
            p.setInt(9, entity.getAlignment());
            p.setInt(10, entity.get_honor());
            p.setInt(11, entity.getDeshonor());
            p.setInt(12, entity.getALvl());
            p.setInt(13, entity.stats.getEffect(Constant.STATS_ADD_VITA));
            p.setInt(14, entity.stats.getEffect(Constant.STATS_ADD_FORC));
            p.setInt(15, entity.stats.getEffect(Constant.STATS_ADD_SAGE));
            p.setInt(16, entity.stats.getEffect(Constant.STATS_ADD_INTE));
            p.setInt(17, entity.stats.getEffect(Constant.STATS_ADD_CHAN));
            p.setInt(18, entity.stats.getEffect(Constant.STATS_ADD_AGIL));
            p.setInt(19, (entity.is_showFriendConnection() ? 1 : 0));
            p.setInt(20, (entity.is_showWings() ? 1 : 0));
            p.setInt(21, (entity.isShowSeller() ? 1 : 0));
            p.setString(22, entity.get_canaux());
            if (entity.getCurMap() != null) p.setInt(23, entity.getCurMap().getId());
            else p.setInt(23, 7411);
            if (entity.getCurCell() != null) p.setInt(24, entity.getCurCell().getId());
            else p.setInt(24, 311);
            p.setInt(25, entity.get_pdvper());
            p.setString(26, entity.encodeSpellsToDB());
            p.setString(27, entity.parseObjetsToDB());
            p.setString(28, entity.parseStoreItemstoBD());
            p.setString(29, entity.getSavePosition().toString(","));
            p.setString(30, entity.parseZaaps());
            p.setString(31, entity.parseJobData());
            p.setInt(32, entity.getMountXpGive());
            p.setInt(33, (entity.getMount() != null ? entity.getMount().getId() : -1));
            p.setByte(34, (entity.getCurrentTitle()));
            p.setInt(35, entity.getWife());
            p.setString(36, (entity.getMorphMode() ? 1 : 0) + ";" + entity.getMorphId());
            p.setString(37, entity.getAllTitle());
            p.setString(38, entity.parseEmoteToDB());
            p.setLong(39, (entity.isInEnnemyFaction ? entity.enteredOnEnnemyFaction : 0));
            p.setString(40, entity.parseStatsParcho());
            p.setLong(41, entity.getTimeTaverne());
            p.setBoolean(42, entity.noall);
            p.setString(43, entity.getDeathInformation());
            p.setByte(44, entity.getDeathCount());
            p.setLong(45, entity.getTotalKills());
            p.setInt(46, entity.getTilemanCredits());
            p.setLong(47, entity.getTilemanCreditXp());
            p.setInt(48, entity.getId());
            execute(p);
            if (entity.getGuildMember() != null)
                ((GuildMemberData) DatabaseManager.get(GuildMemberData.class)).update(entity);
            if (entity.getMount() != null)
                ((MountData) DatabaseManager.get(MountData.class)).update(entity.getMount());
            entity.saveQuestProgress();
        } catch (Exception e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    @Override
    public Class<?> getReferencedClass() {
        return PlayerData.class;
    }

    private HashMap<Integer, Integer> getStats(ResultSet result) throws SQLException {
        HashMap<Integer, Integer> stats = new HashMap<>();
        stats.put(Constant.STATS_ADD_VITA, result.getInt("vitalite"));
        stats.put(Constant.STATS_ADD_FORC, result.getInt("force"));
        stats.put(Constant.STATS_ADD_SAGE, result.getInt("sagesse"));
        stats.put(Constant.STATS_ADD_INTE, result.getInt("intelligence"));
        stats.put(Constant.STATS_ADD_CHAN, result.getInt("chance"));
        stats.put(Constant.STATS_ADD_AGIL, result.getInt("agilite"));
        return stats;
    }

    public void loadByAccountId(int id) {
        try {
            Account account = World.world.ensureAccountLoaded(id);
            if (account != null && account.getPlayers() != null)
                    account.getPlayers().values().stream().filter(Objects::nonNull).forEach(World.world::verifyClone);
        } catch (Exception e) {
            super.sendError(e);
        }

        try {
            getData("SELECT * FROM " + getTableName() + " WHERE account = '" + id + "' AND server = '"+Config.gameServerId+"'", result -> {
                while (result.next()) {
                    Player p = World.world.getPlayer(result.getInt("id"));
                    if (p != null) {
                        if (p.getFight() != null) {
                            continue;
                        }
                    }

                    HashMap<Integer, Integer> stats = new HashMap<>();
                    stats.put(Constant.STATS_ADD_VITA, result.getInt("vitalite"));
                    stats.put(Constant.STATS_ADD_FORC, result.getInt("force"));
                    stats.put(Constant.STATS_ADD_SAGE, result.getInt("sagesse"));
                    stats.put(Constant.STATS_ADD_INTE, result.getInt("intelligence"));
                    stats.put(Constant.STATS_ADD_CHAN, result.getInt("chance"));
                    stats.put(Constant.STATS_ADD_AGIL, result.getInt("agilite"));

                    Player player = new Player(result.getInt("id"), result.getString("name"), result.getInt("groupe"), result.getInt("sexe"), result.getInt("class"), result.getInt("color1"), result.getInt("color2"), result.getInt("color3"), result.getLong("kamas"), result.getInt("spellboost"), result.getInt("capital"), result.getInt("energy"), result.getInt("level"), result.getLong("xp"), result.getInt("size"), result.getInt("gfx"), result.getByte("alignement"), result.getInt("account"), stats, result.getByte("seeFriend"), result.getByte("seeAlign"), result.getByte("seeSeller"), result.getString("canaux"), result.getShort("map"), result.getInt("cell"), result.getString("objets"), result.getString("storeObjets"), result.getInt("pdvper"), result.getString("spells"), result.getString("savepos"), result.getString("jobs"), result.getInt("mountxpgive"), result.getInt("mount"), result.getInt("honor"), result.getInt("deshonor"), result.getInt("alvl"), result.getString("zaaps"), result.getByte("title"), result.getInt("wife"), result.getString("morphMode"), result.getString("allTitle"), result.getString("emotes"), result.getLong("prison"), false, result.getString("parcho"), result.getLong("timeDeblo"), result.getBoolean("noall"), result.getString("deadInformation"), result.getByte("deathCount"), result.getLong("totalKills"));

                    player.setTilemanCredits(result.getInt("tileman_credits"));
                    player.setTilemanCreditXp(result.getLong("tileman_credit_xp"));
                    player.setUnlockedMaps(DatabaseManager.get(UnlockedMapData.class).getMaps(player.getId()));

                    if(p != null)
                        player.setLastFightForEndFightAction(p.getLastFight());
                    player.VerifAndChangeItemPlace();

                    player.setUnlockedMaps(DatabaseManager.get(UnlockedMapData.class).getMaps(player.getId()));

                    DatabaseManager.get(QuestProgressData.class).load(id);
                    // Find player's guild
                    World.world.getGuilds().values().stream().map(g -> g.getMember(player.getId())).filter(Objects::nonNull).findFirst().ifPresent(player::setGuildMember);

                    World.world.addPlayer(player);
                }
            });
        } catch (SQLException e) {
            super.sendError(e);
            Main.stop("unknown");
        }
    }

    public String loadTitles(int guid) {
        try {
            return getData("SELECT * FROM " + getTableName() + " WHERE id = '" + guid + "';", result -> {
                if (!result.next()) return "";
                return result.getString("allTitle");
            });
        } catch (SQLException e) {
            super.sendError(e);
        }
        return "";
    }

    public void updateInfos(Player perso) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `name` = ?, `sexe`=?, `class`= ?, `color1` = ?, `color2` = ?, `color3` = ? WHERE `id`= ?;");
            p.setString(1, perso.getName());
            p.setInt(2, perso.getSexe());
            p.setInt(3, perso.getClasse());
            p.setInt(4, perso.getColor1());
            p.setInt(5, perso.getColor2());
            p.setInt(6, perso.getColor3());
            p.setInt(7, perso.getId());
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public void updateGroupe(int group, String name) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `groupe` = ? WHERE `name` = ?;");

            p.setInt(1, group);
            p.setString(2, name);
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public void updateGroupe(Player perso) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `groupe` = ? WHERE `id`= ?");
            int id = (perso.getGroup() != null) ? perso.getGroup().getId() : -1;
            p.setInt(1, id);
            p.setInt(2, perso.getId());
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public void updateTimeTaverne(Player player) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `timeDeblo` = ? WHERE `id` = ?");
            p.setLong(1, player.getTimeTaverne());
            p.setInt(2, player.getId());
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public void updateTitles(int guid, String title) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `allTitle` = ? WHERE `id` = ?");
            p.setString(1, title);
            p.setInt(2, guid);
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public void updateLogged(int guid, int logged) {
        PreparedStatement p = null;
        try {
            p = getPreparedStatement("UPDATE " + getTableName() + " SET `logged` = ? WHERE `id` = ?");
            p.setInt(1, logged);
            p.setInt(2, guid);
            execute(p);
        } catch (SQLException e) {
            super.sendError(e);
        } finally {
            close(p);
        }
    }

    public boolean exist(String name) {
        try {
            return getData("SELECT COUNT(*) AS exist FROM " + getTableName() + " WHERE name LIKE '" + name + "';", result -> {
                if(!result.next()) return false;
                return result.getInt("exist")>0;
            });
        } catch (SQLException e) {
            super.sendError(e);
        }
        return false;
    }

    public void reloadGroup(Player p) {
        try {
            getData("SELECT groupe FROM " + getTableName() + " WHERE id = '" + p.getId() + "'", result -> {
                if (result.next()) {
                    int group = result.getInt("groupe");
                    p.setGroupe(group, false);
                }
            });
        } catch (SQLException e) {
            super.sendError(e);
        }
    }

    public int canRevive(Player player) {
        try {
            return getData("SELECT id, revive FROM " + getTableName() + " WHERE `id` = '" + player.getId() + "';", result -> {
                if(!result.next()) return 0;
                return result.getInt("revive");
            });
        } catch (SQLException e) {
            super.sendError(e);
        }
        return 0;
    }

    public void setRevive(Player player) {
        try {
            PreparedStatement p = getPreparedStatement("UPDATE " + getTableName() + " SET `revive` = 0 WHERE `id` = '" + player.getId() + "';");
            execute(p);
            close(p);
        } catch (SQLException e) {
            super.sendError(e);
        }
    }
}
