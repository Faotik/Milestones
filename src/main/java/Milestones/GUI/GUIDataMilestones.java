package Milestones.GUI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.GuiData;

import Milestones.Configs.ConfigMilestones;

public class GUIDataMilestones extends GuiData {

    public NBTTagCompound completedMilestones;
    public String[] allMilestones;

    public GUIDataMilestones(EntityPlayer player) {
        super(player);
        this.completedMilestones = getNbtTagCompoundMilestones();
        this.allMilestones = ConfigMilestones.items;
    }

    private NBTTagCompound getNbtTagCompoundMilestones() {
        NBTTagCompound entityData = getPlayer().getEntityData();
        if (!entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            return null;
        }

        NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (!persistedData.hasKey("CompletedMilestones")) {
            return null;
        }

        return persistedData.getCompoundTag("CompletedMilestones");
    }
}
