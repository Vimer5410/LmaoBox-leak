// 
// Decompiled by Procyon v0.5.36
// 

package lmaobox.holyfuckthisismodulesfolder.categories.categorysecond;

import meteordevelopment.meteorclient.mixin.AbstractBlockAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.util.math.MathHelper;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import net.minecraft.util.math.BlockPos;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;

public class ImprovedAnchor extends Module
{
    private final SettingGroup sgGeneral;
    private final Setting<Integer> maxHeight;
    private final Setting<Integer> minPitch;
    private final Setting<Boolean> cancelMove;
    private final Setting<Boolean> pull;
    private final Setting<Double> pullSpeed;
    private final Setting<Boolean> whileJumping;
    private final BlockPos.Mutable blockPos;
    private boolean wasInHole;
    private boolean foundHole;
    private int holeX;
    private int holeZ;
    public boolean cancelJump;
    public boolean controlMovement;
    public double deltaX;
    public double deltaZ;
    
    public ImprovedAnchor() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYSECOND, "Improved-Anchor", "Anchor");
        this.sgGeneral = this.settings.getDefaultGroup();
        this.maxHeight = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("max-height").description("The maximum height Anchor will work at.").defaultValue(10).min(0).max(255).sliderMax(20).build());
        this.minPitch = (Setting<Integer>)this.sgGeneral.add((Setting)new IntSetting.Builder().name("min-pitch").description("The minimum pitch at which anchor will work.").defaultValue(-90).min(-90).max(90).sliderMin(-90).sliderMax(90).build());
        this.cancelMove = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("cancel-jump-in-hole").description("Prevents you from jumping when Anchor is active and Min Pitch is met.").defaultValue(false).build());
        this.pull = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("pull").description("The pull strength of Anchor.").defaultValue(false).build());
        this.pullSpeed = (Setting<Double>)this.sgGeneral.add((Setting)new DoubleSetting.Builder().name("pull-speed").description("How fast to pull towards the hole in blocks per second.").defaultValue(0.3).min(0.0).sliderMax(5.0).build());
        this.whileJumping = (Setting<Boolean>)this.sgGeneral.add((Setting)new BoolSetting.Builder().name("while-jumping").description("Should anchor be active while jump is held.").defaultValue(true).build());
        this.blockPos = new BlockPos.Mutable();
    }
    
    public void onActivate() {
        this.wasInHole = false;
        final int n = 0;
        this.holeZ = n;
        this.holeX = n;
    }
    
    @EventHandler
    private void onPreTick(final TickEvent.Pre event) {
        cancelJump = foundHole && cancelMove.get() && mc.player.getPitch() >= minPitch.get();
    }
    
    @EventHandler
    private void onPostTick(final TickEvent.Post event) {
        if (!(boolean)this.whileJumping.get() && this.mc.options.keyJump.isPressed()) {
            return;
        }
        this.controlMovement = false;
        final int x = MathHelper.floor(this.mc.player.getX());
        int y = MathHelper.floor(this.mc.player.getY());
        final int z = MathHelper.floor(this.mc.player.getZ());
        if (this.isHole(x, y, z)) {
            this.wasInHole = true;
            this.holeX = x;
            this.holeZ = z;
            return;
        }
        if (this.wasInHole && this.holeX == x && this.holeZ == z) {
            return;
        }
        if (this.wasInHole) {
            this.wasInHole = false;
        }
        if (mc.player.getPitch() < minPitch.get()) {
            return;
        }
        this.foundHole = false;
        double holeX = 0.0;
        double holeZ = 0.0;
        for (int i = 0; i < (int)this.maxHeight.get() && --y > 0; ++i) {
            if (!this.isAir(x, y, z)) {
                break;
            }
            if (this.isHole(x, y, z)) {
                this.foundHole = true;
                holeX = x + 0.5;
                holeZ = z + 0.5;
                break;
            }
        }
        if (this.foundHole) {
            this.controlMovement = true;
            this.deltaX = Utils.clamp(holeX - this.mc.player.getX(), -0.05, 0.05);
            this.deltaZ = Utils.clamp(holeZ - this.mc.player.getZ(), -0.05, 0.05);
            ((IVec3d)this.mc.player.getVelocity()).set(this.deltaX, this.mc.player.getVelocity().y - (this.pull.get() ? this.pullSpeed.get() : 0.0), this.deltaZ);
        }
    }
    
    private boolean isHole(final int x, final int y, final int z) {
        return this.isHoleBlock(x, y - 1, z) && this.isHoleBlock(x + 1, y, z) && this.isHoleBlock(x - 1, y, z) && this.isHoleBlock(x, y, z + 1) && this.isHoleBlock(x, y, z - 1);
    }
    
    private boolean isHoleBlock(final int x, final int y, final int z) {
        this.blockPos.set(x, y, z);
        final Block block = this.mc.world.getBlockState((BlockPos)this.blockPos).getBlock();
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.RESPAWN_ANCHOR || block == Blocks.ANCIENT_DEBRIS || block == Blocks.CRYING_OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.NETHERITE_BLOCK || block == Blocks.ANVIL || block == Blocks.DAMAGED_ANVIL || block == Blocks.CHIPPED_ANVIL;
    }
    
    private boolean isAir(final int x, final int y, final int z) {
        this.blockPos.set(x, y, z);
        return !((AbstractBlockAccessor)this.mc.world.getBlockState((BlockPos)this.blockPos).getBlock()).isCollidable();
    }
}
