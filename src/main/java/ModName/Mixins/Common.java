package ModName.Mixins;

import ModName.ModName;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.UUID;

public class Common {
    private static final long TICKS_IN_SECOND = 20;
    private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
    private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

    public static void checkItem(EntityPlayer player, ItemStack stack){
        if (stack == null || stack.getItem() == null || player.worldObj.isRemote) {
            return;
        }

        int meta = stack.getItemDamage();
        String id = GameRegistry.findUniqueIdentifierFor(stack.getItem()).toString();
        String itemIdAndMeta = meta == 0 ? id : id + ":" + meta;

        if (ModName.milestonesList.contains(itemIdAndMeta)){
            NBTTagCompound completedMilestones = getNbtTagCompoundMilestones(player);

            if (!completedMilestones.hasKey(itemIdAndMeta)) {
                if (player instanceof EntityPlayerMP) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) player;

                    long timeTicks = playerMP.func_147099_x().writeStat(StatList.minutesPlayedStat);
                    String totalWorldTimeString = getTimeString(timeTicks);

                    completedMilestones.setLong(itemIdAndMeta, timeTicks);

                    player.addChatMessage(new ChatComponentText(
                        EnumChatFormatting.GRAY + "[New milestone completed!]: " +
                            EnumChatFormatting.GREEN + stack.getDisplayName() + " - " + totalWorldTimeString
                    ));

                    spawnTrophy(player, itemIdAndMeta);
                    spawnFirework(player);
                }
            }
        }
    }

    private static String getTimeString(long timeTicks) {
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

    private static NBTTagCompound getNbtTagCompoundMilestones(EntityPlayer player) {
        NBTTagCompound entityData = player.getEntityData();

        if (!entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }
        NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (!persistedData.hasKey("CompletedMilestones")) {
            persistedData.setTag("CompletedMilestones", new NBTTagCompound());
        }
        return persistedData.getCompoundTag("CompletedMilestones");
    }

    private static void spawnTrophy(EntityPlayer player, String itemIdAndMeta) {
        ItemStack trophyItemStack = new ItemStack(ModName.trophyBlock, 1);
        trophyItemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = trophyItemStack.getTagCompound();
        nbt.setString("trophyitem", itemIdAndMeta);

        EntityItem trophyEntity = new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, trophyItemStack);
        player.worldObj.spawnEntityInWorld(trophyEntity);
    }

    private static void spawnFirework(EntityPlayer player) {
        ItemStack fireworkItemStack = new ItemStack(Items.fireworks);

        NBTTagCompound baseTag = new NBTTagCompound();
        NBTTagCompound fireworksTag = new NBTTagCompound();
        NBTTagList explosionsList = new NBTTagList();
        NBTTagCompound explosionTag = new NBTTagCompound();

        explosionTag.setByte("Type", (byte) 1);
        explosionTag.setByte("Flicker", (byte) 1);
        explosionTag.setByte("Trail", (byte) 0);
        int[] colors = new int[] { 0xFF0000, 0x00FF00, 0x0000FF };
        explosionTag.setIntArray("Colors", colors);
        int[] fadeColors = new int[] { 0xFFFFFF };
        explosionTag.setIntArray("FadeColors", fadeColors);

        explosionsList.appendTag(explosionTag);
        fireworksTag.setTag("Explosions", explosionsList);
        fireworksTag.setByte("Flight", (byte) 0);

        baseTag.setTag("Fireworks", fireworksTag);
        fireworkItemStack.setTagCompound(baseTag);

        EntityFireworkRocket rocket = new EntityFireworkRocket(player.worldObj, player.posX, player.posY, player.posZ, fireworkItemStack);
        player.worldObj.spawnEntityInWorld(rocket);
    }

    public static EntityPlayerMP getPlayerByUUID(UUID targetUUID) {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null && server.getConfigurationManager() != null) {
            for (Object obj : server.getConfigurationManager().playerEntityList) {
                EntityPlayerMP player = (EntityPlayerMP) obj;
                if (player.getUniqueID().equals(targetUUID)) {
                    return player;
                }
            }
        }
        return null;
    }
}
