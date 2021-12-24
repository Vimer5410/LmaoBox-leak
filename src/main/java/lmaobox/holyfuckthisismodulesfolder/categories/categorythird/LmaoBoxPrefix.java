package lmaobox.holyfuckthisismodulesfolder.categories.categorythird;

import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class LmaoBoxPrefix extends Module {

    public LmaoBoxPrefix() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD, "LmaoBox-Prefix", "Cool lmaobox prefix on chat.");
    }

    @Override
    public void onActivate() {
        ChatUtils.registerCustomPrefix("lmaobox", this::getPrefix);
        ChatUtils.registerCustomPrefix("meteordevelopment.meteorclient", this::getPrefix);
        }

        public LiteralText getPrefix() {
            BaseText logo = new LiteralText("LmaoBox");
            LiteralText prefix = new LiteralText("");
            logo.setStyle(logo.getStyle().withFormatting(Formatting.WHITE));
            prefix.setStyle(prefix.getStyle().withFormatting(Formatting.WHITE));
            prefix.append("");
            prefix.append(logo);
            prefix.append(" >> ");
            return prefix;
        }
    }