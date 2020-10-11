package mods.gregtechmod.objects.blocks.tileentities.machines;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.api.BlockItems;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.objects.blocks.tileentities.machines.base.TileEntityDigitalChestBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TileEntityQuantumChest extends TileEntityDigitalChestBase {

    public TileEntityQuantumChest() {
        super(GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
    }

    public TileEntityQuantumChest(ItemStack storedItems, boolean isPrivate, @Nullable GameProfile owner) {
        super(GregTechConfig.FEATURES.quantumChestMaxItemCount, true);
        this.content.put(storedItems);
        if (isPrivate && owner != null) {
            this.isPrivate = true;
            this.owner = owner;
            this.upgradeSlot.put(new ItemStack((Item) BlockItems.Upgrades.machine_lock.getInstance()));
        }
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
    }
}