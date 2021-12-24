package lmaobox.holyfuckthisismodulesfolder.hud;

import lmaobox.nomodulesfolder.misc.ItemCounter;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.HudElement;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.render.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Anchor extends HudElement {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> scale = sgGeneral.add(new DoubleSetting.Builder().name("scale").description("Scale of the counter.").defaultValue(3).min(1).sliderMin(1).sliderMax(4).build());

    public Anchor(HUD hud) {
        super(hud, "Item-Anchor", "", false);
    }

    @Override
    public void update(HudRenderer renderer) {
        box.setSize(16 * scale.get(), 16 * scale.get());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (!Utils.canUpdate()) return;
        double x = box.getX();
        double y = box.getY();

        if (isInEditor()) {
            RenderUtils.drawItem(Items.RESPAWN_ANCHOR.getDefaultStack(), (int) x, (int) y, scale.get(), true);
        } else if (InvUtils.find(Items.RESPAWN_ANCHOR).getCount() > 0) {
            RenderUtils.drawItem(new ItemStack(Items.RESPAWN_ANCHOR, ItemCounter.anchor()), (int) x, (int) y, scale.get(), true);
        }
    }
}
