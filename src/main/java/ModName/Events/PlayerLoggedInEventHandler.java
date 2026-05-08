package ModName.Events;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;

import ModName.Mixins.Common;
import ModName.ModName;
import ModName.SaveData.CompletedMilestonesCacheSaveData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP playerMP) {
                UUID uuid = playerMP.getUniqueID();
                if (ModName.completedMilestonesCache.containsKey(uuid)) {
                    for (String id : ModName.completedMilestonesCache.get(uuid)) {
                        Common.completeMilestone(playerMP, uuid, id);
                    }
                    ModName.completedMilestonesCache.remove(uuid);
                    CompletedMilestonesCacheSaveData.get()
                        .markDirty();
                }
            }
        }
    }
}
