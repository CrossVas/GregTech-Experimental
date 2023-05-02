package mods.gregtechmod.objects.items.tools;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.machine.IGregTechMachine;
import mods.gregtechmod.objects.items.base.ItemHammer;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHardHammer extends ItemHammer {

    public ItemHardHammer(String material, int durability, int entityDamage) {
        super(material, "hard_hammer", durability, entityDamage);
        this.effectiveAganist.add("minecraft:villager_golem");
        this.effectiveAganist.add("twilightforest:tower_golem");
        this.effectiveAganist.add("thaumcraft:golem");
        GregTechAPI.instance().registerHardHammer(this);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.attemptDamageItem(4, JavaUtil.RANDOM, null) ? ItemStack.EMPTY : copy;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(GtLocale.translateItem("hard_hammer.description_2"));
        tooltip.add(GtLocale.translateItem("hard_hammer.description_3"));
        tooltip.add(GtLocale.translateItem("hard_hammer.description_4"));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);

            if (te instanceof IGregTechMachine) {
                boolean input = ((IGregTechMachine) te).isInputEnabled();
                boolean output = ((IGregTechMachine) te).isOutputEnabled();

                if (input = !input) output = !output;
                ((IGregTechMachine) te).setInputEnabled(input);
                ((IGregTechMachine) te).setOutputEnabled(output);
                ItemStack stack = player.getHeldItem(hand);
                stack.damageItem(1, player);

                String enabled = GtLocale.translateGeneric("enabled");
                String disabled = GtLocale.translateGeneric("disabled");
                GtUtil.sendMessage(player, GtLocale.buildKey("generic", "hard_hammer", "auto_input"), input ? enabled : disabled, output ? enabled : disabled);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}
