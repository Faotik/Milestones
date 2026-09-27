package Milestones;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class Utils {

    private static final long TICKS_IN_SECOND = 20;
    private static final long TICKS_IN_MINUTES = TICKS_IN_SECOND * 60;
    private static final long TICKS_IN_HOURS = TICKS_IN_MINUTES * 60;

    public static EntityPlayerMP getPlayerByUUID(UUID targetUUID) {
        MinecraftServer server = MinecraftServer.getServer();

        if (server != null && server.getConfigurationManager() != null) {
            for (EntityPlayerMP player : server.getConfigurationManager().playerEntityList) {
                if (player.getUniqueID()
                    .equals(targetUUID)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static String getTimeString(long timeTicks) {
        String timeString;
        if (timeTicks < TICKS_IN_MINUTES) {
            timeString = (timeTicks / TICKS_IN_SECOND) + "s";
        } else if (timeTicks < TICKS_IN_HOURS) {
            timeString = (timeTicks / TICKS_IN_MINUTES) + "m "
                + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND)
                + "s";
        } else {
            timeString = (timeTicks / TICKS_IN_HOURS) + "h "
                + ((timeTicks % TICKS_IN_HOURS) / TICKS_IN_MINUTES)
                + "m "
                + ((timeTicks % TICKS_IN_MINUTES) / TICKS_IN_SECOND)
                + "s";
        }
        return timeString;
    }

    public static ItemStack getItemStackFromId(String id) {
        String[] parts = id.split(":");
        if (parts.length < 2) {
            return null;
        }
        String modid = parts[0];
        String name = parts[1];
        int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        Item item = GameRegistry.findItem(modid, name);

        if (item == null) {
            return null;
        }

        return new ItemStack(item, 1, meta);
    }

    public static FluidStack getFluidStackFromId(String id) {
        return FluidRegistry.getFluidStack(id, 1);
    }

    public static String getIdAndMeta(ItemStack stack) {
        int meta = stack.getItemDamage();
        String id = GameRegistry.findUniqueIdentifierFor(stack.getItem())
            .toString();
        return meta == 0 ? id : id + ":" + meta;
    }
}
