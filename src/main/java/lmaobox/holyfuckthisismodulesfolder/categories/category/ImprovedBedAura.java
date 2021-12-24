package lmaobox.holyfuckthisismodulesfolder.categories.category;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import lmaobox.nomodulesfolder.Wrapper;
import lmaobox.nomodulesfolder.world.BlockHelper;
import lmaobox.nomodulesfolder.misc.Stats;
import lmaobox.nomodulesfolder.player.AutomationUtils;
import lmaobox.nomodulesfolder.player.BedUtils;
import lmaobox.nomodulesfolder.player.ItemHelper;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.CrystalAura;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.entity.SortPriority;
import meteordevelopment.meteorclient.utils.entity.TargetUtils;
import meteordevelopment.meteorclient.utils.player.*;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.meteorclient.utils.world.CardinalDirection;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.Item;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class ImprovedBedAura extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgDamage = settings.createGroup("Damage");
    private final SettingGroup sgSuicide = settings.createGroup("Suicide");
    private final SettingGroup sgPause = settings.createGroup("Pause");
    private final SettingGroup sgRender = settings.createGroup("Render");
    private final SettingGroup sgTest = settings.createGroup("Test");

    // General
    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder().name("place-speed").description("").defaultValue(7).min(0).sliderMax(15).build());
    private final Setting<Double> targetRange = sgGeneral.add(new DoubleSetting.Builder().name("place-range").description("").defaultValue(6).min(0).sliderMax(7).build());
    private final Setting<BreakHand> breakHand = sgGeneral.add(new EnumSetting.Builder<BreakHand>().name("break-hand").description("").defaultValue(BreakHand.Offhand).build());
    private final Setting<SortPriority> priority = sgGeneral.add(new EnumSetting.Builder<SortPriority>().name("priority-mode").description("Targed priorities.").defaultValue(SortPriority.LowestHealth).build());
    private final Setting<Boolean> autoSwitch = sgGeneral.add(new BoolSetting.Builder().name("auto-switch").description("").defaultValue(true).build());

    // Damage
    private final Setting<Double> minDamage = sgDamage.add(new DoubleSetting.Builder().name("min-damage").description("").defaultValue(8).min(0).max(36).sliderMax(36).build());
    private final Setting<Double> maxSelfDamage = sgDamage.add(new DoubleSetting.Builder().name("min-self-damage").description("").defaultValue(8).min(0).max(36).sliderMax(36).build());
    private final Setting<Double> safetyHP = sgDamage.add(new DoubleSetting.Builder().name("min-safety").description("What health safety activates at.").defaultValue(10).min(0).max(36).sliderMax(36).build());
    // Suicide
    private final Setting<Boolean> antiSuicide = sgSuicide.add(new BoolSetting.Builder().name("anti-suicide").description("Will not place and break beds if they will kill you.").defaultValue(true).build());

    // Pause
    private final Setting<Boolean> pauseOnCraft = sgPause.add(new BoolSetting.Builder().name("pause-on-crafting").description("Pauses while crafting.").defaultValue(false).build());
    private final Setting<Boolean> pauseOnEat = sgPause.add(new BoolSetting.Builder().name("pause-on-eat").description("Pauses while eating.").defaultValue(true).build());
    private final Setting<Boolean> pauseOnDrink = sgPause.add(new BoolSetting.Builder().name("pause-on-drink").description("Pauses while drinking.").defaultValue(true).build());
    private final Setting<Boolean> pauseOnMine = sgPause.add(new BoolSetting.Builder().name("pause-on-mine").description("Pauses while mining.").defaultValue(true).build());
    private final Setting<Boolean> pauseOnCa = sgPause.add(new BoolSetting.Builder().name("pause-on-crystal-aura").description("Pause while Crystal Aura is active.").defaultValue(false).build());

    // Render
    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder().name("render").description("Renders the block where it is placing a bed.").defaultValue(true).build());
    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>().name("shape-mode").description("How the shapes are rendered.").defaultValue(ShapeMode.Both).build());
    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder().name("side-color").description("The side color for positions to be placed.").defaultValue(new SettingColor(255, 255, 255,100)).build());
    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder().name("line-color").description("The line color for positions to be placed.").defaultValue(new SettingColor(255, 255, 255)).build());


    // pure

    private final Setting<Boolean> strictDirection = sgTest.add(new BoolSetting.Builder().name("").description("Only places beds in the direction you are facing.").defaultValue(false).build());


    // Inventory
    private final Setting<Boolean> autoMove = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Integer> autoMoveSlot = sgTest.add(new IntSetting.Builder().name("").description("").defaultValue(9).min(1).max(9).sliderMin(1).sliderMax(9).visible(autoMove::get).build());
    private final Setting<Boolean> restoreOnDisable = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());

    // Automation
    private final Setting<Boolean> breakSelfTrap = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> breakBurrow = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> breakWeb = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> preventEscape = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> renderAutomation = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> disableOnNoBeds = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());

    // Safety
    private final Setting<Boolean> disableOnSafety = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());
    private final Setting<Boolean> safetyGapSwap = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());


    // Render
    private final Setting<Boolean> swing = sgTest.add(new BoolSetting.Builder().name("").description("").defaultValue(false).build());

    private CardinalDirection direction;
    private PlayerEntity target;
    private BlockPos placePos, breakPos, stb;
    private int timer, webTimer;
    private Item ogItem;
    private boolean sentTrapMine, sentBurrowMine, safetyToggled;


    public ImprovedBedAura() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY, "Improved-BedAura", "");
    }

    @Override
    public void onActivate() {
        target = null;
        ogItem = Wrapper.getItemFromSlot(autoMoveSlot.get() - 1);
        if (ogItem instanceof BedItem) ogItem = null; //ignore if we already have a bed there.
        safetyToggled = false;
        sentTrapMine = false;
        sentBurrowMine = false;
        timer = 0;
        webTimer = 0;
        direction = CardinalDirection.North;
        stb = null;
    }

    @Override
    public void onDeactivate() {
        if (safetyToggled) {
            warning("Your health is too low!");
            if (safetyGapSwap.get()) {
                FindItemResult gap = ItemHelper.findEgap();
                if (gap.found()) mc.player.getInventory().selectedSlot = gap.getSlot();
            }
        }
        if (!safetyToggled && restoreOnDisable.get() && ogItem != null) {
            FindItemResult ogItemInv = InvUtils.find(ogItem);
            if (ogItemInv.found()) InvUtils.move().from(ogItemInv.getSlot()).toHotbar(autoMoveSlot.get() - 1);
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        CrystalAura ca = Modules.get().get(CrystalAura.class);
        if (PlayerUtils.getTotalHealth() <= safetyHP.get()) {
            if (disableOnSafety.get()) {
                safetyToggled = true;
                toggle();
            }
            return;
        }
        if (mc.world.getDimension().isBedWorking()) { error( "Beds don't work in the overworld!"); toggle(); return; }

        if (PlayerUtils.shouldPause(pauseOnMine.get(), pauseOnEat.get(), pauseOnDrink.get())) return;
        if (pauseOnCraft.get() && mc.player.currentScreenHandler instanceof CraftingScreenHandler) return;
        if (pauseOnCa.get() && ca.isActive()) return;

        target = TargetUtils.getPlayerTarget(targetRange.get(), priority.get());
        if (TargetUtils.isBadTarget(target, targetRange.get())) target = null;
        if (target == null) { timer = delay.get(); placePos = null; breakPos = null; stb = null; sentTrapMine = false; sentBurrowMine = false; return; }

        // Auto move
        if (autoMove.get()) {
            FindItemResult bed = InvUtils.find(itemStack -> itemStack.getItem() instanceof BedItem);
            if (bed.found() && bed.getSlot() != autoMoveSlot.get() - 1) { InvUtils.move().from(bed.getSlot()).toHotbar(autoMoveSlot.get() - 1); }
            if (!bed.found() && disableOnNoBeds.get()) { warning("You've run out of beds! Disabling."); toggle(); return; }
        }

        if (preventEscape.get() && BlockHelper.getBlock(target.getBlockPos().up(2)) != Blocks.OBSIDIAN && Wrapper.isInHole(target)) {
            FindItemResult obby = ItemHelper.findObby();
            if (obby.found()) {BlockUtils.place(target.getBlockPos().up(2), obby, true, 50, true, true, true);}
            if (BlockHelper.getBlock(target.getBlockPos().up(2)) != Blocks.OBSIDIAN) return;
        }

        if (breakPos == null) { placePos = findPlace(target); }

        //Automation
        if (breakSelfTrap.get() && shouldTrapMine()) {
            FindItemResult pick = ItemHelper.findPick();
            if (pick.found()) {
                Wrapper.updateSlot(pick.getSlot());
                info("Breaking " + target.getEntityName() + "'s self-trap.");
                stb = BedUtils.getSelfTrapBlock(target, preventEscape.get());
                AutomationUtils.doPacketMine(stb);
                sentTrapMine = true;
                return;
            }
        }
        if (placePos == null && AutomationUtils.isBurrowed(target, false) && breakBurrow.get() && !sentBurrowMine) {
            FindItemResult pick = ItemHelper.findPick();
            if (pick.found()) {
                Wrapper.updateSlot(pick.getSlot());
                info("Breaking " + target.getEntityName() + "'s burrow.");
                AutomationUtils.doPacketMine(target.getBlockPos());
                sentBurrowMine = true;
                return;
            }
        }
        if (placePos == null && AutomationUtils.isWebbed(target) && breakWeb.get()) {
            FindItemResult sword = ItemHelper.findSword();
            if (sword.found()) {
                Wrapper.updateSlot(sword.getSlot());
                if (webTimer <= 0) {
                    info("Breaking " + target.getEntityName() + "'s web.");
                    webTimer = 100;
                } else {
                    webTimer--;
                }
                AutomationUtils.mineWeb(target, sword.getSlot());
                return;
            }
        }

        if (sentTrapMine && didTrapMine()) { sentTrapMine = false; stb = null; }
        if (sentBurrowMine && !AutomationUtils.isBurrowed(target, false)) sentBurrowMine = false;

        // Place bed
        if (timer <= 0 && placeBed(placePos)) { timer = delay.get(); }
        else { timer--; }

        if (breakPos == null) breakPos = findBreak();
        breakBed(breakPos);
    }

    private BlockPos findPlace(PlayerEntity target) {
        if (!InvUtils.find(itemStack -> itemStack.getItem() instanceof BedItem).found()) return null;

        for (int index = 0; index < 3; index++) {
            int i = index == 0 ? 1 : index == 1 ? 0 : 2;

            for (CardinalDirection dir : CardinalDirection.values()) {
                if (strictDirection.get()
                        && dir.toDirection() != mc.player.getHorizontalFacing()
                        && dir.toDirection().getOpposite() != mc.player.getHorizontalFacing()) continue;

                BlockPos centerPos = target.getBlockPos().up(i);

                double headSelfDamage = DamageUtils.bedDamage(mc.player, Utils.vec3d(centerPos));
                double offsetSelfDamage = DamageUtils.bedDamage(mc.player, Utils.vec3d(centerPos.offset(dir.toDirection())));

                if (mc.world.getBlockState(centerPos).getMaterial().isReplaceable()
                        && BlockUtils.canPlace(centerPos.offset(dir.toDirection()))
                        && DamageUtils.bedDamage(target, Utils.vec3d(centerPos)) >= minDamage.get()
                        && offsetSelfDamage < maxSelfDamage.get()
                        && headSelfDamage < maxSelfDamage.get()
                        && (!antiSuicide.get() || PlayerUtils.getTotalHealth() - headSelfDamage > 0)
                        && (!antiSuicide.get() || PlayerUtils.getTotalHealth() - offsetSelfDamage > 0)) {
                    return centerPos.offset((direction = dir).toDirection());
                }
            }
        }

        return null;
    }

    private BlockPos findBreak() {
        for (BlockEntity blockEntity : Utils.blockEntities()) {
            if (!(blockEntity instanceof BedBlockEntity)) continue;
            BlockPos bedPos = blockEntity.getPos();
            Vec3d bedVec = Utils.vec3d(bedPos);
            if (PlayerUtils.distanceTo(bedVec) <= mc.interactionManager.getReachDistance()
                    && DamageUtils.bedDamage(target, bedVec) >= minDamage.get()
                    && DamageUtils.bedDamage(mc.player, bedVec) < maxSelfDamage.get()
                    && (!antiSuicide.get() || PlayerUtils.getTotalHealth() - DamageUtils.bedDamage(mc.player, bedVec) > 0)) {
                return bedPos;
            }
        }
        return null;
    }

    private boolean placeBed(BlockPos pos) {
        if (pos == null) return false;
        FindItemResult bed = InvUtils.findInHotbar(itemStack -> itemStack.getItem() instanceof BedItem);
        if (bed.getHand() == null && !autoSwitch.get()) return false;
        double yaw = switch (direction) {
            case East -> 90;
            case South -> 180;
            case West -> -90;
            default -> 0;
        };
        Rotations.rotate(yaw, Rotations.getPitch(pos), () -> {
            BlockUtils.place(pos, bed, false, 0, swing.get(), true);
            breakPos = pos;
        });
        return true;
    }

    private void breakBed(BlockPos pos) {
        if (pos == null) return;
        breakPos = null;
        if (!(mc.world.getBlockState(pos).getBlock() instanceof BedBlock)) return;
        boolean wasSneaking = mc.player.isSneaking();
        if (wasSneaking) mc.player.setSneaking(false);
        Hand bHand;
        if (breakHand.get() == BreakHand.Mainhand) { bHand = Hand.MAIN_HAND;
        } else { bHand = Hand.OFF_HAND; }
        mc.interactionManager.interactBlock(mc.player, mc.world, bHand, new BlockHitResult(mc.player.getPos(), Direction.UP, pos, false));
        mc.player.setSneaking(wasSneaking);
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (render.get() && placePos != null && breakPos == null) {
            int x = placePos.getX();
            int y = placePos.getY();
            int z = placePos.getZ();

            switch (direction) {
                case North -> event.renderer.box(x, y, z, x + 1, y + 0.6, z + 2, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
                case South -> event.renderer.box(x, y, z - 1, x + 1, y + 0.6, z + 1, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
                case East -> event.renderer.box(x - 1, y, z, x + 1, y + 0.6, z + 1, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
                case West -> event.renderer.box(x, y, z, x + 2, y + 0.6, z + 1, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            }
        }
        if (renderAutomation.get() && target != null) {
            if (stb != null) event.renderer.box(stb, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            if (sentBurrowMine) event.renderer.box(target.getBlockPos(), sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            if (AutomationUtils.isWeb(target.getBlockPos())) event.renderer.box(target.getBlockPos(), sideColor.get(), lineColor.get(), shapeMode.get(), 0);
            if (AutomationUtils.isWeb(target.getBlockPos().up())) event.renderer.box(target.getBlockPos().up(), sideColor.get(), lineColor.get(), shapeMode.get(), 0);
        }
    }

    private boolean shouldTrapMine() {
        return !sentTrapMine && placePos == null && BedUtils.getSelfTrapBlock(target, preventEscape.get()) != null;
    }

    private boolean didTrapMine() {
        if (BedUtils.getSelfTrapBlock(target, preventEscape.get()) == null) return true;
        return BlockHelper.getBlock(stb) == Blocks.AIR || !AutomationUtils.isTrapBlock(stb);
    }

    @Override
    public String getInfoString() {
        return EntityUtils.getName(target);
    }

    public enum BreakHand {
        Mainhand,
        Offhand
    }
}
