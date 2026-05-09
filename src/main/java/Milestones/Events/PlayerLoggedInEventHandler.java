package Milestones.Events;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;

import Milestones.Mixins.Common;
import Milestones.Milestones;
import Milestones.SaveData.CompletedMilestonesCacheSaveData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP playerMP) {
                UUID uuid = playerMP.getUniqueID();
                if (Milestones.completedMilestonesCache.containsKey(uuid)) {
                    for (String id : Milestones.completedMilestonesCache.get(uuid)) {
                        Common.completeMilestone(playerMP, uuid, id);
                    }
                    Milestones.completedMilestonesCache.remove(uuid);
                    CompletedMilestonesCacheSaveData.get()
                        .markDirty();
                }
            }
        }
    }
}
