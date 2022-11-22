package io.github.ivymc.normalcore.config.punish;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.ivymc.normalcore.helper.PlayerData;
import io.github.ivymc.normalcore.helper.PlayerHelper;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class Updating extends BaseClass {
    private final long[] useTime = {0, 0, 0};
    protected int time;
    public int update;

    @Override
    public boolean accept(JsonObject json) {
        JsonElement time = json.get("time");
        JsonElement update = json.get("update");
        if(time == null) return false;
        if(update == null) return false;
        this.time = time.getAsInt();
        this.update = update.getAsInt();
        return super.accept(json);
    }

    public abstract void review(ServerPlayerEntity player);
    public abstract void update(ServerPlayerEntity player, boolean join);

    protected String getUseTime(ServerPlayerEntity player) {
        PlayerData data = PlayerHelper.of(player).getData();
        String time = convertTime(getDateDiff(new Date(), data.deathEnd, TimeUnit.SECONDS));
        return time;
    }

    protected String convertTime(long time) {
        long days = TimeUnit.SECONDS.toDays(time);
        long hours = TimeUnit.SECONDS.toHours(time) - TimeUnit.DAYS.toHours(days);
        long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.DAYS.toMinutes(days) - TimeUnit.HOURS.toMinutes(hours);
        long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.DAYS.toSeconds(days) - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);
        useTime[0] = days;
        useTime[1] = hours;
        useTime[2] = minutes;
        String timeStr = "";
        if (useTime[0] > 0) {
            timeStr += String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        } else if (useTime[1] > 0) {
            timeStr += String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else if (useTime[2] > 0) {
            timeStr += String.format("%02d:%02d", minutes, seconds);
        } else {
            timeStr += String.format("%02d", seconds);
        }
        return timeStr;
    }

    public void onDeath(ServerPlayerEntity player) {
        PlayerData data = PlayerHelper.of(player).getData();
        data.deathEnd = new Date(new Date().getTime() + (this.time * 1000L));
        data.death = true;
        update(player, false);
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
    public boolean isDead(ServerPlayerEntity player) {
        PlayerData data = PlayerHelper.of(player).getData();
        return getDateDiff(new Date(), data.deathEnd, TimeUnit.SECONDS) > 0;
    }
}
