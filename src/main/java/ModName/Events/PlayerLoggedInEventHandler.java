package ModName.Events;

import ModName.Mixins.Common;
import ModName.ModName;
import ModName.SaveData.CompletedMilestonesCacheSaveData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.util.UUID;

public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP playerMP) {
                UUID uuid = playerMP.getUniqueID();
                if (ModName.completedMilestonesCache.containsKey(uuid)){
                    for (String id : ModName.completedMilestonesCache.get(uuid)){
                        Common.completeMilestone(playerMP, uuid, id);
                    }
                    ModName.completedMilestonesCache.remove(uuid);
                    CompletedMilestonesCacheSaveData.get().markDirty();
                }
            }
        }
    }
}
