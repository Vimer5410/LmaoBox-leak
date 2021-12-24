package lmaobox.holyfuckthisismodulesfolder.categories.category;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.*;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;

import net.minecraft.item.Items;

public class AntiBedAura extends Module {

    public enum Mode {
        TRIPWIRE,
        RSTONE
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>().name("mode").description("the mode to use for anti bed aura.").defaultValue(Mode.TRIPWIRE).build());

    private final Setting<Boolean> doubles = sgGeneral.add(new BoolSetting.Builder().name("triple-mode").description("the triple mode.").defaultValue(true).build());

    private final Setting<Boolean> disableJump = sgGeneral.add(new BoolSetting.Builder().name("turn-off-jump").description("disable when jump.").defaultValue(true).build());

    private final Setting<Boolean> disableYaxis = sgGeneral.add(new BoolSetting.Builder().name("turn-off-moving-Y").description("disable when moving Y-axis.").defaultValue(true).build());

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder().name("rotate").description("simple rotate mode.").defaultValue(true).build());

    FindItemResult tripW;

    public AntiBedAura() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY, "no-ajajAura", "places tripwire or redstone on you.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        switch (mode.get()) {
            case TRIPWIRE:
                placeTRIPWIRE();
                break;
            case RSTONE:
                placeREDSTONE();
                break;
        }
    }

    private void placeTRIPWIRE() {

        FindItemResult tw = InvUtils.findInHotbar(Items.STRING);

        BlockUtils.place(mc.player.getBlockPos(), tw, rotate.get(), 0, false);
        BlockUtils.place(mc.player.getBlockPos().add(0, 1, 0), tw, rotate.get(), 0, false);


        if (doubles.get()) {
            BlockUtils.place(mc.player.getBlockPos().add(0, 2, 0), tw, rotate.get(), 0, false);
        }
    }

    private void placeREDSTONE() {

        FindItemResult rs = InvUtils.findInHotbar(Items.REDSTONE);

        BlockUtils.place(mc.player.getBlockPos(), rs, rotate.get(), 0, false);
        BlockUtils.place(mc.player.getBlockPos().add(0, 1, 0), rs, rotate.get(), 0, false);


        if (doubles.get()) {
            BlockUtils.place(mc.player.getBlockPos().add(0, 2, 0), rs, rotate.get(), 0, false);

        }
    }

}
