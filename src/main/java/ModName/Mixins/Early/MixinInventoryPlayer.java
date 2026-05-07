package ModName.Mixins.Early;

import ModName.Configs.ConfigMilestones;
import ModName.ModName;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
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
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;

                    long timeTicks = playerMP.func_147099_x().writeStat(StatList.minutesPlayedStat);
                    String totalWorldTimeString = getTimeString(timeTicks);

                    milestones.setLong(itemIdAndMeta, timeTicks);

                    this.player.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GRAY + "[New milestone completed!]: " +
                            EnumChatFormatting.GREEN + stack.getDisplayName() + " - " + totalWorldTimeString
                    ));

                    ItemStack trophyItemStack = new ItemStack(ModName.trophyBlock, 1);
                    trophyItemStack.setTagCompound(new NBTTagCompound());
                    NBTTagCompound nbt = trophyItemStack.getTagCompound();
                    nbt.setString("trophyitem", itemIdAndMeta);

                    player.dropPlayerItemWithRandomChoice(trophyItemStack, false);
                }
            }
        }

    }

    @Unique
    private String getTimeString(long timeTicks) {
        String timeString;
        if (timeTicks < TICKS_IN_MINUTES) {
            timeString = (timeTicks / TICKS_IN_SECOND) + "s";
        }
        else if (timeTicks < TICKS_IN_HOURS) {
            timeString = (timeTicks / TICKS_IN_MINUTES) + "m " + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
        }
        else {
            timeString = (timeTicks / TICKS_IN_HOURS) + "h " + ((timeTicks % TICKS_IN_HOURS) / TICKS_IN_MINUTES) + "m " + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND) + "s";
        }
        return timeString;
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
