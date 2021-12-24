package lmaobox.holyfuckthisismodulesfolder.hud;

import meteordevelopment.meteorclient.renderer.GL;
import meteordevelopment.meteorclient.renderer.Renderer2D;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.HudElement;
import meteordevelopment.meteorclient.utils.Utils;
import net.minecraft.util.Identifier;

public class Logo extends HudElement {
    private static final Identifier LOGO = new Identifier("lmaobox", "logo.png");
    private static final Identifier LOGO_FLAT = new Identifier("lmaobox", "logo_flat.png");

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder().name("scale").description("The scale.").defaultValue(2).min(1).sliderMin(1).sliderMax(10).build());

    public Logo(HUD hud) {
        super(hud, "lmaoBox-logo", "display lmaobox logo.");
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(96 * scale.get(), 96 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (!Utils.canUpdate()) return;
        double x = box.getX();
        double y = box.getY();
        int w = (int) box.width;
        int h = (int) box.height;
        Renderer2D.TEXTURE.begin();
        Renderer2D.TEXTURE.render(null);
    }
}


