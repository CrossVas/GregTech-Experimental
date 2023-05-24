package mods.gregtechmod.objects.blocks.teblocks.container;

import ic2.core.slot.SlotInvSlot;
import mods.gregtechmod.objects.blocks.teblocks.struct.TileEntityIndustrialGrinder;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class ContainerIndustrialGrinder extends ContainerMachineBase<TileEntityIndustrialGrinder> {

    public ContainerIndustrialGrinder(EntityPlayer player, TileEntityIndustrialGrinder base) {
        super(player, base);
        addSlotToContainer(new SlotInvSlot(base.inputSlot, 0, 34, 16));
        addSlotToContainer(new SlotInvSlot(base.secondaryInput, 0, 34, 34));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 0, 86, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 1, 104, 25));
        addSlotToContainer(new SlotInvSlot(base.outputSlot, 2, 122, 25));
        addSlotToContainer(new SlotInvSlot(base.fluidContainerOutput, 0, 140, 25));
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("fluidTank");
    }
}
