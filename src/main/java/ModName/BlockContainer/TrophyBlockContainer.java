package ModName.BlockContainer;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import ModName.ModName;
import ModName.TileEntity.TrophyTileEntity;

public class TrophyBlockContainer extends BlockContainer {

    public TrophyBlockContainer() {
        super(Material.wood);
        this.setHardness(1.5F);
        this.setBlockName("trophy");
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockTextureName(ModName.MODNAME + ":trophy");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TrophyTileEntity();
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, placer, stack);

        if (!world.isRemote && stack.hasTagCompound()) {
            TileEntity te = world.getTileEntity(x, y, z);

            if (te instanceof TrophyTileEntity) {
                TrophyTileEntity trophyTE = (TrophyTileEntity) te;

                if (stack.getTagCompound()
                    .hasKey("trophyitem")) {
                    trophyTE.item = stack.getTagCompound()
                        .getString("trophyitem");
                }
                if (stack.getTagCompound()
                    .hasKey("trophyownername")) {
                    trophyTE.ownerName = stack.getTagCompound()
                        .getString("trophyownername");
                }
                if (stack.getTagCompound()
                    .hasKey("trophyplaytime")) {
                    trophyTE.playtime = stack.getTagCompound()
                        .getInteger("trophyplaytime");
                }
                if (stack.getTagCompound()
                    .hasKey("trophydate")) {
                    trophyTE.date = stack.getTagCompound()
                        .getLong("trophydate");
                }
                trophyTE.markDirty();
                world.markBlockForUpdate(x, y, z);
            }
        }
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        ItemStack dropStack = new ItemStack(this);

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TrophyTileEntity) {
            TrophyTileEntity trophyTE = (TrophyTileEntity) te;

            if (trophyTE.item != null && !trophyTE.item.isEmpty()) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("trophyitem", trophyTE.item);
                nbt.setString("trophyownername", trophyTE.ownerName);
                nbt.setInteger("trophyplaytime", trophyTE.playtime);
                nbt.setLong("trophydate", trophyTE.date);
                dropStack.setTagCompound(nbt);
            }
        }

        drops.add(dropStack);
        return drops;
    }

    @Override
    public boolean removedByPlayer(World world, net.minecraft.entity.player.EntityPlayer player, int x, int y, int z,
        boolean willHarvest) {
        if (willHarvest) {
            return true;
        }

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void harvestBlock(World world, net.minecraft.entity.player.EntityPlayer player, int x, int y, int z,
        int meta) {
        super.harvestBlock(world, player, x, y, z, meta);
        world.setBlockToAir(x, y, z);
    }
}
