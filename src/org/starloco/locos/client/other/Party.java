package org.starloco.locos.client.other;

import org.starloco.locos.area.map.GameCase;
import org.starloco.locos.client.Player;
import org.starloco.locos.common.PathFinding;
import org.starloco.locos.common.SocketManager;

import java.util.ArrayList;
import java.util.List;

public class Party {

    private Player chief, master;
    private Player oneWindowTarget;
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<MasterOption> options = new ArrayList<>();
    private boolean followSameMap;

    public Party(Player p1, Player p2) {
        this.chief = p1;
        this.players.add(p1);
        this.players.add(p2);
    }
    public Player getChief() {
        return this.chief;
    }

    public void setChief(Player chief) {
        this.chief = chief;
    }

    public boolean isChief(int id) {
        return this.chief.getId() == id;
    }

    public Player getMaster() {
        return master;
    }

    public void setMaster(Player master) {
        this.master = master;
        if (master == null) {
            this.followSameMap = false;
            this.oneWindowTarget = null;
        }
    }

    public Player getOne_windows() {
        return oneWindowTarget;
    }

    public void setOne_windows(Player oneWindowTarget) {
        this.oneWindowTarget = oneWindowTarget;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public boolean isFollowSameMapEnabled() {
        return followSameMap;
    }

    public boolean toggleFollowSameMap() {
        this.followSameMap = !this.followSameMap;
        return this.followSameMap;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void leave(Player player) {
        if (!this.players.contains(player)) return;

        player.follow = null;
        player.follower.clear();
        player.setParty(null);
        this.players.removeIf(player1 ->  player1.getId() == player.getId());

        for(Player member : this.players) {
            if(member.follow == player) member.follow = null;
            member.follower.remove(player.getId());
        }

        if(this.oneWindowTarget != null && this.oneWindowTarget.getId() == player.getId()) {
            this.oneWindowTarget = null;
        }

        if (this.players.size() == 1) {
            this.players.get(0).setParty(null);
            if (this.players.get(0).getAccount() == null || this.players.get(0).getGameClient() == null)
                return;
            SocketManager.GAME_SEND_PV_PACKET(this.players.get(0).getGameClient(), "");
        } else {
            if(this.isChief(player.getId())) {
                this.chief = this.players.get(0);
                for(Player member : this.players) {
                    member.send("PL" + this.chief.getId());
                }
            }
            SocketManager.GAME_SEND_PM_DEL_PACKET_TO_GROUP(this, player.getId());
        }
    }

    public void disband() {
        for(Player player : new ArrayList<>(this.players)) {
            if(player == null) continue;
            player.follow = null;
            player.follower.clear();
            player.setParty(null);
            player.setOne_windows(false);
            if(player.getGameClient() != null) {
                SocketManager.GAME_SEND_PM_DEL_PACKET_TO_GROUP(this, player.getId());
                SocketManager.GAME_SEND_PV_PACKET(player.getGameClient(), "");
            }
        }
        this.players.clear();
        this.options.clear();
        this.master = null;
        this.oneWindowTarget = null;
    }

    public void moveAllPlayersToMaster(final GameCase cell, boolean tp) {
        if(this.master != null) {
            this.players.stream().filter((follower1) -> isWithTheMaster(follower1, false, false)).forEach(follower -> follower.setBlockMovement(true));
            this.players.stream().filter((follower1) -> isWithTheMaster(follower1, false, false)).forEach(follower -> {
                try {
                    final GameCase newCell = cell != null ? cell : this.master.getCurCell(), lastCell;
                    List<GameCase> cells = PathFinding.getShortestPathBetween(this.master.getCurMap(), follower.getCurCell().getId(), newCell.getId(), 0);

                    if(!cells.isEmpty()) {
                        lastCell = cells.get(cells.size() - 1);

                        if (lastCell != null) {
                            if (tp) {
                                follower.teleport(this.master.getCurMap(), this.master.getCurCell().getId());
                            } else {
                                String path = PathFinding.getShortestStringPathBetween(this.master.getCurMap(), follower.getCurCell().getId(), lastCell.getId(), 0);

                                if(path != null) {
                                    follower.getCurCell().removePlayer(follower);
                                    follower.setCurCell(lastCell);
                                    follower.getCurCell().addPlayer(follower);
                                    SocketManager.GAME_SEND_GA_PACKET_TO_MAP(follower.getCurMap(), "0", 1, follower.getId(), path);
                                }
                            }
                        }
                    }
                } catch (Exception ignored) {}
            });
            this.players.stream().filter((follower1) -> isWithTheMaster(follower1, false, false)).forEach(follower -> follower.setBlockMovement(false));
        }
    }

    public boolean isWithTheMaster(Player follower, boolean inFight, boolean changeMap) {
        boolean sameMap = changeMap || (this.master.getCurMap() == follower.getCurMap() && this.master.getCurMap().getId() == follower.getCurMap().getId());

        return sameMap && !follower.getName().equals(this.master.getName()) &&  this.players.contains(follower) && follower.getGameClient() != null &&
                this.haveSameIp(follower) && (inFight ? follower.getFight() == this.master.getFight() : follower.getFight() == null);
    }

    private boolean haveSameIp(Player follower) {
        if(follower.getAccount() != null && this.master != null && this.master.getAccount() != null)
            return follower.getAccount().getCurrentIp().equals(this.master.getAccount().getCurrentIp());
        return false;
    }

    public MasterOption getOptionByPlayer(Player player) {
        if(this.players.contains(player)) {
            for (MasterOption option : options)
                if (option.player.getId() == player.getId())
                    return option;
            MasterOption option = new MasterOption(player);
            options.add(option);
            return option;
        }
        return null;
    }

    public ArrayList<MasterOption> getOptions() {
        return options;
    }

    public static class MasterOption {

        private final Player player;
        private byte second = 1, pass = 0;

        public MasterOption(Player player) {
            this.player = player;
        }

        public void setSecond(byte second) {
            this.second = second;
        }

        public byte getSecond() {
            return second;
        }

        public void togglePass() {
            this.pass = (byte) (this.pass == 0 ? 1 : 0);
        }

        public boolean passAuto() {
            return pass == 1;
        }
    }
}