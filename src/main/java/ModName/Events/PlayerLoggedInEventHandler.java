package ModName.Events;

import ModName.Mixins.Common;
import ModName.ModName;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class PlayerLoggedInEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            if (event.player instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;

                if (ModName.completedMilestonesCache.containsKey(player.getUniqueID())){
                    for (String itemAndMeta : ModName.completedMilestonesCache.get(player.getUniqueID())){
                        String[] parts = itemAndMeta.split(":");
                        String modid = parts[0];
                        String name = parts[1];
                        int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

                        Item item = GameRegistry.findItem(modid, name);
                        ItemStack stack = new ItemStack(item, 1, meta);

                        Common.checkItem(player, stack);
                    }
                }
            }
        }
    }
}
