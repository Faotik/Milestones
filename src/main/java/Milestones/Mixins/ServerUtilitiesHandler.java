package Milestones.Mixins;

import static Milestones.Mixins.Common.completeMilestone;
import static Milestones.Utils.getIdAndMeta;
import static Milestones.Utils.getPlayerByUUID;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import Milestones.Milestones;
import serverutils.lib.data.ForgePlayer;
import serverutils.lib.data.ForgeTeam;
import serverutils.lib.data.ServerUtilitiesAPI;
import serverutils.lib.data.Universe;

public class ServerUtilitiesHandler {

    public static void checkItemTeam(UUID uuid, ItemStack stack) {
        String idAndMeta = getIdAndMeta(stack);
        if (Milestones.milestonesList.contains(idAndMeta)) {
            ForgeTeam team = Universe.get()
                .getTeam(ServerUtilitiesAPI.getTeam(uuid));
            if (!team.getMembers()
                .isEmpty()) {
                for (ForgePlayer member : team.getMembers()) {
                    EntityPlayerMP playerMP = member.isOnline() ? member.getPlayer() : null;
                    completeMilestone(playerMP, uuid, idAndMeta);
                }
            } else {
                EntityPlayerMP playerMP = getPlayerByUUID(uuid);
                completeMilestone(playerMP, uuid, idAndMeta);
            }
        }
    }
}
