package lmaobox.holyfuckthisismodulesfolder.categories.category;

import meteordevelopment.orbit.EventHandler;
import lmaobox.LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.combat.*;
import meteordevelopment.meteorclient.utils.entity.EntityUtils;
import meteordevelopment.meteorclient.utils.misc.Placeholders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.*;

import java.util.*;

public class FragsReport extends Module {
    private int ticks;

    private boolean canSendPop;

    private final Random random = new Random();

    private final SettingGroup sgKills = settings.createGroup("Kills");

    private final Setting<Boolean> kills = sgKills.add(new BoolSetting.Builder()
        .name("enabled")
        .description("Enables the kill messages.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Mode> killMode = sgKills.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Determines what messages to use.")
        .defaultValue(Mode.withLmaoBox)
        .build()
    );

    private final Setting<MessageStyle> killMessageStyle = sgKills.add(new EnumSetting.Builder<MessageStyle>()
        .name("mode")
        .description("Determines what message style to use.")
        .defaultValue(MessageStyle.RAGE)
        .visible(() -> killMode.get() == Mode.withLmaoBox)
        .build()
    );

    private final Setting<Boolean> killIgnoreFriends = sgKills.add(new BoolSetting.Builder()
        .name("no-for-friends")
        .description("no for friends.")
        .defaultValue(true)
        .build()
    );

    private final Setting<List<String>> killMessages = sgKills.add(new StringListSetting.Builder()
        .name("messages")
        .description("kill messages.")
        .defaultValue(Arrays.asList(
            "%killed_player% was defeated by lmaobox.ru.",
            "%killed_player% was killed by lmaobox.ru.",
            "%killed_player% was fucked by lmaobox.ru."
        ))
        .visible(() -> killMode.get() == Mode.Custom)
        .build()
    );

    public FragsReport() {
        super(LmaoBoxGetGoodChangeYourGamesenseAndNeverLoseByJackesparragosAndMore.CATEGORY, "frags-reporter", "send auto ez message on chat.");
    }

