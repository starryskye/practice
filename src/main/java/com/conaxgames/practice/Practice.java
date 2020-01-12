package com.conaxgames.practice;

import com.conaxgames.CorePlugin;
import com.conaxgames.internal.com.mongodb.client.MongoDatabase;
import com.conaxgames.practice.arena.ArenaManager;
import com.conaxgames.practice.kit.KitManager;
import com.conaxgames.practice.lobby.LobbyManager;
import com.conaxgames.practice.queue.QueueManager;
import com.conaxgames.practice.util.adapter.ItemStackTypeAdapter;
import com.conaxgames.practice.util.adapter.LocationTypeAdapter;
import com.conaxgames.util.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Practice extends JavaPlugin {

    @Getter private static Practice instance;
    public static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackTypeAdapter())
            .registerTypeHierarchyAdapter(Location.class, new LocationTypeAdapter())
            .serializeNulls()
            .create();

    private Config mainConfig;

    private MongoDatabase mongoDatabase;

    private ArenaManager arenaManager;
    private LobbyManager lobbyManager;
    private KitManager kitManager;
    private QueueManager queueManager;

    public void onEnable() {
        instance = this;

        this.mainConfig = new Config("config", this);

        this.mongoDatabase = CorePlugin.getInstance().getCoreDatabase();

        this.arenaManager = new ArenaManager();
        this.lobbyManager = new LobbyManager();
        this.kitManager = new KitManager();
        this.queueManager = new QueueManager();
    }

    public void onDisable() {
        kitManager.saveAllKits();
        arenaManager.saveAllArenas();
    }
}
