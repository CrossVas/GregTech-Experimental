package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.core.GregTechMod;

public class ItemIntegratedCircuit extends ItemBase {

    public ItemIntegratedCircuit() {
        super("integrated_circuit");
        setFolder("component");
        setRegistryName("integrated_circuit");
        setTranslationKey("integrated_circuit");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }
}
