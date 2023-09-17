package com.github.vini2003.linkart;

import com.github.vini2003.linkart.compat.audaki.AudakiCartIntegration;
import com.github.vini2003.linkart.compat.Mods;
import com.github.vini2003.linkart.configuration.LinkartConfiguration;
import com.github.vini2003.linkart.mixin.PersistentStateAccessor;
import com.github.vini2003.linkart.utility.LinkartCommand;
import com.github.vini2003.linkart.utility.LoadingCarts;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Linkart implements ModInitializer {

    public static final String ID = "linkart";
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("linkart.json");
    public static LinkartConfiguration CONFIG;
    public static final Map<PlayerEntity, AbstractMinecartEntity> LINKING_CARTS = new HashMap<>();
    public static final Map<PlayerEntity, AbstractMinecartEntity> UNLINKING_CARTS = new HashMap<>();
    public static final TagKey<Item> LINKERS = TagKey.of(RegistryKeys.ITEM, new Identifier(ID, "linkers"));

    static {
        loadConfig();
    }

    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            LinkartCommand.register(dispatcher);
        });

        ServerWorldEvents.LOAD.register((server, world) -> {
            if (CONFIG.chunkloading) LoadingCarts.getOrCreate(world);
        });

        ServerTickEvents.START_WORLD_TICK.register(world -> {
            if (CONFIG.chunkloading && ((PersistentStateAccessor)world.getPersistentStateManager()).linkart$loadedStates().containsKey("linkart_loading_carts")) {
                LoadingCarts.getOrCreate(world).tick();
            }
        });

        try {
            if (
                Mods.AUDAKI_CART_ENGINE.isLoaded
                && Mods.AUDAKI_CART_ENGINE.version.compareTo(Version.parse("2.1.0")) >= 0
            ) {
                System.out.println("[Linkart] Version minimum met, loading AudakiCartIntegration");
                Mods.AUDAKI_CART_ENGINE.executeIfInstalled(() -> () -> AudakiCartIntegration.init());
            } else {
                System.out.println("[Linkart] Version minimum not met, not loading AudakiCartIntegration");
            }
        } catch (VersionParsingException e) {
            System.out.println("[Linkart] VersionParsingException during AudakiCartIntegration init, ignoring since should never happen");
        }
    }

    public static void loadConfig() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (Files.exists(CONFIG_PATH)) {
            try(var reader = Files.newBufferedReader(CONFIG_PATH)) {
                CONFIG = gson.fromJson(reader, LinkartConfiguration.class);
                Files.write(CONFIG_PATH, gson.toJson(CONFIG).getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to load linkart config!", e);
            }
        } else {
            try {
                CONFIG = new LinkartConfiguration();
                Files.write(CONFIG_PATH, gson.toJson(CONFIG).getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create linkart config!", e);
            }
        }
    }
}
