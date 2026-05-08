package ModName.Mixins.Late;

import java.util.UUID;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import ModName.Mixins.Common;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

@Mixin(value = MTEBasicMachine.class, remap = false)
public abstract class MixinMTEBasicMachine {

    @Final
    @Shadow
    public ItemStack[] mOutputItems;

    @Inject(
        method = "onPostTick",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Arrays;fill([Ljava/lang/Object;Ljava/lang/Object;)V",
            ordinal = 0),
        remap = false)
    private void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick, CallbackInfo ci) {
        UUID uuid = aBaseMetaTileEntity.getOwnerUuid();
        for (ItemStack stack : mOutputItems) {
            Common.checkItem(uuid, stack);
        }
    }
}
