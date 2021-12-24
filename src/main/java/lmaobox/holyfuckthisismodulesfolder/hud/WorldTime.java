package lmaobox.holyfuckthisismodulesfolder.hud;

import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import lmaobox.holyfuckthisismodulesfolder.hud.render.TextHUDElement;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WorldTime extends TextHUDElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat timeFormatSeconds = new SimpleDateFormat("HH:mm:ss");

    private final Setting<Boolean> seconds = sgGeneral.add(new BoolSetting.Builder()
        .name("seconds")
        .description("Shows seconds.")
        .defaultValue(true)
        .build()
    );

    public WorldTime(HUD hud) {
        super(hud, "world-time", "display world time.", true);
    }

    @Override
    protected String getLeft() {
        return "World Time: ";
    }

    @Override
    protected String get2Left() {
        return "";
    }

    @Override
    protected String getRight() {
        if (seconds.get()) return timeFormatSeconds.format(new Date());
        return timeFormat.format(new Date());
    }

    @Override
    protected String get2Right() {
        return "";
    }

    @Override
    protected String get3Right() {
        return "";
    }

    @Override
    public String getEnd() {
        return ".";
    }
}
