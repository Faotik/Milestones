package ModName.Mixins.Late;

import ModName.Mixins.Common;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ModName.Mixins.Common.checkItem;
import static ModName.Mixins.Common.getPlayerByUUID;

@Mixin(MTEBasicMachine.class)
public abstract class MixinMTEBasicMachine {
    @Final
    @Shadow
    public ItemStack[] mOutputItems;

    @Inject(
        method = "onPostTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Arrays;fill([Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 0
        ),
        remap = false
    )
    private void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick, CallbackInfo ci) {
        EntityPlayerMP player = getPlayerByUUID(aBaseMetaTileEntity.getOwnerUuid());
        if (player != null) {
            for (ItemStack stack : mOutputItems) {
                Common.checkItem(player, stack);
            }
        }
    }
}
