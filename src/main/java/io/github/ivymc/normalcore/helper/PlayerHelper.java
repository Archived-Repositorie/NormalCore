package io.github.ivymc.normalcore.helper;

import eu.pb4.playerdata.api.PlayerDataApi;
import eu.pb4.playerdata.api.storage.JsonDataStorage;
import eu.pb4.playerdata.api.storage.PlayerDataStorage;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerHelper {
    private static final PlayerDataStorage<PlayerData> storage = new JsonDataStorage<>("normalcore", PlayerData.class);
    private final ServerPlayerEntity playerEntity;
    private PlayerHelper(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }
    public static PlayerHelper of(ServerPlayerEntity playerEntity) {
        return new PlayerHelper(playerEntity) {};
    }
    public PlayerData getData() {
        var data = PlayerDataApi.getCustomDataFor(playerEntity, storage);
        if (data == null) {
            data = new PlayerData();
            PlayerDataApi.setCustomDataFor(playerEntity, storage, data);
        }
        return data;
    }
    public PlayerData writeData(PlayerData data) {
        PlayerDataApi.setCustomDataFor(playerEntity, storage, data);
        return data;
    }
    public static boolean register() {
        return PlayerDataApi.register(storage);
    }
}
