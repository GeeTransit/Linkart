package com.github.vini2003.linkart.compat;

import java.util.Optional;
import java.util.function.Supplier;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;

/**
 * For compatibility with and without another mod present, we have to define load conditions of the specific code
 */
public enum Mods {
    AUDAKI_CART_ENGINE("audaki_cart_engine")
    ;

    public final boolean isLoaded;
    public final String fabricId;
    public final Version version;

    Mods(String fabricId) {
        this.fabricId = fabricId;
        this.isLoaded = FabricLoader.getInstance().isModLoaded(fabricId);
        if (!this.isLoaded) {
            this.version = null;
        } else {
            this.version = FabricLoader.getInstance().getModContainer(fabricId).get().getMetadata().getVersion();
        }
    }

    /**
     * Simple hook to run code if a mod is installed
     * @param toRun will be run only if the mod is loaded
     * @return Optional.empty() if the mod is not loaded, otherwise an Optional of the return value of the given supplier
     */
    public <T> Optional<T> runIfInstalled(Supplier<Supplier<T>> toRun) {
        if (isLoaded)
            return Optional.of(toRun.get().get());
        return Optional.empty();
    }

    /**
     * Simple hook to execute code if a mod is installed
     * @param toExecute will be executed only if the mod is loaded
     */
    public void executeIfInstalled(Supplier<Runnable> toExecute) {
        if (isLoaded) {
            toExecute.get().run();
        }
    }
}
