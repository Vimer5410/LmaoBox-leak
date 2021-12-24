package lmaobox.holyfuckthisismodulesfolder.categories.categorythird;

import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.Renderer3D;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;


import java.util.ArrayList;
import java.util.List;

public class FuckedDetector extends Module {

    public FuckedDetector(){
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD, "fucked-detector", "see if people are hecked.");
    }
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Double> renderDistance = sgGeneral.add(new DoubleSetting.Builder()
            .name("distance")
            .description("render distance.")
            .defaultValue(20)
            .min(0)
            .sliderMax(100)
            .build()
    );

    private final Setting<Boolean> top = sgGeneral.add(new BoolSetting.Builder()
            .name("render-top")
            .description(".")
            .defaultValue(false)
            .build()
    );

    private final Setting<Boolean> bottom = sgGeneral.add(new BoolSetting.Builder()
            .name("render-bottom")
            .description(".")
            .defaultValue(true)
            .build()
    );

    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
            .name("shape-mode")
            .description("How the shapes are rendered.")
            .defaultValue(ShapeMode.Both)
            .build()
    );

    private final Setting<SettingColor> nextSideColor = sgRender.add(new ColorSetting.Builder()
            .name("side-color")
            .description("color settings for side shape mode.")
            .defaultValue(new SettingColor(255, 255, 255,25))
            .build()
    );

    private final Setting<SettingColor> nextLineColor = sgRender.add(new ColorSetting.Builder()
            .name("line-color")
            .description("color settings for line shape mode.")
            .defaultValue(new SettingColor(255, 255, 255))
            .build()
    );

    private final List<PlayerEntity> players = new ArrayList<>();

    @Override
    public void onDeactivate() {
        players.clear();
    }

    private void updateLastPlayers() {
        players.clear();
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof PlayerEntity && entity != mc.player && entity.distanceTo(mc.player) <= renderDistance.get()) players.add((PlayerEntity) entity);
        }
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        updateLastPlayers();
    }



    @EventHandler
    private void onRender(Render3DEvent event) {
        if (players.isEmpty()) return;

        for (PlayerEntity renderer : players) {

            BlockPos pos = renderer.getBlockPos();
            if (renderer.equals(players.get(players.size() - 1))) {
                if (bottom.get()) {
                    event.renderer.box(pos, nextSideColor.get(), nextLineColor.get(), shapeMode.get(), 0);
                }
                if (top.get()) {
                    event.renderer.box(pos.up(1), nextSideColor.get(), nextLineColor.get(), shapeMode.get(), 0);
                }
            }
        }
    }
}
