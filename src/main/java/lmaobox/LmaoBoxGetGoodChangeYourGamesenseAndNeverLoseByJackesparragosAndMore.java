package lmaobox;

import lmaobox.holyfuckthisismodulesfolder.categories.category.*;
import lmaobox.holyfuckthisismodulesfolder.categories.categorysecond.*;
import lmaobox.holyfuckthisismodulesfolder.categories.categorythird.*;
import lmaobox.holyfuckthisismodulesfolder.hud.*;
import lmaobox.nomodulesfolder.Wrapper;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.MeteorAddon;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.hud.HUD;
import meteordevelopment.meteorclient.systems.config.Config;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.item.Items;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Arrays;
import java.util.List;
import java.lang.invoke.MethodHandles;

public class LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore extends MeteorAddon {
    public static MinecraftClient mc;
    public static final Logger LOG = LogManager.getLogger();
	public static final Category CATEGORY = new Category("lmaobox.PvP", Items.END_CRYSTAL.getDefaultStack());
	public static final Category CATEGORYSECOND = new Category("lmaobox.Movement", Items.GOLDEN_APPLE.getDefaultStack());
    public static final Category CATEGORYTHIRD = new Category("lmaobox.Misc", Items.TOTEM_OF_UNDYING.getDefaultStack());
    public static final String VERSION = "v.1.0.2-b21ea07b12"; // for devs

	@Override
	public void onInitialize() {
		LOG.info("Initializing LmaoBox - " + LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.VERSION);

        MeteorClient.EVENT_BUS.registerLambdaFactory("lmaobox", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
        //CATEGORY
        Modules.get().add(new ImprovedAnchorAura());
        Modules.get().add(new FragsReport());
        Modules.get().add(new HolePush());
        Modules.get().add(new NewSelfTrap());
        Modules.get().add(new NewSurround());
        Modules.get().add(new AutoPotions());
        // Modules.get().add(new CevBreaker());
        // Modules.get().add(new ImprovedAutoTrap());
        Modules.get().add(new Auto32K());
		Modules.get().add(new ImprovedHoleFiller());
        Modules.get().add(new ImprovedAutoCity());
        Modules.get().add(new BedAuraPlus());
        Modules.get().add(new FragsReport());
        Modules.get().add(new ImprovedAnchor());
        Modules.get().add(new TNTAura());
        //CATEGORYSECOND
        Modules.get().add(new ImprovedAnchor());
        Modules.get().add(new SpeedPlus());
        //CATEGORYTHIRD
        Modules.get().add(new BedAutoCrafter());
        Modules.get().add(new AutoSkull());
        Modules.get().add(new FuckedDetector());
        // Modules.get().add(new SpamSystem());
        Modules.get().add(new SoundLocator());
        Modules.get().add(new AutoBuildPortal());
        Modules.get().add(new LmaoBoxRPC());
        Modules.get().add(new LmaoBoxPrefix());

		//HUD
		HUD hud = Modules.get().get(HUD.class);
        hud.elements.add(new BaritoneProcesses(hud));
        hud.elements.add(new Watermark(hud));
        hud.elements.add(new Welcomer(hud));
        hud.elements.add(new Logo(hud));
        hud.elements.add(new WorldTime(hud));

        //HUD Item Counter
        hud.elements.add(new Anchor(hud));
        hud.elements.add(new Obsidian(hud));
        hud.elements.add(new Beds(hud));
        hud.elements.add(new Crystals(hud));
        hud.elements.add(new Gaps(hud));
        hud.elements.add(new TNT(hud));
        hud.elements.add(new XP(hud));
        hud.elements.add(new GlowStone(hud));

        Wrapper.setTitle("lmaobox.ru - " + LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.VERSION);
        Config.get().customWindowTitleText = "lmaobox.ru - " + LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.VERSION;
	}
    public void onRegisterCategories() {
        Modules.registerCategory(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY);
        Modules.registerCategory(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYSECOND);
        Modules.registerCategory(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORYTHIRD);
    }
}
