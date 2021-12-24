package lmaobox.holyfuckthisismodulesfolder.categories.categorythird;

import meteordevelopment.meteorclient.events.world.TickEvent;
import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import lmaobox.utils.world.SetBlockResultUtils;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class AutoBuildPortal extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> disableOnMove = sgGeneral.add(new BoolSetting.Builder()
            .name("disable-on-move")
            .description("automatically disables when you move.")
            .defaultValue(true)
            .build()
    );

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
            .name("rotate")
            .description("automatically faces towards the obsidian being placed.")
            .defaultValue(false)
            .build()
    );


    public AutoBuildPortal() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD, "auto-build-portal", "automaticaly build nether portal.");
    }


	byte[] x,z;
    BlockPos ppos = null;

    @SuppressWarnings("incomplete-switch")
	@Override
    public void onActivate() {
    	ppos = mc.player.getBlockPos();
    	byte[] v1 = {2,2, 2,2, 2,2, 2,2, 2,2, 2};
    	byte[] v_1 = {-2,-2, -2,-2, -2,-2, -2,-2, -2,-2, -2};
    	byte[] vf = {0,-1, 1,-2, 1,-2, 1,-2, 0,-1, 0};

    	switch (mc.player.getHorizontalFacing()) {
			case EAST:
				x = v1;
				z = vf;
				break;
			case SOUTH:
				x = vf;
				z = v1;
				break;
			case WEST:
				x = v_1;
				z = vf;
				break;
			case NORTH:
				x = vf;
				z = v_1;
				break;
			}

    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
    	if (disableOnMove.get() && ppos != mc.player.getBlockPos()) {
            toggle();
            return;
        }



	    int fire = InvUtils.findInHotbar(Items.FLINT_AND_STEEL, Items.FIRE_CHARGE).getSlot();
	    if(fire == -1){
	    	error("No flint and steel in hotbar!");
	    	toggle();
	    	return;
	    }


        if(p(x[0],  0, z[0])) return;
        if(p(x[1],  0, z[1])) return;
        if(p(x[2],  1, z[2])) return;
        if(p(x[3],  1, z[3])) return;
        if(p(x[4],  2, z[4])) return;
        if(p(x[5],  2, z[5])) return;
        if(p(x[6],  3, z[6])) return;
        if(p(x[7],  3, z[7])) return;
        if(p(x[8],  4, z[8])) return;
        if(p(x[9],  4, z[9])) return;




        BlockPos pos = new BlockPos(mc.player.getX()+x[10], mc.player.getY()+1, mc.player.getZ()+z[10]);
        int preSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = fire;
        mc.interactionManager.interactBlock(mc.player, mc.world, Hand.MAIN_HAND,
          		new BlockHitResult(mc.player.getPos(), Direction.UP, pos, true));
        mc.player.getInventory().selectedSlot = preSlot;

        toggle();


    };

    private boolean p(int x, int y, int z) {

        int slot = InvUtils.findInHotbar(Items.OBSIDIAN).getSlot();
        if(slot==-1){
	    	warning("No obsidian in hotbar!");
	    	toggle();
	    	sendToggledMsg();
	    	return true;
        }


        return SetBlockResultUtils.setBlock().RELATIVE_XYZ(x, y, z).SLOT(slot).ROTATE(rotate.get()).S() ? true : false;
    }



}
