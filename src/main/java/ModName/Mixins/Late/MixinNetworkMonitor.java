package ModName.Mixins.Late;

import ModName.Mixins.Common;
import ModName.Mixins.IPlayerDataAccessor;
import ModName.ModName;
import ModName.SaveData.CompletedMilestonesCacheSaveData;
import appeng.api.config.Actionable;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.networking.security.PlayerSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.core.worlddata.WorldData;
import appeng.me.cache.NetworkMonitor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.UUID;

import static ModName.Mixins.Common.getPlayerByUUID;

@Mixin(NetworkMonitor.class)
public abstract class MixinNetworkMonitor<T extends IAEStack<T>> implements IMEMonitor<T> {
    @Inject(
        method = "injectItems",
        at = @At("HEAD"),
        remap = false
    )
    private void onItemsAddedToNetwork(T input, Actionable mode, BaseActionSource src, CallbackInfoReturnable<T> cir) {
        if (mode == Actionable.MODULATE && input != null && input.getStackSize() > 0) {
            if (input instanceof IAEItemStack AEStack) {
                ItemStack stack = AEStack.getItemStack();

                UUID uuid = null;

                if (src instanceof PlayerSource playerSrc) {
                    uuid = playerSrc.player.getUniqueID();
                }
                else if (src instanceof MachineSource machineSrc) {
                    IActionHost machine = machineSrc.via;
                    int playerID = machine.getActionableNode().getPlayerID();
                    uuid = ((IPlayerDataAccessor)WorldData.instance().playerData()).getPlayerMapping().get(playerID).orNull();
                }

                if (uuid == null) {
                    return;
                }

                Common.checkItem(uuid, stack);
            }
        }
    }
}
