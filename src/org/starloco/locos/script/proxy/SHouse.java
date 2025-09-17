package org.starloco.locos.script.proxy;

import org.classdump.luna.impl.DefaultUserdata;
import org.classdump.luna.impl.ImmutableTable;
import org.classdump.luna.lib.ArgumentIterator;
import org.starloco.locos.client.Player;
import org.starloco.locos.entity.map.House;
import org.starloco.locos.script.types.MetaTables;

public class SHouse extends DefaultUserdata<House> {
    private static final ImmutableTable META_TABLE = MetaTables.MetaTable(MetaTables.ReflectIndexTable(SHouse.class));

    public SHouse(House userValue) {
        super(META_TABLE, userValue);
    }

    @SuppressWarnings("unused")
    private static int id(House house) {
        return house.getId();
    }

    @SuppressWarnings("unused")
    private static int mapId(House house) {
        return house.getMapId();
    }

    @SuppressWarnings("unused")
    private static int cellId(House house) {
        return house.getCellId();
    }

    @SuppressWarnings("unused")
    private static int ownerId(House house) {
        return house.getOwnerId();
    }

    @SuppressWarnings("unused")
    private static int salePrice(House house) {
        return house.getSale();
    }

    @SuppressWarnings("unused")
    private static int guildId(House house) {
        return house.getGuildId();
    }

    @SuppressWarnings("unused")
    private static int houseMapId(House house) {
        return house.getHouseMapId();
    }

    @SuppressWarnings("unused")
    private static int houseCellId(House house) {
        return house.getHouseCellId();
    }

    @SuppressWarnings("unused")
    private static void enter(House house, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        house.enter(player);
    }

    @SuppressWarnings("unused")
    private static void lock(House house, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        house.lock(player);
    }

    @SuppressWarnings("unused")
    private static void buy(House house, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        house.buyIt(player);
    }

    @SuppressWarnings("unused")
    private static void sell(House house, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        house.sellIt(player);
    }
}
