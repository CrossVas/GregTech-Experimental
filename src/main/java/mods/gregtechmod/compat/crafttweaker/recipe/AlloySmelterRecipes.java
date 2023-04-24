package mods.gregtechmod.compat.crafttweaker.recipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.IRecipeUniversal;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.crafttweaker.AddRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RecipeInputConverter;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeAction;
import mods.gregtechmod.compat.crafttweaker.RemoveRecipeByOutputAction;
import mods.gregtechmod.objects.items.tools.ItemSolderingTool;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ModOnly(Reference.MODID)
@ZenClass("mods.gregtechmod.AlloySmelter")
@ZenRegister
public class AlloySmelterRecipes {

    @ZenMethod
    public static void addRecipe(IIngredient[] inputs, IItemStack output, int duration, @Optional double energyCost, @Optional boolean universal) {
        List<IRecipeIngredient> inputIngredients = RecipeInputConverter.of(inputs);
        ItemStack outputStack = CraftTweakerMC.getItemStack(output);
        IRecipeUniversal<List<IRecipeIngredient>> recipe = GregTechAPI.getRecipeFactory().makeAlloySmelterRecipe(inputIngredients, outputStack, duration, energyCost, universal);
        CraftTweakerAPI.apply(new AddRecipeAction<>(GtRecipes.alloySmelter, recipe));
    }

    @ZenMethod
    public static void removeRecipe(IItemStack[] inputs) {
        List<ItemStack> inputStacks = Arrays.asList(CraftTweakerMC.getItemStacks(inputs));
        CraftTweakerAPI.apply(new RemoveRecipeAction<>(GtRecipes.alloySmelter, GtRecipes.alloySmelter.getRecipeFor(inputStacks)));
    }

    @ZenMethod    
    public static void removeByOutput(IItemStack output) {
        ItemStack stack = CraftTweakerMC.getItemStack(output);
        CraftTweakerAPI.apply(new RemoveRecipeByOutputAction<>(GtRecipes.alloySmelter, stacks -> stacks.stream().anyMatch(stack::isItemEqual)));
    }
}
