package mods.gregtechmod.api.recipe;

import mods.gregtechmod.api.recipe.manager.IRecipeManagerCentrifuge;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GtRecipes {
    public static IRecipeManagerCentrifuge industrial_centrifuge;
    public static IGtRecipeManager<List<ItemStack>, IGtMachineRecipe<List<ItemStack>, ItemStack>> assembler;
}
