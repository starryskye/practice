package com.conaxgames.practice.match;

import com.conaxgames.practice.Practice;
import com.conaxgames.practice.arena.Arena;
import com.conaxgames.practice.kit.Kit;
import com.conaxgames.practice.match.task.MatchCountdownTask;
import com.conaxgames.util.finalutil.CC;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;

import java.util.*;

@Getter
public class Match {

    /**
     * Unique identifier of the match. Used when
     * storing in database.
     */
    private final UUID matchUuid = UUID.randomUUID();

    /**
     * The arena this match is being held in.
     */
    private final Arena arena;

    /**
     * The kit that this match is using.
     */
    private final Kit kit;

    /**
     * List of all teams participating in this match.
     */
    private final List<MatchTeam> teams;

    /**
     * List of players spectating this match.
     */
    private final Set<UUID> spectators = new HashSet<>();

    /**
     * List of all players that have ever spectated this
     * match. Mainly used to determine whether or not to
     * broadcast a spectator message.
     */
    private final Set<UUID> previousSpectators = new HashSet<>();

    /**
     * Maintains a list of locations that players placed
     * blocks at. Mainly used to determine whether or not
     * a player can break a block.
     */
    private final Set<Location> placedBlocks = new HashSet<>();

    /**
     * Maintains a list of changed block states. These are
     * restored when a match ends, putting the arena in it's
     * original state (before the match started.)
     */
    private final List<BlockState> blockChanges = new LinkedList<>();

    /**
     * Whether or not this match is ranked.
     */
    private final boolean isRanked;

    /**
     * The state of the current match. Will always start as
     * {@link MatchState#COUNTDOWN}
     */
    private MatchState state = MatchState.COUNTDOWN;

    /**
     * The team that won this match. Will almost never be
     * set until the match is finished.
     */
    private MatchTeam winningTeam;

    public Match(Arena arena, Kit kit, boolean isRanked, MatchTeam... teams) {
        this.arena = arena;
        this.kit = kit;
        this.isRanked = isRanked;
        this.teams = Arrays.asList(teams);

        arena.setBeingUsed(true);

        new MatchCountdownTask(this).runTaskTimer(Practice.getInstance(), 0L, 20L);
    }

    /**
     * Starts the match. Sets the state to {@link MatchState#IN_PROGRESS}
     * and broadcasts a start message and sound to everybody.
     */
    public void startMatch() {
        state = MatchState.IN_PROGRESS;
        broadcast(CC.GREEN + "The match has started.");
    }

    /**
     * Broadcasts a message to every team in the match.
     *
     * @param message the message to broadcast
     */
    public void broadcast(String message) {
        teams.forEach(team -> team.broadcast(message));
    }

    /**
     * Broadcasts a sound to every team in the match.
     *
     * @param sound the sound to broadcast
     * @param pitch the pitch of the sound
     */
    public void broadcast(Sound sound, float pitch) {
        teams.forEach(team -> team.getLivingPlayerList()
                .forEach(player -> player.playSound(player.getLocation(), sound, 10, pitch)));
    }
}