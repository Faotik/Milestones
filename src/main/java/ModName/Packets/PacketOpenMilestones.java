package ModName.Packets;

import ModName.GUI.GUIDataMilestones;
import ModName.GUI.GUIFactoryMilestones;
import com.cleanroommc.modularui.factory.GuiManager;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenMilestones implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketOpenMilestones, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenMilestones message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            GuiManager.open(new GUIFactoryMilestones(), new GUIDataMilestones(player), player);
            return null;
        }
    }
}
