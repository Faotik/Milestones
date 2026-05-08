package ModName.Mixins.Late;

import ModName.Mixins.Common;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = MTEMultiBlockBase.class, remap = false)
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
