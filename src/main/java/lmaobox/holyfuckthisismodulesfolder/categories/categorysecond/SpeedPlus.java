package lmaobox.holyfuckthisismodulesfolder.categories.categorysecond;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.events.entity.player.JumpVelocityMultiplierEvent;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.world.Timer;
import meteordevelopment.orbit.EventHandler;

import net.minecraft.entity.MovementType;
import net.minecraft.client.option.KeyBinding;

import java.util.Comparator;
import java.util.Optional;

public class SpeedPlus extends Module {

    public enum JumpWhen {
        Sprinting,
        Walking,
        Always
    }

    public enum Mode {
        Jump,
        LowHop
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgAutoMoveGroup = settings.createGroup("Auto Move Settings");
    private final SettingGroup sgAutoJumpGroup = settings.createGroup("Auto Jump Settings");

    private final Setting<Double> multiplier = sgGeneral.add(new DoubleSetting.Builder()
        .name("Y-height")
        .description("Y-axis height value.")
        .defaultValue(0.3)
        .min(0)
        .build()
    );

    public final Setting<Double> timer = sgGeneral.add(new DoubleSetting.Builder()
        .name("timer-value")
        .description("the timer multiplier amount.")
        .defaultValue(1.2)
        .min(0.01)
        .sliderMin(0.01)
        .sliderMax(10)
        .build()
    );

    private final Setting<Boolean> WalkForwardEnable = sgAutoMoveGroup.add(new BoolSetting.Builder()
        .name("enable-auto-walk")
        .description("walk key (on).")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> SprintEnable = sgAutoMoveGroup.add(new BoolSetting.Builder()
        .name("enable-sprint")
        .description("sprint key (on).")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> StepEnable = sgAutoMoveGroup.add(new BoolSetting.Builder()
        .name("enable-step")
        .description("enable step, okey")
        .defaultValue(true)
        .build()
    );

    private final Setting<Mode> AutoJumpMode = sgAutoJumpGroup.add(new EnumSetting.Builder<Mode>()
        .name("jump-mode")
        .description("the method of jumping.")
        .defaultValue(Mode.Jump)
        .build()
    );

    private final Setting<JumpWhen> AutoJumpIf = sgAutoJumpGroup.add(new EnumSetting.Builder<JumpWhen>()
        .name("jump-if")
        .description("jump if.")
        .defaultValue(JumpWhen.Always)
        .build()
    );

    public SpeedPlus() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYSECOND, "Speed-+", "new speed module.");
    }
// auto jump mods
    private boolean jump() {
        switch (AutoJumpIf.get()) {
            case Sprinting:
                return mc.player.isSprinting() && (mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0);
            case Walking:
                return mc.player.forwardSpeed != 0 || mc.player.sidewaysSpeed != 0;
            case Always:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDeactivate() {
        Modules.get().get(Timer.class).setOverride(Timer.OFF);
        if (WalkForwardEnable.get()) unpress();
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {

        Modules.get().get(Timer.class).setOverride(PlayerUtils.isMoving() ? timer.get() : Timer.OFF);

    }

    private void unpress() {
        setPressed(mc.options.keyForward, false);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        // auto sprint
       if (SprintEnable.get()) {
            mc.player.setSprinting(true);
        }
       // auto walk
		if (WalkForwardEnable.get()) {
            setPressed(mc.options.keyForward, true);
        }
        // auto jump
        if (!mc.player.isOnGround() || mc.player.isSneaking() || !jump()) return;
        if (AutoJumpMode.get() == Mode.Jump) mc.player.jump();
    }
// auto move
    private void setPressed(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
    }
// Y height
    @EventHandler
    private void onJumpVelocityMultiplier(JumpVelocityMultiplierEvent event) {
        event.multiplier *= multiplier.get();
    }
}


