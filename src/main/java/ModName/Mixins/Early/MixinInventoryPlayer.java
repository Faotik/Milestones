package ModName.Mixins.Early;

import ModName.Configs.ConfigMilestones;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
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

@Mixin(InventoryPlayer.class)
public abstract class MixinInventoryPlayer {

    @Unique
    private static final long TICKS_IN_SECOND = 20;
    @Unique
    private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
    @Unique
    private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

    @Unique
    private static final Set<String> items = new HashSet<>(Arrays.asList(ConfigMilestones.milestones.items));

    @Shadow
    public EntityPlayer player;

    @Inject(method = "addItemStackToInventory", at = @At("HEAD"))
    private void addItemStackToInventory(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        checkItem(stack);
    }

    @Inject(method = "setInventorySlotContents", at = @At("HEAD"))
    private void setInventorySlotContents(int slot, ItemStack stack, CallbackInfo ci) {
        checkItem(stack);
    }

    @Unique
    private void checkItem(ItemStack stack){
        if (stack == null || stack.getItem() == null || this.player.worldObj.isRemote) {
            return;
        }

        int meta = stack.getItemDamage();
        String id = GameRegistry.findUniqueIdentifierFor(stack.getItem()).toString();
        String itemIdAndMeta = meta == 0 ? id : id + ":" + meta;

        if (items.contains(itemIdAndMeta)){
            NBTTagCompound milestones = getNbtTagCompoundMilestones();
            System.out.println(milestones);

            if (!milestones.hasKey(itemIdAndMeta)) {
                long totalWorldTime = this.player.worldObj.getTotalWorldTime();
                String totalWorldTimeString = getTotalWorldTimeString();

                milestones.setLong(itemIdAndMeta, totalWorldTime);

                this.player.addChatMessage(new ChatComponentText(
                    EnumChatFormatting.GRAY + "[New milestone completed] - " +
                        EnumChatFormatting.GREEN + itemIdAndMeta + " : " + totalWorldTimeString
                ));
            }
        }

    }

    @Unique
    private String getTotalWorldTimeString() {
        long totalWorldTimeTicks = this.player.worldObj.getTotalWorldTime();

        String totalWorldTime;
        if (totalWorldTimeTicks < TICKS_IN_MINUTES) {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_SECOND) + "s";
        }
        else if (totalWorldTimeTicks < TICKS_IN_HOURS) {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_MINUTES) + "m " + ((totalWorldTimeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
        }
        else {
            totalWorldTime = (totalWorldTimeTicks / TICKS_IN_HOURS) + "h " + ((totalWorldTimeTicks % TICKS_IN_HOURS) / TICKS_IN_MINUTES) + "m " + ((totalWorldTimeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
        }
        return totalWorldTime;
    }

    @Unique
    private NBTTagCompound getNbtTagCompoundMilestones() {
        NBTTagCompound entityData = this.player.getEntityData();

        if (!entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (!persistedData.hasKey("CompletedMilestones")) {
            persistedData.setTag("CompletedMilestones", new NBTTagCompound());
        }
        return persistedData.getCompoundTag("CompletedMilestones");
    }
}
