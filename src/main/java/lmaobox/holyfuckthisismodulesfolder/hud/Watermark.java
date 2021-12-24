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

public class Watermark extends TextHUDElement {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

// first part
    private final Setting<String> custom0 = sgGeneral.add(new StringSetting.Builder()
        .name("first-text")
        .description("first watermark part text.")
        .defaultValue("Lmao")
        .build()
    );

    private final Setting<SettingColor> ColorNull = sgGeneral.add(new ColorSetting.Builder()
        .name("first-text-color")
        .description("")
        .defaultValue(new SettingColor(255, 255, 255,255))
        .build()
    );
// second part
    private final Setting<String> custom1 = sgGeneral.add(new StringSetting.Builder()
        .name("second-text")
        .description("second watermark part color.")
        .defaultValue("Box")
        .build()
    );

    private final Setting<SettingColor> ColorA = sgGeneral.add(new ColorSetting.Builder()
        .name("second-text-color")
        .description("")
        .defaultValue(new SettingColor(255, 255, 255,255))
        .build()
    );
// middle part
    private final Setting<String> custom2 = sgGeneral.add(new StringSetting.Builder()
        .name("middle-text")
        .description("middle watermark part text.")
        .defaultValue("-")
        .build()
    );

    private final Setting<SettingColor> ColorB = sgGeneral.add(new ColorSetting.Builder()
        .name("middle-text-color")
        .description("")
        .defaultValue(new SettingColor(255, 255, 255,255))
        .build()
    );
// end part
    private final Setting<String> custom3 = sgGeneral.add(new StringSetting.Builder()
        .name("end-text")
        .description("end watermark part text.")
        .defaultValue("" + LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.VERSION)
        .build()
    );

    private final Setting<SettingColor> ColorC = sgGeneral.add(new ColorSetting.Builder()
        .name("end-color")
        .description("")
        .defaultValue(new SettingColor(255, 255, 255,255))
        .build()
    );

    public Watermark(HUD hud) {
        super(hud, "cool-watermark", "cool", true);
        colorLeft = ColorNull.get();
        rightColor = ColorA.get();
        right2Color = ColorB.get();
        right3Color = ColorC.get();
    }


    @Override
    protected String getLeft() {
        return "";
    }

    @Override
    protected String get2Left() {
        return custom0 + "";
    }

    @Override
    protected String getRight() {
        return custom1 + " ";
    }

    @Override
    protected String get2Right() {
        return custom2 + " ";
    }

    @Override
    protected String get3Right() {
        return custom3 + "";
    }

    @Override
    public String getEnd() {
        return "";
    }

        @Override
        public String toString() {
            return super.toString().replace("_", " ");
        }
    }
