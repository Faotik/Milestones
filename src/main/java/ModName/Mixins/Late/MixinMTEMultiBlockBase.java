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
    @Inject(
        method = "addItemOutputs",
        at = @At("HEAD"),
        remap = false
    )
    public void addItemOutputs(ItemStack[] outputItems, CallbackInfoReturnable<Boolean> cir) {
        UUID uuid = ((MTEMultiBlockBase) (Object) this).getBaseMetaTileEntity().getOwnerUuid();
        for (ItemStack stack : outputItems) {
            Common.checkItem(uuid, stack);
        }
    }
}
