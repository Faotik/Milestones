package ModName.ItemBlock;

import ModName.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TrophyItemBlock extends ItemBlock {
    public TrophyItemBlock(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("trophyownername")) {
                String owner = stack.getTagCompound().getString("trophyownername");
                list.add(StatCollector.translateToLocal("tooltip.trophy.owner") + ": " + EnumChatFormatting.AQUA + owner);
            }

            if (stack.getTagCompound().hasKey("trophyitem")) {
                String id = stack.getTagCompound().getString("trophyitem");
                ItemStack item = Utils.getItemStackFromId(id);
                String displayName = item.getDisplayName();

                list.add(StatCollector.translateToLocal("tooltip.trophy.item") + ": " + EnumChatFormatting.GOLD + displayName);
            }

            if (stack.getTagCompound().hasKey("trophyplaytime")) {
                int playtime = stack.getTagCompound().getInteger("trophyplaytime");
                list.add(StatCollector.translateToLocal("tooltip.trophy.playtime") + ": " + EnumChatFormatting.GREEN + Utils.getTimeString(playtime));
            }

            if (stack.getTagCompound().hasKey("trophydate")) {
                long dateEpoch = stack.getTagCompound().getLong("trophydate");
                System.out.println(dateEpoch);
                LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(dateEpoch),
                    ZoneId.systemDefault()
                );
                String dateFormated = dateTime.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
                list.add(StatCollector.translateToLocal("tooltip.trophy.date") + ": " + EnumChatFormatting.DARK_PURPLE + dateFormated);
            }
        } else {
            list.add(EnumChatFormatting.DARK_GRAY + StatCollector.translateToLocal("tooltip.trophy.empty"));
        }
    }
}
