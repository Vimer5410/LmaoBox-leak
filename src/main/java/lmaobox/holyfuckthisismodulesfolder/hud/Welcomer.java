package lmaobox.holyfuckthisismodulesfolder.hud;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.NameProtect;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import lmaobox.holyfuckthisismodulesfolder.hud.render.TextHUDElement;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Welcomer extends TextHUDElement {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Message> message = sgGeneral.add(new EnumSetting.Builder<Message>()
        .name("message")
        .description("Determines what message style to use.")
        .defaultValue(Welcomer.Message.Hello)
        .build()
    );

    private final Setting<SettingColor> usernameColor = sgGeneral.add(new ColorSetting.Builder()
        .name("name-color")
        .description("color of the username.")
        .defaultValue(new SettingColor(255, 255, 255,255))
        .build()
    );

    private final Setting<String> custom = sgGeneral.add(new StringSetting.Builder()
        .name("custom-welcomer")
        .description("custom welcome message.")
        .defaultValue("You got lmaoboxed")
        .build()
    );

    private final Setting<String> custom2 = sgGeneral.add(new StringSetting.Builder()
        .name("custom-welcomer-end")
        .description("custom welcome end.")
        .defaultValue("8^)")
        .build()
    );

    public Welcomer(HUD hud) {
        super(hud, "welcomer", "displays a welcome message.", true);
        rightColor = usernameColor.get();
    }

    @Override
    protected String getLeft() {
        switch (message.get()) {
            case WorldTime -> {
                if (Modules.get().isActive(NameProtect.class)) return getTime() + " ";
                else return getTime() + " ";
            }
            case Lmaobox -> {
                if (Modules.get().isActive(NameProtect.class)) return "Welcome to Lmaobox ";
                else return "Welcome to Lmaobox ";
            }
            case Custom -> {
                if (Modules.get().isActive(NameProtect.class)) return custom + " ";
                else return custom + " ";
            }
            default -> {
                if (Modules.get().isActive(NameProtect.class)) return "Hello ";
                else return "Hello ";
            }
        }
    }

    @Override
    public String get2Left() {
        return "";
    }

    @Override
    protected String getRight() {
        return Modules.get().get(NameProtect.class).getName(mc.getSession().getUsername());
    }

    @Override
    public String get2Right() {
        return "";
    }

    @Override
    public String get3Right() {
        return "";
    }

    @Override
    public String getEnd() {
        return " " + custom2;
    }

    private String getTime() {
        final String hourDate = new SimpleDateFormat("k").format(new Date());
        final int hour = Integer.valueOf(hourDate);
        if (hour < 6) return "Good Night";
        if (hour < 12) return "Good Morning";
        if (hour < 17) return "Good Afternoon";
        if (hour < 20) return "Good Evening";
        return "Good Night";
    }

    public enum Message {
        Hello,
        Lmaobox,
        WorldTime,
        Custom;

        @Override
        public String toString() {
            return super.toString().replace("_", " ");
        }
    }
}
