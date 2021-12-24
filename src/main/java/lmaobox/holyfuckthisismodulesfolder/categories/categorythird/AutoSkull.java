package lmaobox.holyfuckthisismodulesfolder.categories.categorythird;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class AutoSkull extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder().name("auto-disable").description("disable auto skull after sending emoji.").defaultValue(true).build());

    public AutoSkull(){
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD, "auto-skull", "automatically sends one :skull: emoji in chat.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (autoDisable.get()) {
            toggle();
        }
    }

    @Override
    public void onActivate() {
        mc.player.sendChatMessage("☠");
    }
}
