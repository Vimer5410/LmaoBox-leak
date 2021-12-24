package lmaobox.holyfuckthisismodulesfolder.categories.category;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import lmaobox.nomodulesfolder.world.BlockHelper;
import net.minecraft.block.Block;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewSelfTrap extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final SettingGroup sgMods = settings.createGroup("Mods");

    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final SettingGroup sgSblock = settings.createGroup("Block Select");

    private final SettingGroup sgRender = settings.createGroup("Render");
    //place
    private final Setting<Integer> blockPerTick = sgGeneral.add(new IntSetting.Builder().name("place-tick").description("").defaultValue(1).min(0).sliderMax(10).build());
    private final Setting<Boolean> placeInside = sgGeneral.add(new BoolSetting.Builder().name("place-on-crystal").description("").defaultValue(false).build());
    private final Setting<Boolean> groundOnly = sgGeneral.add(new BoolSetting.Builder().name("place-on-ground").description("").defaultValue(true).build());
    private final Setting<Boolean> disableAfter = sgGeneral.add(new BoolSetting.Builder().name("toggle-after").description("").defaultValue(false).build());

    //mods
    private final Setting<Boolean> usePlus = sgMods.add(new BoolSetting.Builder().name("plus-trap-mode").description("").defaultValue(false).build());
    //misc
    private final Setting<Boolean> centerPlayer = sgMisc.add(new BoolSetting.Builder().name("center-tp").description("").defaultValue(true).build());
    private final Setting<Boolean> rotation = sgMisc.add(new BoolSetting.Builder().name("rotate").description("").defaultValue(false).build());
    //block select
    private final Setting<List<Block>> blocks = sgSblock.add(new BlockListSetting.Builder().name("block").description("").defaultValue(Collections.singletonList(Blocks.OBSIDIAN)).filter(this::blockFilter).build());
    //render
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder().name("render").description("Renders where the surround will be placed.").defaultValue(true).build());
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder().name("side-color").description("The side color.").defaultValue(new SettingColor(0, 160, 255, 100)).build());
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder().name("line-color").description("The line color.").defaultValue(new SettingColor(0, 160, 255)).build());

    private final ArrayList<Vec3d> trap = new ArrayList<Vec3d>() {{
        add(new Vec3d(1, 0, 0));
        add(new Vec3d(-1, 0, 0));
        add(new Vec3d(0, 0, 1));
        add(new Vec3d(0, 0, -1));
        add(new Vec3d(1, 1, 0));
        add(new Vec3d(-1, 1, 0));
        add(new Vec3d(0, 1, 1));
        add(new Vec3d(0, 1, -1));
        add(new Vec3d(0, 2, 0));
    }};

    private final ArrayList<Vec3d> trapPlus = new ArrayList<Vec3d>() {{
        add(new Vec3d(0, 3, 0));
    }};

    public NewSelfTrap() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY, "new-self-trap", "new surround :)))w");
    }

    @Override
    public void onActivate() {
        if (centerPlayer.get()) PlayerUtils.centerPlayer();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        int bpt = 0;
        if (mc.options.keyJump.isPressed()) { toggle(); return; }
        if (groundOnly.get() && !mc.player.isOnGround()) return;
        if (BlockHelper.isVecComplete(getSimpleTrap())) {
            if (disableAfter.get()) {
                toggle();
            }
        } else {
            BlockPos ppos = mc.player.getBlockPos();
            for (Vec3d b : getSimpleTrap()) {
                if (bpt >= blockPerTick.get()) return;
                BlockPos bb = ppos.add(b.x, b.y, b.z);
                if (BlockHelper.getBlock(bb) == Blocks.AIR) {
                    if (placeInside.get()) {
                        BlockUtils.place(bb, InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem()))), rotation.get(), 100, false);
                    } else {
                        BlockUtils.place(bb, InvUtils.findInHotbar(itemStack -> blocks.get().contains(Block.getBlockFromItem(itemStack.getItem()))), rotation.get(), 100, true);
                    }
                    bpt++;
                }
            }
        }
    }

    private ArrayList<Vec3d> getSimpleTrap() {
        ArrayList<Vec3d> simpleTrap = new ArrayList<Vec3d>(trap);
        if (usePlus.get()) simpleTrap.addAll(trapPlus);
        return simpleTrap;
    }

    private boolean isDangerousCrystal(BlockPos bp) {
        BlockPos ppos = mc.player.getBlockPos();
        for (Vec3d b : getSimpleTrap()) {
            BlockPos bb = ppos.add(b.x, b.y, b.z);
            if (!bp.equals(bb) && BlockHelper.distanceBetween(bb, bp) <= 2) return true;
        }
        return false;
    }


    private boolean blockFilter(Block block) {
        return block == Blocks.BEDROCK ||
                block == Blocks.BARRIER ||
                block == Blocks.COMMAND_BLOCK ||
                block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.NETHERITE_BLOCK ||
                block == Blocks.RESPAWN_ANCHOR;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (render.get()) {
            BlockPos ppos = mc.player.getBlockPos();
            for (Vec3d b: getSimpleTrap()) {
                BlockPos bb = ppos.add(b.x, b.y, b.z);
                if (BlockHelper.getBlock(bb) == Blocks.AIR) event.renderer.box(bb, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            }
        }
    }
}

