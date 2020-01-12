package com.conaxgames.practice.arena;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.bukkit.Location;

@Data
public class Arena {

    /**
     * The internal ID of this arena. This field
     * is used to load, save, and identify specific
     * arenas.
     */
    @SerializedName("_id") private final String name;

    /**
     * Locations players spawn at when a match starts.
     */
    private final Location spawnA, spawnB;

    /**
     * Whether or not this arena is in use.
     */
    private boolean beingUsed;

}
