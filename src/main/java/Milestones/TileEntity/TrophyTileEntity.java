package Milestones.TileEntity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TrophyTileEntity extends TileEntity {

    public String item = "";
    public String ownerName = "";
    public int playtime = 0;
    public long date = 0;

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.item = nbt.getString("trophyitem");
        this.ownerName = nbt.getString("trophyownername");
        this.playtime = nbt.getInteger("trophyplaytime");
        this.date = nbt.getLong("trophydate");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setString("trophyitem", this.item);
        nbt.setString("trophyownername", this.ownerName);
        nbt.setInteger("trophyplaytime", this.playtime);
        nbt.setLong("trophydate", this.date);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }
}
