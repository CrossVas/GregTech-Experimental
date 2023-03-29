package mods.gregtechmod.compat.jei;

import ic2.core.profile.Version;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.recipe.GtRecipes;
import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.compat.jei.category.*;
import mods.gregtechmod.gui.*;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.GregTechTEBlock;
import mods.gregtechmod.objects.items.ItemCellClassic;
import mods.gregtechmod.recipe.RecipeAlloySmelter;
import mods.gregtechmod.recipe.RecipeCanner;
import mods.gregtechmod.recipe.RecipeDualInput;
import mods.gregtechmod.recipe.RecipeSimple;
import mods.gregtechmod.recipe.util.DamagedOreIngredientFixer;
import mods.gregtechmod.util.IItemProvider;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import one.util.streamex.StreamEx;

import java.util.*;

@JEIPlugin
public class JEIModule implements IModPlugin {
    private static final Collection<CategoryBase<?, ?>> CATEGORIES = new HashSet<>();

    public static final List<ItemStack> HIDDEN_ITEMS = new ArrayList<>();
    public static final List<IRecipeWrapper> HIDDEN_RECIPES = new ArrayList<>();
    public static IIngredientBlacklist ingredientBlacklist;

    @Override
    public void register(IModRegistry registry) {
        ingredientBlacklist = registry.getJeiHelpers().getIngredientBlacklist();

        CATEGORIES.forEach(category -> category.init(registry));

        initBasicMachine(registry, GuiAutoMacerator.class, "macerator");
        initBasicMachine(registry, GuiAutoExtractor.class, "extractor");
        initBasicMachine(registry, GuiAutoCompressor.class, "compressor");
        initBasicMachine(registry, GuiAutoElectricFurnace.class, "auto_electric_furnace", "minecraft.smelting");

        registry.addRecipeClickArea(GuiScrapboxinator.class, 62, 63, 16, 16, "ic2.scrapbox");
    }

    private void initBasicMachine(IModRegistry registry, Class<? extends GuiBasicMachine<?>> guiClass, String name) {
        initBasicMachine(registry, guiClass, "auto_" + name, name);
    }

    private void initBasicMachine(IModRegistry registry, Class<? extends GuiBasicMachine<?>> guiClass, String name, String categoryName) {
        registry.addRecipeClickArea(guiClass, 78, 24, 18, 18, categoryName);
        registry.addRecipeCatalyst(GregTechObjectAPI.getTileEntity(name), categoryName);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        Collection<CategoryBase<?, ?>> categories = Arrays.asList(
            new CategoryCentrifuge(guiHelper),
            new CategoryBasicMachineSingle<>("wiremill", RecipeSimple.class, GuiWiremill.class, GtRecipes.wiremill, true, GregtechGauge.EXTRUDING, guiHelper),
            new CategoryBasicMachineMulti<>("alloy_smelter", RecipeAlloySmelter.class, GuiAlloySmelter.class, GtRecipes.alloySmelter, true, GregtechGauge.SMELTING, guiHelper),
            new CategoryBasicMachineMulti<>("auto_canner", RecipeCanner.class, GuiAutoCanner.class, GtRecipes.canner, false, true, GregtechGauge.CANNING, guiHelper),
            new CategoryBasicMachineMulti<>("bender", RecipeDualInput.class, GuiBender.class, GtRecipes.bender, true, GregtechGauge.BENDING, guiHelper),
            new CategoryBasicMachineMulti<>("assembler", RecipeDualInput.class, GuiAssembler.class, GtRecipes.assembler, true, GregtechGauge.ASSEMBLING, guiHelper),
            new CategoryLathe(guiHelper),
            new CategoryElectrolyzer(guiHelper),
            new CategoryChemicalReactor(guiHelper),
            new CategoryIndustrialBlastFurnace(guiHelper),
            new CategoryIndustrialGrinder(guiHelper),
            new CategoryIndustrialSawmill(guiHelper),
            new CategoryImplosionCompressor(guiHelper),
            new CategoryVacuumFreezer(guiHelper),
            new CategoryDistillationTower(guiHelper),
            CategoryGenerator.createFluidGeneratorCategory("thermal_generator", GuiThermalGenerator.class, GtFuels.hot, guiHelper),
            CategoryGenerator.createFluidGeneratorCategory("diesel_generator", GuiDieselGenerator.class, GtFuels.diesel, guiHelper),
            CategoryGenerator.createFluidGeneratorCategory("semifluid_generator", GuiSemifluidGenerator.class, GtFuels.denseLiquid, guiHelper),
            CategoryGenerator.createFluidGeneratorCategory("gas_turbine", GuiGasTurbine.class, GtFuels.gas, guiHelper),
            new CategoryPlasmaGenerator(guiHelper),
            CategoryGenerator.createFluidGeneratorCategory("magic_energy_converter", GuiMagicEnergyConverter.class, GtFuels.magic, guiHelper),
            new CategoryFusionReactor(guiHelper)
        );
        CATEGORIES.addAll(categories);
        registry.addRecipeCategories(categories.toArray(new IRecipeCategory[0]));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        hideEnum(BlockItems.Plate.values());
        hideEnum(BlockItems.Rod.values());

        if (!Version.shouldEnable(ItemCellClassic.class)) {
            BlockItems.classicCells.values().stream()
                .map(ItemStack::new)
                .forEach(HIDDEN_ITEMS::add);
        }

        if (!ModHandler.buildcraftLib) {
            HIDDEN_ITEMS.add(BlockItems.Upgrade.PNEUMATIC_GENERATOR.getItemStack());
            HIDDEN_ITEMS.add(BlockItems.Upgrade.RS_ENERGY_CELL.getItemStack());
        }

        StreamEx.of(GregTechTEBlock.VALUES)
            .remove(teBlock -> Version.shouldEnable(teBlock.getTeClass()))
            .map(GregTechTEBlock::getSimpleName)
            .map(GregTechObjectAPI::getTileEntity)
            .forEach(HIDDEN_ITEMS::add);

        HIDDEN_ITEMS.forEach(ingredientBlacklist::addIngredientToBlacklist);

        IRecipeRegistry registry = jeiRuntime.getRecipeRegistry();
        StreamEx.of(DamagedOreIngredientFixer.fixedRecipes)
            .mapPartial(recipe -> Optional.ofNullable(registry.getRecipeWrapper(recipe, VanillaRecipeCategoryUid.CRAFTING)))
            .forEach(HIDDEN_RECIPES::add);

        // Hide the data orb clean recipe
        IRecipe dataOrbRepair = ForgeRegistries.RECIPES.getValue(new ResourceLocation(Reference.MODID, "components/data_orb_clean"));
        if (dataOrbRepair != null)
            HIDDEN_RECIPES.add(registry.getRecipeWrapper(dataOrbRepair, VanillaRecipeCategoryUid.CRAFTING));

        HIDDEN_RECIPES.forEach(recipe -> registry.hideRecipe(recipe, VanillaRecipeCategoryUid.CRAFTING));
    }

    private void hideEnum(IItemProvider[] values) {
        StreamEx.of(values)
            .remove(ProfileDelegate::shouldEnable)
            .map(IItemProvider::getItemStack)
            .forEach(HIDDEN_ITEMS::add);
    }
}
