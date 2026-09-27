package Milestones.Mixins;

import static Milestones.Mixins.Common.completeMilestone;
import static Milestones.Utils.getPlayerByUUID;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;

import Milestones.Milestones;
import serverutils.lib.data.ForgePlayer;
import serverutils.lib.data.ForgeTeam;
import serverutils.lib.data.ServerUtilitiesAPI;
import serverutils.lib.data.Universe;

public class ServerUtilitiesHandler {

    public static void checkMilestoneTeam(UUID uuid, String id) {
        if (Milestones.milestonesId.contains(id)) {
            ForgeTeam team = Universe.get()
                .getTeam(ServerUtilitiesAPI.getTeam(uuid));
            if (!team.getMembers()
                .isEmpty()) {
                for (ForgePlayer member : team.getMembers()) {
                    EntityPlayerMP playerMP = member.isOnline() ? member.getPlayer() : null;
                    completeMilestone(playerMP, uuid, id);
                }
            } else {
                EntityPlayerMP playerMP = getPlayerByUUID(uuid);
                completeMilestone(playerMP, uuid, id);
            }
        }
    }
}
