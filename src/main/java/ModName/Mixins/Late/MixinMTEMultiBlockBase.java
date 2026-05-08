package ModName.Mixins.Late;

import ModName.Mixins.Common;
import ModName.ModName;
import ModName.SaveData.CompletedMilestonesCacheSaveData;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.UUID;

import static ModName.Mixins.Common.checkItem;
import static ModName.Mixins.Common.getPlayerByUUID;

@Mixin(MTEMultiBlockBase.class)
public abstract class MixinMTEMultiBlockBase {
    @Inject(method = "addItemOutputs", at = @At("HEAD"), remap = false)
    public void addItemOutputs(ItemStack[] outputItems, CallbackInfoReturnable<Boolean> cir) {
        UUID uuid = ((MTEMultiBlockBase) (Object) this).getBaseMetaTileEntity().getOwnerUuid();
        EntityPlayerMP player = getPlayerByUUID(uuid);

        boolean needUpdate = false;
        for (ItemStack stack : outputItems) {
            if (stack == null) {
                continue;
            }
            int meta = stack.getItemDamage();
            String id = GameRegistry.findUniqueIdentifierFor(stack.getItem()).toString();
            String itemIdAndMeta = meta == 0 ? id : id + ":" + meta;
            if (ModName.milestonesList.contains(itemIdAndMeta)) {
                if (player != null) {
                    Common.checkItem(player, stack);
                } else {
                    if (ModName.completedMilestonesCache.computeIfAbsent(uuid, k -> new HashSet<>()).add(itemIdAndMeta)) {
                        needUpdate = true;
                    }
                }
            }
        }

        if (needUpdate) {
            CompletedMilestonesCacheSaveData.get().markDirty();
        }
    }
}
