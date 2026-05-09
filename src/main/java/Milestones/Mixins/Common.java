package Milestones.Mixins;

import static Milestones.Utils.*;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import Milestones.Configs.ConfigServer;
import Milestones.Milestones;
import Milestones.SaveData.CompletedMilestonesCacheSaveData;
import cpw.mods.fml.common.Loader;

public class Common {

    public static void checkItem(UUID uuid, ItemStack stack) {
        if (stack == null || stack.getItem() == null) {
            return;
        }

        if (Loader.isModLoaded("serverutilities") && ConfigServer.SUIntegration) {
            ServerUtilitiesHandler.checkItemTeam(uuid, stack);
            return;
        }

        String idAndMeta = getIdAndMeta(stack);
        if (Milestones.milestonesList.contains(idAndMeta)) {
            EntityPlayerMP playerMP = getPlayerByUUID(uuid);
            completeMilestone(playerMP, uuid, idAndMeta);
        }
    }

    public static void completeMilestone(EntityPlayerMP playerMP, UUID uuid, String id) {
        if (playerMP == null) {
            if (Milestones.completedMilestonesCache.computeIfAbsent(uuid, k -> new HashSet<>())
                .add(id)) {
                CompletedMilestonesCacheSaveData.get()
                    .markDirty();
            }
            return;
        }

        ItemStack stack = getItemStackFromId(id);

        NBTTagCompound completedMilestones = getNbtTagCompoundMilestones(playerMP);

        if (!completedMilestones.hasKey(id)) {
            long timeTicks = playerMP.func_147099_x()
                .writeStat(StatList.minutesPlayedStat);
            String totalWorldTimeString = getTimeString(timeTicks);

            completedMilestones.setLong(id, timeTicks);

            playerMP.addChatMessage(
                new ChatComponentText(
                    EnumChatFormatting.GRAY + "[New milestone completed!]: "
                        + EnumChatFormatting.GREEN
                        + stack.getDisplayName()
                        + " - "
                        + totalWorldTimeString));

            if (ConfigServer.enableTrophies) {
                spawnTrophy(playerMP, id);
            }
            if (ConfigServer.enableFireworks) {
                spawnFirework(playerMP);
            }
        }
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

    private static void spawnTrophy(EntityPlayerMP player, String itemIdAndMeta) {
        ItemStack trophyItemStack = new ItemStack(Milestones.trophyBlock, 1);
        trophyItemStack.setTagCompound(new NBTTagCompound());
        NBTTagCompound nbt = trophyItemStack.getTagCompound();
        nbt.setString("trophyitem", itemIdAndMeta);
        nbt.setString("trophyownername", player.getDisplayName());
        nbt.setInteger("trophyplaytime", player.func_147099_x().writeStat(StatList.minutesPlayedStat));
        nbt.setLong("trophydate", System.currentTimeMillis());

        EntityItem trophyEntity = new EntityItem(
            player.worldObj,
            player.posX,
            player.posY,
            player.posZ,
            trophyItemStack);
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

        EntityFireworkRocket rocket = new EntityFireworkRocket(
            player.worldObj,
            player.posX,
            player.posY,
            player.posZ,
            fireworkItemStack);
        player.worldObj.spawnEntityInWorld(rocket);
    }
}
