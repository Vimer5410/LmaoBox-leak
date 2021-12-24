package lmaobox.holyfuckthisismodulesfolder.hud.render;

import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.modules.render.hud.modules.HudElement;
import meteordevelopment.meteorclient.systems.modules.render.hud.HudRenderer;

public abstract class TextHUDElement extends HudElement {
    protected Color colorLeft;
    protected Color rightColor;
    protected Color right2Color;
    protected Color right3Color;
    protected boolean visible = true;

    private String left, secondleft, right, right2, right3, end;

    private double leftWidth;
    private double left2Width;
    private double rightWidth;
    private double right2Width;
    private double right3Width;

    public TextHUDElement(HUD hud, String name, String description, boolean defaultActive) {
        super(hud, name, description, defaultActive);
        this.colorLeft = hud.secondaryColor.get();
        this.rightColor = hud.secondaryColor.get();
        this.right2Color = hud.secondaryColor.get();
        this.right3Color = hud.secondaryColor.get();
    }

    @Override
    public void update(HudRenderer renderer) {
        left = getLeft();
        secondleft = get2Left();
        right = getRight();
        right2 = get2Right();
        right3 = get3Right();
        end = getEnd();
        leftWidth = renderer.textWidth(left);
        left2Width = renderer.textWidth(secondleft);
        rightWidth = renderer.textWidth(right);
        right2Width = renderer.textWidth(right2);
        right3Width = renderer.textWidth(right3);

        double textWidth = leftWidth + renderer.textWidth(right3);

        box.setSize(textWidth + renderer.textWidth(end), renderer.textHeight());
    }

    @Override
    public void render(HudRenderer renderer) {
        if (!visible) return;

        double x = box.getX();
        double y = box.getY();

        renderer.text(left, x, y, hud.primaryColor.get());
        renderer.text(secondleft, x + leftWidth, y, rightColor);
        renderer.text(right, x + leftWidth, y, rightColor);
        renderer.text(right2, x + leftWidth, y, rightColor);
        renderer.text(right3, x + leftWidth, y, rightColor);
        renderer.text(end, x + leftWidth + rightWidth, y, hud.primaryColor.get());
    }

    protected void setLeft(String left) {
        this.left = left;
        this.leftWidth = 0;
    }

    protected abstract String getLeft();
    protected abstract String get2Left();
    protected abstract String getRight();
    protected abstract String get2Right();
    protected abstract String get3Right();
    protected abstract String getEnd();
}
