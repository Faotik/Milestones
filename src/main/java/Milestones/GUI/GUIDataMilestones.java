package Milestones.GUI;

import java.util.ArrayList;
import java.util.List;

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
        this.allMilestones = parseConfig();
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

    private String[] parseConfig() {
        List<String> result = new ArrayList<>();

        for (String entry : ConfigMilestones.items) {
            result.add(entry.split("#")[0].trim());
        }

        return result.toArray(new String[0]);
    }
}
