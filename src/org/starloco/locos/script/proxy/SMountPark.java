package org.starloco.locos.script.proxy;

import org.classdump.luna.impl.DefaultUserdata;
import org.classdump.luna.impl.ImmutableTable;
import org.classdump.luna.lib.ArgumentIterator;
import org.starloco.locos.client.Player;
import org.starloco.locos.entity.map.MountPark;
import org.starloco.locos.guild.Guild;
import org.starloco.locos.script.types.MetaTables;

import java.util.Optional;

public class SMountPark extends DefaultUserdata<MountPark> {
    private static final ImmutableTable META_TABLE = MetaTables.MetaTable(MetaTables.ReflectIndexTable(SMountPark.class));

    public SMountPark(MountPark userValue) {
        super(META_TABLE, userValue);
    }

    @SuppressWarnings("unused")
    private static int mapId(MountPark park) {
        return park.getMap();
    }

    @SuppressWarnings("unused")
    private static int cellId(MountPark park) {
        return park.getCell();
    }

    @SuppressWarnings("unused")
    private static int doorCellId(MountPark park) {
        return park.getDoor();
    }

    @SuppressWarnings("unused")
    private static int ownerId(MountPark park) {
        return park.getOwner();
    }

    @SuppressWarnings("unused")
    private static int guildId(MountPark park) {
        return Optional.ofNullable(park.getGuild()).map(Guild::getId).orElse(-1);
    }

    @SuppressWarnings("unused")
    private static int price(MountPark park) {
        return park.getPrice();
    }

    @SuppressWarnings("unused")
    private static int size(MountPark park) {
        return park.getSize();
    }

    @SuppressWarnings("unused")
    private static int maxObject(MountPark park) {
        return park.getMaxObject();
    }

    @SuppressWarnings("unused")
    private static void open(MountPark park, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        park.open(player);
    }

    @SuppressWarnings("unused")
    private static void promptBuy(MountPark park, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        park.promptBuy(player);
    }

    @SuppressWarnings("unused")
    private static void promptSell(MountPark park, ArgumentIterator args) {
        Player player = args.nextUserdata("SPlayer", SPlayer.class).getUserValue();
        park.promptSell(player);
    }
}
