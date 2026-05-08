package ModName.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.factory.GuiManager;

import ModName.GUI.GUIDataMilestones;
import ModName.GUI.GUIFactoryMilestones;
import ModName.ModName;

public class CommandMilestones extends CommandBase {

    @Override
    public String getCommandName() {
        return "milestones";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/milestones [view|clear]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Specify subcommand [view|clear]"));
        } else if (args[0].equals("clear")) {
            EntityPlayer target;
            if (sender instanceof EntityPlayer) {
                target = (EntityPlayer) sender;
            } else {
                sender.addChatMessage(new ChatComponentText("You must be a player to run this command"));
                return;
            }

            NBTTagCompound entityData = target.getEntityData();
            if (entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

                if (persistedData.hasKey("CompletedMilestones")) {
                    persistedData.removeTag("CompletedMilestones");
                }
            }

            sender.addChatMessage(
                new ChatComponentText(EnumChatFormatting.GREEN + "Milestones cleared for " + target.getDisplayName()));

        } else if (args[0].equals("view")) {
            EntityPlayerMP target;
            if (sender instanceof EntityPlayerMP) {
                target = (EntityPlayerMP) sender;
            } else {
                sender.addChatMessage(new ChatComponentText("You must be a player to run this command"));
                return;
            }

            GuiManager.open(new GUIFactoryMilestones(), new GUIDataMilestones(target), target);
        } else {
            sender.addChatMessage(new ChatComponentText("Incorrect subcommand. Use: /milestones [view|clear]."));
        }
    }
}
