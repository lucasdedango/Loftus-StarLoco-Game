package org.starloco.locos.command;

import org.starloco.locos.auction.AuctionManager;
import org.starloco.locos.area.map.GameMap;
import org.starloco.locos.client.Player;
import org.starloco.locos.client.other.Party;
import org.starloco.locos.common.SocketManager;
import org.starloco.locos.event.EventManager;
import org.starloco.locos.game.action.ExchangeAction;
import org.starloco.locos.game.world.World;
import org.starloco.locos.database.DatabaseManager;
import org.starloco.locos.database.data.login.PlayerData;
import org.starloco.locos.kernel.Config;
import org.starloco.locos.kernel.Constant;
import org.starloco.locos.kernel.Logging;
import org.starloco.locos.object.GameObject;
import org.starloco.locos.util.TimerWaiter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CommandPlayer {

    public final static String canal = "Général";
    static boolean canalMute = false;

    public static boolean analyse(Player player, String msg) {
        msg = msg.replace("|", "");
        if (msg.charAt(0) == '.') {
            if(command(msg, "help")) {
                return commandHelp(player, msg);
            } else if (command(msg, "all") && msg.length() > 5) {
                return commandAll(player, msg);
            } else if (command(msg, "noall")) {
                return commandNoAll(player, msg);
            } else if (command(msg, "staff") || command(msg, "admin")) {
                return commandStaff(player, msg);
            } else if (command(msg, "deblo")) {
                return commandDeblo(player, msg);
            } else if (command(msg, "infos")) {
                return commandInfos(player, msg);
            }else if (command(msg, "master") || command(msg, "maitre") || command(msg, "maître") || command(msg, "maestro")) {
                return commandMaster(player, msg);
            } else if (command(msg, "window")) {
                return commandWindow(player);
            } else if (command(msg, "followmap")) {
                return commandFollowMap(player);
            } else if (command(msg, "pass")) {
                return commandPass(player, msg);
            } else  if (command(msg, "interval")) {
                return commandInterval(player, msg);
            } else if(command(msg, "start") || command(msg, "astrub")) {
                return commandAstrub(player, msg);
            } else if(command(msg, "walkfast")) {
                player.walkFast = !player.walkFast;
                return true;
            } else  if(command(msg, "vip")) {
                player.sendMessage(player.getLang().trans("command.commandplayer.vip"));
                return true;
            } else if(command(msg, "savepos")) {
                return commandStart(player, msg);
            } else  if (command(msg, "transfert")) {
                return commandTransfert(player, msg);
            }else if (command(msg, "banque")) {
                if (!player.getAccount().isSubscribeWithoutCondition()) {
                    player.sendMessage(player.getLang().trans("command.commandplayer.life.nosubscribe"));
                    return true;
                }
                if (player.isInPrison() || player.getFight() != null || player.getExchangeAction() != null)
                    return true;
                player.openBank();
                return true;
            }else if (command(msg, "groupe")) {
                if (player.isInPrison() || player.getFight() != null)
                    return true;
                final byte[] count = {0};
                World.world.getOnlinePlayers().stream().filter(p -> !p.equals(player) && p.getParty() == null && p.getAccount().getCurrentIp().equals(player.getAccount().getCurrentIp()) && p.getFight() == null && !p.isInPrison()).forEach(p -> {
                    if(count[0] <= 8) {
                        if (player.getParty() == null) {
                            Party party = new Party(player, p);
                            SocketManager.GAME_SEND_GROUP_CREATE(player.getGameClient(), party);
                            SocketManager.GAME_SEND_PL_PACKET(player.getGameClient(), party);
                            SocketManager.GAME_SEND_GROUP_CREATE(p.getGameClient(), party);
                            SocketManager.GAME_SEND_PL_PACKET(p.getGameClient(), party);
                            player.setParty(party);
                            p.setParty(party);
                            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(player.getGameClient(), party);
                            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(p.getGameClient(), party);
                        } else {
                            SocketManager.GAME_SEND_GROUP_CREATE(p.getGameClient(), player.getParty());
                            SocketManager.GAME_SEND_PL_PACKET(p.getGameClient(), player.getParty());
                            SocketManager.GAME_SEND_PM_ADD_PACKET_TO_GROUP(player.getParty(), p);
                            player.getParty().addPlayer(p);
                            p.setParty(player.getParty());
                            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(p.getGameClient(), player.getParty());
                            SocketManager.GAME_SEND_PR_PACKET(p);
                        }
                    }
                    count[0] = (byte) (count[0] + 1);
                });

                return true;
            } else if(command(msg, "event")) {
                return player.cantTP() || EventManager.getInstance().subscribe(player) == 1;
            } else if(Config.modeTileman && command(msg, "unlock")) {
                return commandUnlock(player, msg);
            } else if(Config.modeTileman && command(msg, "idunlock")) {
                return commandIdUnlock(player, msg);
            } else if(Config.modeTileman && command(msg, "tileman")) {
                return commandTileman(player, msg);
            } else if(Config.modeTileman && command(msg, "autounlock")) {
                return commandAutoUnlock(player, msg);
            } else if(command(msg, "auction")) {
                if(player.cantTP() || player.isDead() != 0 || player.isGhost() || player.isAway() || player.getFight() != null)
                    return true;
                AuctionManager.getInstance().onPlayerCommand(player, msg.split(" "));
                return true;
            } else {
                player.sendMessage(player.getLang().trans("command.commandplayer.default"));
                return true;
            }
        }
        return false;
    }

    private static boolean commandPass(Player player, String msg) {
        if(player.getParty() != null && player.getParty().getMaster() != null) {
            Party.MasterOption option = player.getParty().getOptionByPlayer(player);
            if(option != null) {
                option.togglePass();
                player.sendMessage(player.getLang().trans("command.commandplayer.pass"));
                return true;
            }
        } else {
            player.sendMessage(player.getLang().trans("command.commandplayer.pass.error"));
        }
        return false;
    }

    private static boolean commandInterval(Player player, String msg) {
        if(player.getParty() != null && player.getParty().getMaster() != null) {
            Party.MasterOption option = player.getParty().getOptionByPlayer(player);
            if(option != null) {
                try {
                    byte second = Byte.parseByte(msg.split(" ")[1]);
                    for(Party.MasterOption opt : player.getParty().getOptions()) {
                        if (opt != null && opt.getSecond() == second) {
                            player.sendMessage(player.getLang().trans("command.commandplayer.interval.error"));
                            return true;
                        }
                    }

                    option.setSecond(second);
                } catch(Exception ignored) {
                    player.sendMessage(player.getLang().trans("command.commandplayer.interval.error"));
                }

                if(option.getSecond() < 1) option.setSecond((byte)1);
                else if(option.getSecond() > 29) option.setSecond((byte)29);

                player.sendMessage(player.getLang().trans("command.commandplayer.interval"));
                return true;
            }
        } else {
            player.sendMessage(player.getLang().trans("command.commandplayer.interval.master"));
        }
        return false;
    }

    private static List<Integer> bannedItemJob = Arrays.asList(491, 493, 494, 495, 496);

    private static boolean commandTransfert(Player player, String msg) {
        if (player.isInPrison() || player.getFight() != null)
            return true;

        if(commandTransfertWithMaster(player, msg)) {
            return true;
        }

        if(player.getExchangeAction() == null || player.getExchangeAction().getType() != ExchangeAction.IN_BANK) {
            player.sendMessage(player.getLang().trans("command.commandplayer.transfer.noinbank"));
            return true;
        }
        String[] info = msg.split(" ");
        int map = player.getCurMap().getId();
        SocketManager.GAME_SEND_EV_PACKET(player.getGameClient());
        player.sendTypeMessage(player.getLang().trans("command.commandplayer.transfer.bank.title"),
                player.getLang().trans("command.commandplayer.transfer.waitting"));
        int count = 0;

        boolean bank = info.length >= 2 && info[1].equalsIgnoreCase("bank");

        for (GameObject object : new ArrayList<>(bank ? player.getAccount().getBank() : player.getItems().values())) {
            if(info.length == 2) {
                if (object == null || object.getTemplate() == null || !object.getTemplate().getStrTemplate().isEmpty())
                    continue;
                if (object.getTemplate().isAnEquipment(true, null))
                    continue;
                switch (object.getTemplate().getType()) {
                    case Constant.ITEM_TYPE_OBJET_VIVANT:
                    case Constant.ITEM_TYPE_PRISME:
                    case Constant.ITEM_TYPE_FILET_CAPTURE:
                    case Constant.ITEM_TYPE_CERTIF_MONTURE:
                    case Constant.ITEM_TYPE_OBJET_UTILISABLE:
                    case Constant.ITEM_TYPE_OBJET_ELEVAGE:
                    case Constant.ITEM_TYPE_CADEAUX:
                    case Constant.ITEM_TYPE_PARCHO_RECHERCHE:
                    case Constant.ITEM_TYPE_PIERRE_AME:
                    case Constant.ITEM_TYPE_BOUCLIER:
                    case Constant.ITEM_TYPE_SAC_DOS:
                    case Constant.ITEM_TYPE_OBJET_MISSION:
                    case Constant.ITEM_TYPE_BOISSON:
                    case Constant.ITEM_TYPE_CERTIFICAT_CHANIL:
                    case Constant.ITEM_TYPE_FEE_ARTIFICE:
                    case Constant.ITEM_TYPE_MAITRISE:
                    case Constant.ITEM_TYPE_POTION_SORT:
                    case Constant.ITEM_TYPE_POTION_METIER:
                    case Constant.ITEM_TYPE_POTION_OUBLIE:
                    case Constant.ITEM_TYPE_BONBON:
                    case Constant.ITEM_TYPE_PERSO_SUIVEUR:
                    case Constant.ITEM_TYPE_RP_BUFF:
                    case Constant.ITEM_TYPE_MALEDICTION:
                    case Constant.ITEM_TYPE_BENEDICTION:
                    case Constant.ITEM_TYPE_TRANSFORM:
                    case Constant.ITEM_TYPE_DOCUMENT:
                    case Constant.ITEM_TYPE_QUETES:
                        continue;
                }
            }
            if(object.getPosition() != -1)
                continue;
            switch (object.getTemplate().getType()) {
                case Constant.ITEM_TYPE_BONBON:
                case Constant.ITEM_TYPE_PERSO_SUIVEUR:
                case Constant.ITEM_TYPE_RP_BUFF:
                case Constant.ITEM_TYPE_MALEDICTION:
                case Constant.ITEM_TYPE_BENEDICTION:
                case Constant.ITEM_TYPE_TRANSFORM:
                case Constant.ITEM_TYPE_DOCUMENT:
                case Constant.ITEM_TYPE_QUETES:
                    continue;
            }
            if (bannedItemJob.contains(object.getTemplate().getId()))
                continue;
            count++;
            if(!bank) {
                player.addInBank(object.getGuid(), object.getQuantity(), false);
            } else {
                player.removeFromBank(object.getGuid(), object.getQuantity());
            }
        }

        player.sendTypeMessage(player.getLang().trans("command.commandplayer.transfer.bank.title"),
                player.getLang().trans("command.commandplayer.transfer.good", count));
        player.setExchangeAction(null);
        if(player.getCurMap().getId() == map)
            player.openBank();
        return true;
    }

    private static boolean commandTransfertWithMaster(Player player, String msg) {
        String[] info = msg.split(" ");
        if(info.length == 1 && player.getParty() != null && player.getParty().getMaster() != null && player.getParty().getMaster().getId() == player.getId()) {
            final List<GameObject> objects = new ArrayList<>();
            player.getParty().getPlayers().stream()
                    .filter(follower -> follower.getFight() == null && follower.getGameClient() != null && player.getParty().isWithTheMaster(follower, false, false))
                    .forEach(follower -> {
                        follower.getGameClient().clearAllPanels(null);
                        for(GameObject object : new ArrayList<>(follower.getItems().values())) {
                            if(object != null) {
                                if (object.getPosition() != -1 || object.getTemplate().isAnEquipment(true, null))
                                    continue;
                                switch (object.getTemplate().getType()) {
                                    case Constant.ITEM_TYPE_OBJET_VIVANT: case Constant.ITEM_TYPE_PRISME:
                                    case Constant.ITEM_TYPE_FILET_CAPTURE: case Constant.ITEM_TYPE_CERTIF_MONTURE:
                                    case Constant.ITEM_TYPE_OBJET_UTILISABLE: case Constant.ITEM_TYPE_OBJET_ELEVAGE:
                                    case Constant.ITEM_TYPE_CADEAUX: case Constant.ITEM_TYPE_PARCHO_RECHERCHE: case Constant.ITEM_TYPE_PIERRE_AME:
                                    case Constant.ITEM_TYPE_BOUCLIER: case Constant.ITEM_TYPE_SAC_DOS: case Constant.ITEM_TYPE_OBJET_MISSION:
                                    case Constant.ITEM_TYPE_BOISSON: case Constant.ITEM_TYPE_CERTIFICAT_CHANIL: case Constant.ITEM_TYPE_FEE_ARTIFICE:
                                    case Constant.ITEM_TYPE_MAITRISE: case Constant.ITEM_TYPE_POTION_SORT:
                                    case Constant.ITEM_TYPE_POTION_METIER: case Constant.ITEM_TYPE_POTION_OUBLIE:
                                    case Constant.ITEM_TYPE_BONBON: case Constant.ITEM_TYPE_PERSO_SUIVEUR:
                                    case Constant.ITEM_TYPE_RP_BUFF: case Constant.ITEM_TYPE_MALEDICTION:
                                    case Constant.ITEM_TYPE_BENEDICTION: case Constant.ITEM_TYPE_TRANSFORM:
                                    case Constant.ITEM_TYPE_DOCUMENT: case Constant.ITEM_TYPE_QUETES:
                                    case Constant.ITEM_TYPE_OUTIL:
                                        continue;
                                }
                                follower.removeItem(object.getGuid(), object.getQuantity(), true, false);
                                objects.add(object);
                            }
                        }
                    });
            TimerWaiter.addNext(() -> {
                for(GameObject object : objects) {
                    if(!player.addItem(object, true, false))
                        World.world.removeGameObject(object.getGuid());
                }
                player.sendTypeMessage(player.getLang().trans("command.commandplayer.transfer.master.title"),
                        player.getLang().trans("command.commandplayer.transfer.master.success",
                                String.valueOf(objects.size())));
            }, 1000);
            return true;
        }
        return false;
    }

    private static boolean commandStart(Player player, String msg) {
        int mapId = player.getCurMap().getId();
        if (player.isInPrison() || player.cantTP() || player.getFight() != null)
            return true;
        player.warpToSavePos();

        final Party party = player.getParty();
        if(party != null && party.getMaster() != null && party.getMaster().getName().equals(player.getName())) {
            for (Player slave : player.getParty().getPlayers()) {
                if (slave.getCurMap().getId() == mapId) {
                    if (!player.isInPrison())
                        if (!player.cantTP())
                            if (player.getFight() == null)
                                slave.warpToSavePos();
                }
            }
        }
        return true;
    }

    private static boolean commandAstrub(Player player, String msg) {
        int mapId = player.getCurMap().getId();
        if (player.isInPrison() || player.cantTP() || player.getFight() != null || Config.gameServerId == 22)
            return true;
        player.teleport((short) 952, 250);

        final Party party = player.getParty();
        if(party != null && party.getMaster() != null && party.getMaster().getName().equals(player.getName())) {
            player.getParty().getPlayers().stream().filter(p -> party.isWithTheMaster(p, false, true)).forEach(slave -> {
                if (slave.getCurMap().getId() == mapId) {
                    if (!player.isInPrison() && !player.cantTP())
                        if (player.getFight() == null)
                            slave.teleport((short) 952, 250);
                }
            });
        }
        return true;
    }

    private static boolean commandMaster(Player player, String msg) {
        Logging.getInstance().write("CommandMaster", "Player " + player.getName() + " invoked command: '" + msg + "'");
        String[] split = msg.split(" ");

        if (split.length > 1) {
            String name = split[1];
            final Party party = player.getParty();

            if (party == null) {
                player.sendMessage(player.getLang().trans("command.commandplayer.master.nogroup"));
                return true;
            }

            if (!party.getChief().equals(player)) {
                player.sendMessage(player.getLang().trans("command.commandplayer.master.noking"));
                return true;
            }

            Player target = World.world.getPlayerByName(name);
            if (target == null || target.getParty() != party) {
                player.sendMessage(player.getLang().trans("command.commandplayer.master.nogroup.name", name));
                return true;
            }

            party.setOne_windows(null);
            party.setMaster(target);
            for (Player member : party.getPlayers()) {
                member.send("PL" + target.getId());
                if (member != target) {
                    SocketManager.GAME_SEND_MESSAGE(member, member.getLang().trans("command.commandplayer.master.follow", target.getName()));
                }
            }
            SocketManager.GAME_SEND_MESSAGE(target, target.getLang().trans("command.commandplayer.master.master"));
            party.moveAllPlayersToMaster(null, false);
            return true;
        }

        if (player.getFight() != null) {
            player.sendMessage("Commande inutilisable en combat.");
            return true;
        }

        Party party = player.getParty();

        if (party != null && party.getMaster() != null) {
            if (party.getMaster().getId() == player.getId()) {
                party.setOne_windows(null);
                party.disband();
                player.setOne_windows(false);
                player.sendMessage("Mode Maître désactivé.");
            } else {
                player.sendMessage("Vous êtes déjà dans un groupe.");
            }
            return true;
        }

        if (party != null && !party.getChief().equals(player)) {
            player.sendMessage(player.getLang().trans("command.commandplayer.master.noking"));
            return true;
        }

        List<Player> candidates = new ArrayList<>();
        if (player.getCurMap() != null) {
            for (Player candidate : player.getCurMap().getPlayers()) {
                if (candidate == null || candidate == player)
                    continue;
                if (candidate.getGameClient() == null || candidate.getAccount() == null)
                    continue;
                if (candidate.getParty() != null || candidate.getFight() != null)
                    continue;
                if (!candidate.getAccount().getCurrentIp().equals(player.getAccount().getCurrentIp()))
                    continue;
                candidates.add(candidate);
            }
        }

        if (candidates.isEmpty()) {
            player.sendMessage("Aucune mule n'est sur la map");
            return true;
        }

        if (party == null) {
            Player first = candidates.remove(0);
            party = new Party(player, first);
            player.setParty(party);
            first.setParty(party);
            SocketManager.GAME_SEND_GROUP_CREATE(player.getGameClient(), party);
            SocketManager.GAME_SEND_PL_PACKET(player.getGameClient(), party);
            SocketManager.GAME_SEND_GROUP_CREATE(first.getGameClient(), party);
            SocketManager.GAME_SEND_PL_PACKET(first.getGameClient(), party);
            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(player.getGameClient(), party);
            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(first.getGameClient(), party);
            SocketManager.GAME_SEND_PM_ADD_PACKET_TO_GROUP(party, first);
            SocketManager.GAME_SEND_PR_PACKET(first);
            SocketManager.GAME_SEND_PR_PACKET(player);
            SocketManager.GAME_SEND_MESSAGE(first, "Vous suivez maintenant " + player.getName());
        }

        for (Player candidate : candidates) {
            if (party.getPlayers().size() >= 8)
                break;
            SocketManager.GAME_SEND_GROUP_CREATE(candidate.getGameClient(), party);
            SocketManager.GAME_SEND_PL_PACKET(candidate.getGameClient(), party);
            SocketManager.GAME_SEND_PM_ADD_PACKET_TO_GROUP(party, candidate);
            party.addPlayer(candidate);
            candidate.setParty(party);
            SocketManager.GAME_SEND_ALL_PM_ADD_PACKET(candidate.getGameClient(), party);
            SocketManager.GAME_SEND_PR_PACKET(candidate);
            SocketManager.GAME_SEND_MESSAGE(candidate, "Vous suivez maintenant " + player.getName());
        }

        party.setMaster(player);
        party.setChief(player);
        party.moveAllPlayersToMaster(null, false);
        SocketManager.GAME_SEND_MESSAGE(player, "Vous êtes désormais le maître de votre groupe");
        return true;
    }

    private static boolean commandWindow(Player player) {
        if (player.getFight() != null) {
            SocketManager.GAME_SEND_MESSAGE(player, "Commande indisponible en combat.");
            return true;
        }

        Party party = player.getParty();
        if (party == null || party.getMaster() == null || party.getMaster() != player) {
            SocketManager.GAME_SEND_MESSAGE(player, "Mets toi Maître avant");
            return true;
        }

        boolean enabled = !player.isOne_windows();
        player.setOne_windows(enabled);

        if (!enabled) {
            party.setOne_windows(null);
            SocketManager.GAME_SEND_ASK_WINDOW(player.getGameClient(), player);
            SocketManager.GAME_SEND_STATS_PACKET_ONE_WINDOWS(player, player);
            SocketManager.GAME_SEND_SPELL_LIST_ONE_WINDOWS(player, player);
            player.send("kI" + player.getId());
        }

        SocketManager.GAME_SEND_MESSAGE(player, enabled ? "One Window On" : "One Window Off");
        return true;
    }

    private static boolean commandFollowMap(Player player) {
        final Party party = player.getParty();

        if (party == null || party.getMaster() == null) {
            player.sendMessage(player.getLang().trans("command.commandplayer.followmap.nomaster"));
            return true;
        }

        if (party.getMaster() != player) {
            player.sendMessage(player.getLang().trans("command.commandplayer.followmap.notmaster", party.getMaster().getName()));
            return true;
        }

        final boolean enabled = party.toggleFollowSameMap();
        final String key = enabled ? "command.commandplayer.followmap.on" : "command.commandplayer.followmap.off";
        player.sendMessage(player.getLang().trans(key));
        return true;
    }

    private static boolean commandHelp(Player player, String msg) {
        player.sendMessage(player.getLang().trans("command.commandplayer.default"));
        return true;
    }

    //region Commands

    private static boolean commandAll(Player player, String msg) {
        if (player.isInPrison())
            return true;
        if(canalMute && player.getGroup() == null) {
            player.sendMessage(player.getLang().trans("command.commandplayer.commandall.unvailable"));
            return true;
        }
        if (player.noall) {
            player.sendMessage(player.getLang().trans("command.commandplayer.noall"));
            return true;
        }
        if (player.getGroup() == null && System.currentTimeMillis() - player.getGameClient().timeLastTaverne < 10000) {
            player.sendMessage(player.getLang().trans("command.commandplayer.allwait").replace("#1", String.valueOf(10 - ((System.currentTimeMillis() - player.getGameClient().timeLastTaverne) / 1000))));
            return true;
        }

        player.getGameClient().timeLastTaverne = System.currentTimeMillis();

        String prefix = "<font color='#C35617'>[" + (new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()))) + "] (" + canal + ") (" + (Config.gameServerKey.isEmpty() ? getNameServerById(Config.gameServerId) : Config.gameServerKey) + ") <b><a href='asfunction:onHref,ShowPlayerPopupMenu," + player.getName() + "'>" + player.getName() + "</a></b>";

        Logging.getInstance().write("AllMessage", "[" + (new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()))) + "] : " + player.getName() + " : " + msg.substring(5, msg.length() - 1));

        final String message = "Im116;" + prefix + "~" + msg.substring(5, msg.length()).replace(";", ":").replace("~", "").replace("|", "").replace("<", "").replace(">", "") + "</font>";

        World.world.getOnlinePlayers().stream().filter(p -> !p.noall).forEach(p -> p.send(message));
        Config.exchangeClient.send("DM" + player.getName() + ";" + getNameServerById(Config.gameServerId) + ";" + msg.substring(5, msg.length()).replace("\n", "").replace("\r", "").replace(";", ":").replace("~", "").replace("|", "").replace("<", "").replace(">", ""));
        return true;
    }

    private static boolean commandNoAll(Player player, String msg) {
        if (player.noall) {
            player.noall = false;
            player.sendMessage(player.getLang().trans("command.commandplayer.all.on"));
        } else {
            player.noall = true;
            player.sendMessage(player.getLang().trans("command.commandplayer.all.off"));
        }
        return true;
    }

    private static boolean commandUnlock(Player player, String msg) {
        GameMap current = player.getCurMap();
        if (current == null) {
            return true;
        }
        String[] parts = msg.split(" ");
        if (parts.length < 2) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlock.usage.direction"));
            return true;
        }

        int x = current.getX();
        int y = current.getY();
        switch (parts[1].toLowerCase()) {
            case "up":
                y--;
                break;
            case "down":
                y++;
                break;
            case "left":
                x--;
                break;
            case "right":
                x++;
                break;
            default:
                player.sendMessage(player.getLang().trans("command.commandplayer.unlock.direction.unknown"));
                return true;
        }

        ArrayList<GameMap> maps = World.world.getMapByPosInArray(x, y);
        if (maps.isEmpty()) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlock.nomap",
                    String.valueOf(x), String.valueOf(y)));
            return true;
        }

        int mapId = maps.get(0).getId();

        if (player.hasUnlocked(mapId)) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlock.already",
                    String.valueOf(mapId)));
            return true;
        }

        if (player.getTilemanCredits() < 1) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlock.notenough"));
            return true;
        }

        player.unlockMap(mapId);
        player.setTilemanCredits(player.getTilemanCredits() - 1);
        ((PlayerData) DatabaseManager.get(PlayerData.class)).update(player);
        player.sendMessage(player.getLang().trans("command.commandplayer.unlock.success",
                String.valueOf(mapId), String.valueOf(player.getTilemanCredits())));
        return true;
    }

    private static boolean commandIdUnlock(Player player, String msg) {
        String[] parts = msg.split(" ");
        if (parts.length < 2) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlockid.usage"));
            return true;
        }
        try {
            int mapId = Integer.parseInt(parts[1]);
            GameMap map = World.world.getMap(mapId);
            if (map == null) {
                player.sendMessage(player.getLang().trans("command.commandplayer.unlock.unknown.map",
                        String.valueOf(mapId)));
                return true;
            }

            if (player.hasUnlocked(mapId)) {
                player.sendMessage(player.getLang().trans("command.commandplayer.unlock.already",
                        String.valueOf(mapId)));
                return true;
            }

            if (player.getTilemanCredits() < 1) {
                player.sendMessage(player.getLang().trans("command.commandplayer.unlock.notenough"));
                return true;
            }

            player.unlockMap(mapId);
            player.setTilemanCredits(player.getTilemanCredits() - 1);
            ((PlayerData) DatabaseManager.get(PlayerData.class)).update(player);
            player.sendMessage(player.getLang().trans("command.commandplayer.unlock.success",
                    String.valueOf(mapId), String.valueOf(player.getTilemanCredits())));
        } catch (NumberFormatException e) {
            player.sendMessage(player.getLang().trans("command.commandplayer.unlockid.usage.format"));
        }
        return true;
    }

    private static boolean commandAutoUnlock(Player player, String msg) {
        player.setAutoUnlock(!player.isAutoUnlock());
        if (player.isAutoUnlock()) {
            player.sendMessage(player.getLang().trans("command.commandplayer.autounlock.enabled"));
        } else {
            player.sendMessage(player.getLang().trans("command.commandplayer.autounlock.disabled"));
        }
        return true;
    }


    private static boolean commandTileman(Player player, String msg) {
        int requirement = player.getXpForNextCredit();
        player.sendTypeMessage(player.getLang().trans("command.commandplayer.tileman.title"),
                player.getLang().trans("command.commandplayer.tileman.message",
                        String.valueOf(player.getTilemanCredits()),
                        String.valueOf(player.getTilemanCreditXp()),
                        String.valueOf(requirement)));
        return true;
    }

    private static boolean commandDeblo(Player player, String msg) {
        if (player.cantTP())
            return true;
        if (player.getFight() != null)
            return true;
        if (player.getCurCell().isWalkable(false)) {
            player.sendMessage(player.getLang().trans("command.commandplayer.deblo.no"));
            return true;
        }
        player.teleport(player.getCurMap().getId(), player.getCurMap().getRandomFreeCellId());
        return true;
    }

    private static boolean commandInfos(Player player, String msg) {
        long uptime = System.currentTimeMillis()
                - Config.startTime;
        int jour = (int) (uptime / (1000 * 3600 * 24));
        uptime %= (1000 * 3600 * 24);
        int hour = (int) (uptime / (1000 * 3600));
        uptime %= (1000 * 3600);
        int min = (int) (uptime / (1000 * 60));
        uptime %= (1000 * 60);
        int sec = (int) (uptime / (1000));
        int nbPlayer = (int) Config.gameServer.getClients().stream().filter(gc -> gc != null && gc.getPlayer() != null).count();

        String mess = player.getLang().trans("command.commandplayer.info.uptime", String.valueOf(jour), String.valueOf(hour), String.valueOf(min), String.valueOf(sec));
        if (nbPlayer > 0)
            mess += player.getLang().trans("command.commandplayer.info.online", String.valueOf(nbPlayer));
        player.sendMessage(mess);
        return true;
    }

    private static boolean commandStaff(Player player, String msg) {
        String message = player.getLang().trans("command.commandplayer.staff");
        boolean vide = true;
        for (Player target : World.world.getOnlinePlayers()) {
            if (target == null)
                continue;
            if (target.getGroup() == null || target.isInvisible())
                continue;

            message += "\n- <b><a href='asfunction:onHref,ShowPlayerPopupMenu," + target.getName() + "'>[" + target.getGroup().getName() + "] " + target.getName() + "</a></b>";
            vide = false;
        }
        if (vide)
            message = player.getLang().trans("command.commandplayer.staff.none");
        player.sendMessage(message);
        return true;
    }

    private static boolean command(String msg, String command) {
        return msg.length() > command.length() && msg.substring(1, command.length() + 1).equalsIgnoreCase(command);
    }
    //endregion

    private static String getNameServerById(int id) {
        switch (id) {
            case 13:
                return "Silouate";
            case 19:
                return "Allister";
            case 22:
                return "Oto Mustam";
            case 1:
                return "Jiva";
            case 2:
                return "Zeus";
            case 37:
                return "Nostalgy";

            case 4001:
                return "Alma";
            case 4002:
                return "Aguabrial";
            case 4005:
                return "Bolgrot";
        }
        return "Unknown";
    }
}