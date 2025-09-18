package org.starloco.locos.script.proxy;

import org.classdump.luna.impl.DefaultUserdata;
import org.classdump.luna.impl.ImmutableTable;
import org.classdump.luna.lib.ArgumentIterator;
import org.starloco.locos.client.Player;
import org.starloco.locos.entity.map.Trunk;
import org.starloco.locos.script.types.MetaTables;

public class STrunk extends DefaultUserdata<Trunk> {
    private static final ImmutableTable META_TABLE = MetaTables.MetaTable(MetaTables.ReflectIndexTable(STrunk.class));

    public STrunk(Trunk userValue) {
        super(META_TABLE, userValue);
    }

    @SuppressWarnings("unused")
    private static int id(Trunk trunk) {
        return trunk.getId();
    }

    @SuppressWarnings("unused")
    private static int houseId(Trunk trunk) {
        return trunk.getHouseId();
    }

    @SuppressWarnings("unused")
    private static int mapId(Trunk trunk) {
        return trunk.getMapId();
    }

    @SuppressWarnings("unused")
    private static int cellId(Trunk trunk) {
        return trunk.getCellId();
    }

    @SuppressWarnings("unused")
    private static int ownerId(Trunk trunk) {
        return trunk.getOwnerId();
    }

    @SuppressWarnings("unused")
    private static long kamas(Trunk trunk) {
        return trunk.getKamas();
    }

    @SuppressWarnings("unused")
    private static String key(Trunk trunk) {
        return trunk.getKey();
    }

    @SuppressWarnings("unused")
    private static void enter(Trunk trunk, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        trunk.enter(player);
    }

    @SuppressWarnings("unused")
    private static void lock(Trunk trunk, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        trunk.Lock(player);
    }
}
