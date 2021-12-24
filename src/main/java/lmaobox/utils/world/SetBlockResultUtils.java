package lmaobox.utils.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Hand;
import net.minecraft.block.Block;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import meteordevelopment.meteorclient.utils.player.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.Items;
import net.minecraft.item.BlockItem;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;


public class SetBlockResultUtils {

    public static MinecraftClient mc;

    private static final SetBlockResult RESULT = new SetBlockResult();

    public static class SetBlockResult {
        private static int slot = -1;
        private static BlockPos pos = null;
        private static Direction direct = Direction.DOWN;
        private static boolean rotate = false;
        private static boolean noBack = false;
        private static boolean packet = false;
        private static boolean noCheck = false;
        private static Hand hand = Hand.MAIN_HAND;

        public SetBlockResult POS(BlockPos s) {
            pos = s;
            return this;
        }

        public SetBlockResult DIRECTION(Direction s) {
            direct = s;
            return this;
        }

        public SetBlockResult ROTATE(boolean s) {
            rotate = s;
            return this;
        }

        public SetBlockResult XYZ(int x, int y, int z) {
            pos = new BlockPos(x, y, z);
            return this;
        }

        public SetBlockResult RELATIVE_XYZ(int x, int y, int z) {
            pos = new BlockPos(mc.player.getBlockPos().getX() + x, mc.player.getBlockPos().getY() + y, mc.player.getBlockPos().getZ() + z);
            return this;
        }

        public SetBlockResult NOBACK() {
            noBack = true;
            return this;
        }

        public SetBlockResult PACKET(boolean s) {
            packet = s;
            return this;
        }

        public SetBlockResult SLOT(int slot) {
            this.slot = slot;
            return this;
        }

        public SetBlockResult INDEX_SLOT(int s) {
            slot = invIndexToSlotId(s);
            return this;
        }

        public SetBlockResult NOCHECK() {
            noCheck = true;
            return this;
        }

        public SetBlockResult HAND(Hand hand) {
            this.hand = hand;
            return this;
        }

        private void reset() {
            slot = -1;
            pos = null;
            direct = Direction.DOWN;
            rotate = false;
            noBack = false;
            packet = false;
            noCheck = false;
        }

        public boolean S() {
            if (pos == null
                || slot == -1
                || mc.player.getInventory().getStack(slot).isEmpty()
                || !(mc.player.getInventory().getStack(slot).getItem() instanceof BlockItem)) {
                reset();
                return false;
            }


            if (!noCheck && !BlockUtils.canPlace(pos, true)) {
                reset();
                return false;
            }

            Block block = ((BlockItem) mc.player.getInventory().getStack(slot).getItem()).getBlock();
            if(!block.canPlaceAt(block.getDefaultState(), mc.world, pos)) {
                reset();
                return false;
            }

            int PreSlot = mc.player.getInventory().selectedSlot;
            swap(slot);

            if (rotate) {
                Vec3d hitPos = new Vec3d(0, 0, 0);
                ((IVec3d) hitPos).set(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                Rotations.rotate(Rotations.getYaw(hitPos), Rotations.getPitch(hitPos));
            }

            BlockHitResult hitresult = new BlockHitResult(mc.player.getPos(), direct, pos, true);

            if (packet) mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(hand, hitresult));
            else mc.interactionManager.interactBlock(mc.player, mc.world, hand, hitresult);

            if (!noBack) swap(PreSlot);

            reset();

            return true;
        }

    }

    public static SetBlockResult setBlock() {
        return RESULT;
    }


    public static int invIndexToSlotId(int invIndex) {
        if (invIndex < 9 && invIndex != -1) return 44 - (8 - invIndex);
        return invIndex;
    }

    public static void swap(int s) {
        if (s != mc.player.getInventory().selectedSlot && s >= 0 && s < 9) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(s));
            mc.player.getInventory().selectedSlot = s;
        }
    }
}
