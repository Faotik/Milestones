package ModName.Mixins.Early;

import ModName.Configs.ConfigMilestones;
import ModName.Mixins.Common;
import ModName.ModName;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static ModName.Mixins.Common.checkItem;

@Mixin(InventoryPlayer.class)
public abstract class MixinInventoryPlayer {
    @Shadow
    public EntityPlayer player;

    @Inject(method = "addItemStackToInventory", at = @At("HEAD"))
    private void addItemStackToInventory(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        Common.checkItem(player, stack);
    }

    @Inject(method = "setInventorySlotContents", at = @At("HEAD"))
    private void setInventorySlotContents(int slot, ItemStack stack, CallbackInfo ci) {
        Common.checkItem(player, stack);
    }
}
