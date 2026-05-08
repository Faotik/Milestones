package ModName.Mixins.Early;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ModName.Mixins.Common;

@Mixin(InventoryPlayer.class)
public abstract class MixinInventoryPlayer {

    @Shadow
    public EntityPlayer player;

    @Inject(method = "addItemStackToInventory", at = @At("HEAD"))
    private void addItemStackToInventory(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!player.worldObj.isRemote) {
            Common.checkItem(player.getUniqueID(), stack);
        }
    }

    @Inject(method = "setInventorySlotContents", at = @At("HEAD"))
    private void setInventorySlotContents(int slot, ItemStack stack, CallbackInfo ci) {
        if (!player.worldObj.isRemote) {
            Common.checkItem(player.getUniqueID(), stack);
        }
    }
}