    @EventHandler
    public void onPacketReadMessage(PacketEvent.Receive event) {
        if (!kills.get()) return;
        if (killMessages.get().isEmpty() && killMode.get() == Mode.Custom) return;
        if (event.packet instanceof GameMessageS2CPacket) {
            String msg = ((GameMessageS2CPacket) event.packet).getMessage().getString();
            for (PlayerEntity player : mc.world.getPlayers()) {
                if (player == mc.player)
                    continue;
                if (player.getName().getString().equals(mc.getSession().getUsername())) return;
                if (msg.contains(player.getName().getString())) {
                    if (msg.contains("by " + mc.getSession().getUsername()) || msg.contains("whilst fighting " + mc.getSession().getUsername()) || msg.contains(mc.getSession().getUsername() + " sniped") || msg.contains(mc.getSession().getUsername() + " annaly fucked") || msg.contains(mc.getSession().getUsername() + " destroyed") || msg.contains(mc.getSession().getUsername() + " killed") || msg.contains(mc.getSession().getUsername() + " fucked") || msg.contains(mc.getSession().getUsername() + " separated") || msg.contains(mc.getSession().getUsername() + " punched") || msg.contains(mc.getSession().getUsername() + " shoved")) {
                        if (msg.contains("end crystal") || msg.contains("end-crystal")) {
                            if (Modules.get().isActive(CrystalAura.class)) {
                              //  if (!Modules.get().isActive(CEVBreaker.class)) {
                                 //   if (mc.player.distanceTo(player) < Modules.get().get(CrystalAura.class).targetRange.get())
                                    {
                                        if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                        if (EntityUtils.getGameMode(player).isCreative()) return;
                                        String message = getMessageStyle();
                                        mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                    }
                                   // else
                                        {
                                    if (mc.player.distanceTo(player) < 5) {
                                        if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                        if (EntityUtils.getGameMode(player).isCreative()) return;
                                        String message = getMessageStyle();
                                        mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                    }
                                }
                            } else {
                                if (mc.player.distanceTo(player) < 7) {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            }
                        } else {
                            if (Modules.get().isActive(KillAura.class)) {
                          //      if (mc.player.distanceTo(player) < Modules.get().get(KillAura.class).targetRange.get())
                                {
                                    if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                    if (EntityUtils.getGameMode(player).isCreative()) return;
                                    String message = getMessageStyle();
                                    mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                                }
                            } else if (mc.player.distanceTo(player) < 8) {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        }
                    } else {
                        if ((msg.contains("bed") || msg.contains("[Intentional Game Design]")) && (Modules.get().isActive(BedAura.class))) {
                       //     if ((mc.player.distanceTo(player) < Modules.get().get(BedAura.class).targetRange.get()))
                            {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        } else if ((msg.contains("anchor") || msg.contains("[Intentional Game Design]")) && Modules.get().isActive(AnchorAura.class)) {
                      //      if (mc.player.distanceTo(player) < Modules.get().get(AnchorAura.class).targetRange.get())
                            {
                                if (killIgnoreFriends.get() && Friends.get().isFriend(player)) return;
                                if (EntityUtils.getGameMode(player).isCreative()) return;
                                String message = getMessageStyle();
                                mc.player.sendChatMessage(Placeholders.apply(message).replace("%killed_player%", player.getName().getString()));
                            }
                        }
                    }
                }
            }
        }
    }

    public String getMessageStyle() {
        return switch (killMode.get()) {
            case withLmaoBox -> switch (killMessageStyle.get()) {
                case RAGE -> getMessage().get(random.nextInt(getMessage().size()));
                case GG -> getGGMessage().get(random.nextInt(getGGMessage().size()));
            };
            case Custom -> killMessages.get().get(random.nextInt(killMessages.get().size()));
        };
    }

    private static List<String> getMessage() {
        return Arrays.asList(
            "I just EZZZZz'd %killed_player% with lmaobox.ru!",
            "I just fucked %killed_player% with lmaobox.ru!",
            "I just nae nae'd %killed_player% with lmaobox.ru!"
        );
    }

    private static List<String> getGGMessage() {
        return Arrays.asList(
            "GG %killed_player%!",
            "Nice fight %killed_player%!",
            "Good fight, %killed_player%!"
        );
    }

    // Totem Pops

/*    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!totems.get()) return;
        if (totemMessages.get().isEmpty() && totemMode.get() == Mode.Custom) return;
        if (!(event.packet instanceof EntityStatusS2CPacket)) return;

        EntityStatusS2CPacket packet = (EntityStatusS2CPacket) event.packet;
        if (packet.getStatus() != 35) return;

        Entity entity = packet.getEntity(mc.world);

        if (!(entity instanceof PlayerEntity)) return;

        if (entity == mc.player) return;
        if (mc.player.distanceTo(entity) > 8) return;
        if (Friends.get().isFriend((PlayerEntity) entity) && totemIgnoreFriends.get()) return;

        if (EntityUtils.getGameMode(mc.player).isCreative()) return;
        if (canSendPop) {
            String message = getTotemMessageStyle();
            mc.player.sendChatMessage(Placeholders.apply(message).replace("%popped_player%", entity.getName().getString()));
            canSendPop = false;
        }
    }

    @Override
    public void onActivate() {
        canSendPop = true;
        ticks = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (ticks > delay.get()) {
            canSendPop = true;
            ticks = 0;
        }

        if (!canSendPop) ticks++;
    }

 /*   public String getTotemMessageStyle() {
        return switch (totemMode.get()) {
            case withLmaoBox -> getTotemMessage().get(random.nextInt(getTotemMessage().size()));
            case Custom -> totemMessages.get().get(random.nextInt(totemMessages.get().size()));
        };
    }

 /*   private static List<String> getTotemMessage() {
        return Arrays.asList(
            "%popped_player% just got popped by MatHax Legacy!",
            "Keep popping %popped_player%! MatHax Legacy owns you!",
            "%popped_player%'s totem just got ended by MatHax Legacy!",
            "%popped_player% just lost 1 totem thanks to MatHax Legacy!",
            "I just easily popped %popped_player% using MatHax Legacy!"
        );
    }
*/
    public enum Mode {
        withLmaoBox,
        Custom
    }

    public enum MessageStyle {
        RAGE,
        GG
    }
}
