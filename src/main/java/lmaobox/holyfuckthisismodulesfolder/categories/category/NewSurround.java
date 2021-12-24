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

public class NewSurround extends Module {
    private final SettingGroup sgPlace = settings.createGroup("Place");

    private final SettingGroup sgMods = settings.createGroup("Mods");

    private final SettingGroup sgToggle = settings.createGroup("Toggle Shit");

    private final SettingGroup sgMisc = settings.createGroup("Misc");

    private final SettingGroup sgSblock = settings.createGroup("Block Select");

    private final SettingGroup sgRender = settings.createGroup("Render");
//place
    private final Setting<Integer> blockPerTick = sgPlace.add(new IntSetting.Builder().name("place-tick").description("Block placements per tick.").defaultValue(4).min(1).sliderMax(10).build());
    private final Setting<Boolean> placeInside = sgPlace.add(new BoolSetting.Builder().name("place-on-crystal").description("Tries to place on a crystal if it's blocking your surround.").defaultValue(false).build());
    private final Setting<Boolean> groundOnly = sgPlace.add(new BoolSetting.Builder().name("place-on-ground").description("Only activate when you're on the ground.").defaultValue(true).build());
//toggle mods
    private final Setting<Boolean> disableAfter = sgToggle.add(new BoolSetting.Builder().name("toggle-after").description("Disable after the surround is complete.").defaultValue(false).build());
    private final Setting<Boolean> disableJump = sgToggle.add(new BoolSetting.Builder().name("toggle-on-jump").description("Disable if you jump.").defaultValue(true).build());
    private final Setting<Boolean> disableYchange = sgToggle.add(new BoolSetting.Builder().name("toggle-on-y-change").description("Disable if your Y coord changes.").defaultValue(true).build());
//block select
    private final Setting<List<Block>> blocks = sgSblock.add(new BlockListSetting.Builder().name("block").description("What blocks to use for surround.").defaultValue(Collections.singletonList(Blocks.OBSIDIAN)).filter(this::blockFilter).build());
//misc
    private final Setting<Boolean> centerPlayer = sgMisc.add(new BoolSetting.Builder().name("center-tp").description("Center you before starting the surround.").defaultValue(true).build());
    private final Setting<Boolean> rotation = sgMisc.add(new BoolSetting.Builder().name("rotate").description("").defaultValue(false).build());
//mods
    private final Setting<Boolean> useRus = sgMods.add(new BoolSetting.Builder().name("new-russian-surround").description("").defaultValue(false).build());
    private final Setting<Boolean> useYtwo = sgMods.add(new BoolSetting.Builder().name("double-mode").description("").defaultValue(false).build());
    private final Setting<Boolean> useEchests = sgMods.add(new BoolSetting.Builder().name("echests-mode").description("").defaultValue(false).build());
    private final Setting<Boolean> useProtect = sgMods.add(new BoolSetting.Builder().name("protect-mode").description("").defaultValue(true).build());
//render
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder().name("render").description("Renders where the surround will be placed.").defaultValue(true).build());
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder().name("side-color").description("The side color.").defaultValue(new SettingColor(0, 160, 255,100)).build());
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder().name("line-color").description("The line color.").defaultValue(new SettingColor(0, 160, 255)).build());

    private final ArrayList<Vec3d> surr = new ArrayList<Vec3d>() {{
        add(new Vec3d(1, 0, 0));
        add(new Vec3d(-1, 0, 0));
        add(new Vec3d(0, 0, 1));
        add(new Vec3d(0, 0, -1));
    }};

    private final ArrayList<Vec3d> surrRus = new ArrayList<Vec3d>() {{
        add(new Vec3d(-1, 0, -1));
        add(new Vec3d(-1, 0, 1));
        add(new Vec3d(1, 0, 1));
        add(new Vec3d(1, 0, -1));
        add(new Vec3d(2, 0, 0));
        add(new Vec3d(-2, 0, 0));
        add(new Vec3d(0, 0, 2));
        add(new Vec3d(0, 0, -2));
        add(new Vec3d(1, 0, 0));
        add(new Vec3d(-1, 0, 0));
        add(new Vec3d(0, 0, 1));
        add(new Vec3d(0, 0, -1));
    }};

    private final ArrayList<Vec3d> surrYtwo = new ArrayList<Vec3d>() {{
        add(new Vec3d(1, 1, 0));
        add(new Vec3d(-1, 1, 0));
        add(new Vec3d(0, 1, 1));
        add(new Vec3d(0, 1, -1));
    }};


    public NewSurround() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY, "new-surround", "obsidian you.");
    }

    @Override
    public void onActivate() {
        if (centerPlayer.get()) PlayerUtils.centerPlayer();
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        endChestMode();
        int bpt = 0;
        if ((disableJump.get() && (mc.options.keyJump.isPressed() || mc.player.input.jumping)) || (disableYchange.get() && mc.player.prevY < mc.player.getY())) { toggle(); return; }
        if (groundOnly.get() && !mc.player.isOnGround()) return;
        if (BlockHelper.isVecComplete(getSurrDesign())) {
            if (disableAfter.get()) {
                toggle();
            }
        } else {
            BlockPos ppos = mc.player.getBlockPos();
            for (Vec3d b : getSurrDesign()) {
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
        if (useProtect.get()) {
            for (Entity entity : mc.world.getEntities()) {
                if (entity instanceof EndCrystalEntity) {
                    BlockPos crystalPos = entity.getBlockPos();
                    if (isDangerousCrystal(crystalPos)) {
                        mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
                        mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                        return;
                    }
                }
            }
        }
    }

    private void endChestMode() {

        FindItemResult ech = InvUtils.findInHotbar(Items.ENDER_CHEST);

        if (useEchests.get()) {
            BlockUtils.place(mc.player.getBlockPos().add(1, -1, 0), ech, rotation.get(), 100, false);
            BlockUtils.place(mc.player.getBlockPos().add(-1, -1, 0), ech, rotation.get(), 100, false);
            BlockUtils.place(mc.player.getBlockPos().add(0, -1, 1), ech, rotation.get(), 100, false);
            BlockUtils.place(mc.player.getBlockPos().add(0, -1, -1), ech, rotation.get(), 100, false);
        }
    }

    private ArrayList<Vec3d> getSurrDesign() {
        ArrayList<Vec3d> surrDesign = new ArrayList<Vec3d>(surr);
        if (useRus.get()) surrDesign.addAll(surrRus);
        if (useYtwo.get()) surrDesign.addAll(surrYtwo);
        return surrDesign;
    }

    private boolean isDangerousCrystal(BlockPos bp) {
        BlockPos ppos = mc.player.getBlockPos();
        for (Vec3d b : getSurrDesign()) {
            BlockPos bb = ppos.add(b.x, b.y, b.z);
            if (!bp.equals(bb) && BlockHelper.distanceBetween(bb, bp) <= 2) return true;
        }
        return false;
    }


    private boolean blockFilter(Block block) {
        return block == Blocks.OBSIDIAN ||
                block == Blocks.CRYING_OBSIDIAN ||
                block == Blocks.NETHERITE_BLOCK ||
                block == Blocks.ENDER_CHEST ||
                block == Blocks.RESPAWN_ANCHOR;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (render.get()) {
            BlockPos ppos = mc.player.getBlockPos();
            for (Vec3d b: getSurrDesign()) {
                BlockPos bb = ppos.add(b.x, b.y, b.z);
                if (BlockHelper.getBlock(bb) == Blocks.AIR) event.renderer.box(bb, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            }
        }
    }
}

