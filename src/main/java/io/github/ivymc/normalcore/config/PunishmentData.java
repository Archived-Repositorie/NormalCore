package io.github.ivymc.normalcore.config;

import com.google.gson.JsonObject;
import io.github.ivymc.ivycore.config.ConfigData;
import io.github.ivymc.normalcore.PreMain;
import io.github.ivymc.normalcore.RegistryTypes;
import io.github.ivymc.normalcore.config.punish.BaseClass;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PunishmentData extends ConfigData {
    public boolean enable = false;
    public PunishmentClass punishment = new PunishmentClass();
    public int lives = 2;
    public boolean cancel = false;
    public boolean forcedrop = false;
    @Override
    public void onRead(JsonObject jsonObject) throws Exception {
        punishment.baseClass = getPunishment(punishment,jsonObject.get("punishment").getAsJsonObject());
    }

    public static class PunishmentClass {
        public BaseClass baseClass = new BaseClass() {
            @Override
            public void accept(JsonObject json) throws Exception {
            }

            @Override
            public void onDeath(ServerPlayerEntity player) {

            }
        };
        public String type = "normalcore:simple";
        public boolean afterdeath = false;
    }

    private BaseClass getPunishment(PunishmentClass punishment, JsonObject object) throws Exception {
        if(punishment == null) throw new Exception("Punishment is null");
        String type = punishment.type;
        if(type == null) throw new Exception("Punishment type is null");
        BaseClass clazz = PreMain.registry.get(RegistryTypes.PUNISHMENT, Identifier.tryParse(type));
        if(clazz == null) throw new Exception("Punishment type is not registered");
        clazz.accept(object);
        return clazz;
    }
}
