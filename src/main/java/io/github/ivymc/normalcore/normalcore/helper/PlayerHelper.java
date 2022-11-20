package io.github.ivymc.normalcore.normalcore.helper;

import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.JsonDataStorage;
import eu.pb4.playerdata.api.storage.PlayerDataStorage;
import io.github.ivymc.normalcore.helper.PlayerData;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerHelper {
    private static final PlayerDataStorage<io.github.ivymc.normalcore.helper.PlayerData> storage = new JsonDataStorage<>("normalcore", io.github.ivymc.normalcore.helper.PlayerData.class);
    private final ServerPlayerEntity playerEntity;
    private PlayerHelper(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }
    public static PlayerHelper of(ServerPlayerEntity playerEntity) {
        return new PlayerHelper(playerEntity) {};
    }
    public io.github.ivymc.normalcore.helper.PlayerData getData() {
        var data = PlayerDataApi.getCustomDataFor(playerEntity, storage);
        if (data == null) {
            data = new io.github.ivymc.normalcore.helper.PlayerData();
            PlayerDataApi.setCustomDataFor(playerEntity, storage, data);
        }
        return data;
    }
    public io.github.ivymc.normalcore.helper.PlayerData writeData(PlayerData data) {
        PlayerDataApi.setCustomDataFor(playerEntity, storage, data);
        return data;
    }
    public static boolean register() {
        return PlayerDataApi.register(storage);
    }
}
