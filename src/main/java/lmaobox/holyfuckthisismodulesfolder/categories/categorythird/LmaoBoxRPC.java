package lmaobox.holyfuckthisismodulesfolder.categories.categorythird;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.misc.MeteorStarscript;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.starscript.Script;
import meteordevelopment.starscript.compiler.Compiler;
import meteordevelopment.starscript.compiler.Parser;
import meteordevelopment.starscript.utils.Error;
import meteordevelopment.starscript.utils.StarscriptError;
import net.minecraft.util.Util;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LmaoBoxRPC extends Module {

    List<String> line1Strings = Collections.singletonList("good meteor addon.");
    Integer line1UpdateDelay = 20;
    List<String> line2Strings = Collections.singletonList("{player} | {server}");
    Integer line2UpdateDelay = 20;

    private static final DiscordRichPresence rpc = new DiscordRichPresence();
    private static final DiscordRPC instance = DiscordRPC.INSTANCE;
    private boolean forceUpdate;

    private final List<Script> line1Scripts = new ArrayList<>();
    private int line1Ticks, line1I;

    private final List<Script> line2Scripts = new ArrayList<>();
    private int line2Ticks, line2I;

    public LmaoBoxRPC() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD, "lmaoBox-RPC", "");
    }


    @Override
    public void onActivate() {
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        instance.Discord_Initialize("904740463553150996", handlers, true, null);

        rpc.startTimestamp = System.currentTimeMillis() / 1000L;

        rpc.largeImageKey = "lmaoboxv2";
        String largeText = "" + LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.VERSION;
        rpc.largeImageText = largeText;

        recompileLine1();
        recompileLine2();

        line1Ticks = 0;
        line2Ticks = 0;

        line1I = 0;
        line2I = 0;
    }

    @Override
    public void onDeactivate() {
        instance.Discord_ClearPresence();
        instance.Discord_Shutdown();
    }

    private void recompile(List<String> messages, List<Script> scripts) {
        scripts.clear();

        for (int i = 0; i < messages.size(); i++) {
            Parser.Result result = Parser.parse(messages.get(i));

            if (result.hasErrors()) {
                if (Utils.canUpdate()) {
                    Error error = result.errors.get(0);
                    ChatUtils.error("Starscript", "%d, %d '%c': %s", i, error.character, error.ch, error.message);
                }

                continue;
            }

            scripts.add(Compiler.compile(result));
        }

        forceUpdate = true;
    }

    private void recompileLine1() {
        recompile(line1Strings, line1Scripts);
    }

    private void recompileLine2() {
        recompile(line2Strings, line2Scripts);
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {

        if (!Utils.canUpdate()) return;
        boolean update = false;

        if (line1Ticks >= line1UpdateDelay || forceUpdate) {
            if (line1Scripts.size() > 0) {
                int i = Utils.random(0, line1Scripts.size());


                try {
                    rpc.details = MeteorStarscript.ss.run(line1Scripts.get(i));
                } catch (StarscriptError e) {
                    ChatUtils.error("Starscript", e.getMessage());
                }
            }
            update = true;

            line1Ticks = 0;
        } else line1Ticks++;

        if (line2Ticks >= line2UpdateDelay || forceUpdate) {
            if (line2Scripts.size() > 0) {
                int i = Utils.random(0, line2Scripts.size());


                try {
                    rpc.state = MeteorStarscript.ss.run(line2Scripts.get(i));
                } catch (StarscriptError e) {
                    ChatUtils.error("Starscript", e.getMessage());
                }
            }
            update = true;

            line2Ticks = 0;
        } else line2Ticks++;

        if (update) instance.Discord_UpdatePresence(rpc);
        forceUpdate = false;

        instance.Discord_RunCallbacks();
    }
}
