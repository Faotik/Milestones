package ModName.SaveData;

import ModName.ModName;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class CompletedMilestonesCacheSaveData extends WorldSavedData {

    private static final String DATA_NAME = ModName.MODID + "_CompletedMilestonesCacheSaveData";

    public CompletedMilestonesCacheSaveData() {
        super(DATA_NAME);
    }

    public CompletedMilestonesCacheSaveData(String name) {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        ModName.completedMilestonesCache.clear();

        NBTTagList playerList = nbt.getTagList("PlayerMilestones", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < playerList.tagCount(); i++) {
            NBTTagCompound playerTag = playerList.getCompoundTagAt(i);
            UUID uuid = UUID.fromString(playerTag.getString("UUID"));

            Set<String> milestones = new HashSet<>();
            NBTTagList milestoneList = playerTag.getTagList("Milestones", Constants.NBT.TAG_STRING);

            for (int j = 0; j < milestoneList.tagCount(); j++) {
                milestones.add(milestoneList.getStringTagAt(j));
            }

            ModName.completedMilestonesCache.put(uuid, milestones);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList playerList = new NBTTagList();

        for (var entry : ModName.completedMilestonesCache.entrySet()) {
            NBTTagCompound playerTag = new NBTTagCompound();
            playerTag.setString("UUID", entry.getKey().toString());

            NBTTagList milestoneList = new NBTTagList();
            for (String milestone : entry.getValue()) {
                milestoneList.appendTag(new NBTTagString(milestone));
            }

            playerTag.setTag("Milestones", milestoneList);
            playerList.appendTag(playerTag);
        }

        nbt.setTag("PlayerMilestones", playerList);
    }

    public static CompletedMilestonesCacheSaveData get() {
        World world = DimensionManager.getWorld(0);

        if (world == null) {
            return null;
        }

        CompletedMilestonesCacheSaveData instance = (CompletedMilestonesCacheSaveData) world.mapStorage.loadData(CompletedMilestonesCacheSaveData.class, DATA_NAME);

        if (instance == null) {
            instance = new CompletedMilestonesCacheSaveData();
            world.mapStorage.setData(DATA_NAME, instance);
        }

        return instance;
    }
}
