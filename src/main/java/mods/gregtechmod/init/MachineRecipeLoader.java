package mods.gregtechmod.init;

import com.google.common.base.CaseFormat;
import ic2.api.item.IC2Items;
import ic2.api.recipe.ICraftingRecipeManager;
import ic2.api.recipe.Recipes;
import ic2.core.block.machine.tileentity.TileEntityAssemblyBench;
import ic2.core.recipe.AdvRecipe;
import ic2.core.recipe.OreDictionaryEntries;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.util.IItemProvider;
import mods.gregtechmod.util.OptionalItemStack;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.ProfileDelegate;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineRecipeLoader {
    private static final ICraftingRecipeManager.AttributeContainer ATTRIBUTES = new ICraftingRecipeManager.AttributeContainer(true, false, true);

    public static void init() {
        registerMatterAmplifiers();
        addScrapboxDrops();
        loadRecyclerBlackList();
        registerMatterCraftingRecipes();
    }

    private static void registerMatterCraftingRecipes() {
        addUUCraftingTableRecipe("gemRuby", 2," UU", "UUU", "UU ");
        addUUCraftingTableRecipe("gemSapphire", 2, "UU ", "UUU", " UU");
        addUUCraftingTableRecipe("gemGreenSapphire", 2, " UU", "UUU", " UU");
        addUUCraftingTableRecipe("gemOlivine", 2, "UU ", "UUU", "UU ");
        addUUCraftingTableRecipe("dustZinc", 10, "   ", "U U", " U ");
        addUUCraftingTableRecipe("dustNickel", 10, " U ", "U U", "   ");
        addUUCraftingTableRecipe("dustSilver", 14, " U ", "UUU", "UUU");
        addUUCraftingTableRecipe("dustPlatinum", 1, "  U", "UUU", "UUU");
        addUUCraftingTableRecipe("dustTungsten", 6, "U  ", "UUU", "UUU");
        addUUCraftingTableRecipe("dustSmallOsmium", 1, "U U", "UUU", "U U");
        addUUCraftingTableRecipe("dustTitanium", 2, "UUU", " U ", " U ");
        addUUCraftingTableRecipe("dustAluminium", 16, " U ", " U ", "UUU");
        addUUCraftingTableRecipe("dustElectrotine", 12, "UUU", " U ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Items.BLAZE_ROD, 4), "U U", "UU ", "U U");
        addUUCraftingTableRecipe(new ItemStack(Items.LEATHER, 32), "U U", " U ", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Items.STRING, 32), "U U", "   ", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG, 8, 1), "U  ", "   ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG, 8, 2), "  U", "   ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG, 8, 3), "   ", "U  ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG2, 8), "   ", "  U", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG2, 8, 1), "   ", "   ", "U  ");

        // Ic2 Vanilla recipes
        addUUCraftingTableRecipe(new ItemStack(Blocks.LOG, 8), " U ", "   ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.STONE, 16), "   ", " U ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.STONEBRICK, 48, 3), "UU ", "UU ", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.GRASS, 16), "   ", "U  ", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.MYCELIUM, 24), "   ", "U U", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Blocks.MOSSY_COBBLESTONE, 16), "   ", " U ", "U U");
        addUUCraftingTableRecipe(new ItemStack(Items.CLAY_BALL, 48), "UU ", "U  ", "UU ");
        addUUCraftingTableRecipe(new ItemStack(Items.FLINT, 32), " U ", "UU ", "UU ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.GLASS, 32), " U ", "U U", " U ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.WOOL, 12), "U U", "   ", " U ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.SANDSTONE, 16), "   ", "  U", " U ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.CACTUS, 48), " U ", "UUU", "U U");
        addUUCraftingTableRecipe(new ItemStack(Blocks.SNOW, 16), "U U", "   ", "   ");
        addUUCraftingTableRecipe(new ItemStack(Items.SNOWBALL, 16), "   ", "   ", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Items.DYE, 32, 3), "UU ", "  U", "UU ");
        addUUCraftingTableRecipe(new ItemStack(Items.REEDS, 48), "U U", "U U", "U U");
        addUUCraftingTableRecipe(StackUtil.copyWithSize(IC2Items.getItem("misc_resource", "resin"), 21), "U U", "   ", "U U");
        addUUCraftingTableRecipe(new ItemStack(Blocks.VINE, 24), "U  ", "U  ", "U  ");
        addUUCraftingTableRecipe(IC2Items.getItem("misc_resource", "water_sheet"), "   ", " U ", " U ");
        addUUCraftingTableRecipe(IC2Items.getItem("misc_resource", "lava_sheet"), " U ", " U ", " U ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.OBSIDIAN, 12), "U U", "U U", "   ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.NETHERRACK, 16), "  U", " U ", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Blocks.GLOWSTONE, 8), " U ", "U U", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Items.GUNPOWDER, 15), "UUU", "U  ", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Items.BONE, 32), "U  ", "UU ", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Items.FEATHER, 32), " U ", " U ", "U U");
        addUUCraftingTableRecipe(new ItemStack(Items.DYE, 48), " UU", " UU", " U ");
        addUUCraftingTableRecipe(new ItemStack(Items.ENDER_PEARL), "UUU", "U U", " U ");
        addUUCraftingTableRecipe(new ItemStack(Items.COAL, 20), "  U", "U  ", "  U");
        addUUCraftingTableRecipe(StackUtil.copyWithSize(IC2Items.getItem("resource", "copper_ore"), 5), "  U", "U U", "   ");
        addUUCraftingTableRecipe(StackUtil.copyWithSize(IC2Items.getItem("resource", "tin_ore"), 5), "   ", "U U", "  U");
        addUUCraftingTableRecipe(new ItemStack(Blocks.IRON_ORE, 2), "U U", " U ", "U U");
        addUUCraftingTableRecipe(new ItemStack(Blocks.GOLD_ORE, 2), " U ", "UUU", " U ");
        addUUCraftingTableRecipe(StackUtil.copyWithSize(IC2Items.getItem("resource", "lead_ore"), 7), "UUU", "UUU", "U  ");
        addUUCraftingTableRecipe(new ItemStack(Items.DYE, 48, 4), " U ", " U ", " UU");
        addUUCraftingTableRecipe(new ItemStack(Items.REDSTONE, 24), "   ", " U ", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Items.DIAMOND), "UUU", "UUU", "UUU");
        addUUCraftingTableRecipe(new ItemStack(Blocks.EMERALD_ORE), "UU ", "U U", " UU");
        addUUCraftingTableRecipe(new ItemStack(Items.EMERALD, 2), "UUU", "UUU", " U ");
        addUUCraftingTableRecipe(IC2Items.getItem("misc_resource", "iridium_ore"), "UUU", " U ", "UUU");


        ItemStack[] input = new ItemStack[] { ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ModHandler.uuMatter, ModHandler.uuMatter, ModHandler.uuMatter };
        ModHandler.removeCraftingRecipeFromInputs(input);
        IRecipe recipe = ModHandler.getCraftingRecipe(TileEntityAssemblyBench.RECIPES, input);
        if (recipe != null) TileEntityAssemblyBench.RECIPES.remove(recipe);

        ItemStack dustPlutonium = BlockItems.Dust.PLUTONIUM.getItemStack();
        List<Object> pattern;
        if (GregTechAPI.getDynamicConfig("gregtech_recipes", "matterfabricator", true)
            && GregTechConfig.DISABLED_RECIPES.massFabricator
            && GregTechConfig.MACHINES.matterFabricationRate >= 10000000) {
            pattern = Arrays.asList("U", "R", 'R', "dustUranium");
        }
        else pattern = Arrays.asList("UUU", "URU", "UUU", 'R', "dustUranium");

        List<Object> patternWithMatter = new ArrayList<>(pattern);
        patternWithMatter.add('U');
        patternWithMatter.add("craftingUUMatter");
        addUUCraftingTableRecipe(dustPlutonium, pattern.toArray());
    }

    private static void registerMatterAmplifiers() {
        GregTechMod.LOGGER.info("Adding matter amplifiers");

        addMatterAmplifier("dustElectrotine", 5000);
        addMatterAmplifier("dustTungsten", 50000);
        addMatterAmplifier("dustManganese", 5000);
        addMatterAmplifier("dustRedstone", 5000);
        addMatterAmplifier("dustGlowstone", 25000);
        addMatterAmplifier("dustPlatinum", 100000);
        addMatterAmplifier("dustIridium", 100000);
        addMatterAmplifier("dustEnderPearl", 50000);
        addMatterAmplifier("dustEnderEye", 75000);
        addMatterAmplifier("dustOlivine", 50000);
        addMatterAmplifier("dustEmerald", 50000);
        addMatterAmplifier("dustDiamond", 125000);
        addMatterAmplifier("dustRuby", 50000);
        addMatterAmplifier("dustSapphire", 50000);
        addMatterAmplifier("dustGreenSapphire", 50000);
        addMatterAmplifier("dustUranium", 1000000);
        addMatterAmplifier("dustOsmium", 200000);
        addMatterAmplifier("dustPlutonium", 2000000);
        addMatterAmplifier("dustThorium", 500000);
    }

    private static void addMatterAmplifier(String ore, int amp) {
        Recipes.matterAmplifier.addRecipe(Recipes.inputFactory.forOreDict(ore), amp, null, true);
    }

    private static void addScrapboxDrops() {
        GregTechMod.LOGGER.info("Adding Scrapbox drops");

        addScrapboxDrop(IC2Items.getItem("crafting", "scrap"), 200);
        addScrapboxDrop(Items.WOODEN_AXE, 2);
        addScrapboxDrop(Items.WOODEN_SWORD, 2);
        addScrapboxDrop(Items.WOODEN_SHOVEL, 2);
        addScrapboxDrop(Items.WOODEN_PICKAXE, 2);
        addScrapboxDrop(Items.SIGN, 2);
        addScrapboxDrop(Items.STICK, 9.5F);
        addScrapboxDrop(new ItemStack(Blocks.PUMPKIN), 0.5F);
        addScrapboxDrop(Items.ROTTEN_FLESH, 9);
        addScrapboxDrop(Items.COOKED_PORKCHOP, 0.4F);
        addScrapboxDrop(Items.COOKED_BEEF, 0.4F);
        addScrapboxDrop(Items.COOKED_CHICKEN, 0.4F);
        addScrapboxDrop(Items.APPLE, 0.5F);
        addScrapboxDrop(Items.BREAD, 0.5F);
        addScrapboxDrop(Items.CAKE, 0.1F);
        addScrapboxDrop(IC2Items.getItem("filled_tin_can"), 1);
        addScrapboxDrop(ProfileDelegate.getCell("silicon"), 0.2F);
        addScrapboxDrop(ProfileDelegate.getCell("water"), 1);
        addScrapboxDrop(ProfileDelegate.getEmptyCell(), 2);
        addScrapboxDrop(Items.PAPER, 5);
        addScrapboxDrop(IC2Items.getItem("crafting", "plant_ball"), 0.7F);
        addScrapboxDrop(BlockItems.Dust.WOOD, 3.8F);
        addScrapboxDrop(IC2Items.getItem("single_use_battery"), 2.7F);
        addScrapboxDrop(BlockItems.Component.MACHINE_PARTS, 0.8F);
        addScrapboxDrop(BlockItems.Component.ADVANCED_CIRCUIT_PARTS, 1.2F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_BASIC, 1.8F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_ADVANCED, 0.4F);
        addScrapboxDrop(BlockItems.Component.CIRCUIT_BOARD_PROCESSOR, 0.2F);
        addScrapboxDrop(IC2Items.getItem("cable", "type:copper,insulation:1"), 2);
        addScrapboxDrop(IC2Items.getItem("cable", "type:gold,insulation:2"), 0.4F);
        addScrapboxDrop(BlockItems.Dust.CHARCOAL, 2.5F);
        addScrapboxDrop(IC2Items.getItem("dust", "iron"), 1);
        addScrapboxDrop(IC2Items.getItem("dust", "gold"), 1);
        addScrapboxDrop(BlockItems.Dust.SILVER, 0.5F);
        addScrapboxDrop(BlockItems.Dust.ELECTRUM, 0.5F);
        addScrapboxDrop(IC2Items.getItem("dust", "tin"), 1.2F);
        addScrapboxDrop(IC2Items.getItem("dust", "copper"), 1.2F);
        addScrapboxDrop(BlockItems.Dust.BAUXITE, 0.5F);
        addScrapboxDrop(BlockItems.Dust.ALUMINIUM, 0.5F);
        addScrapboxDrop(BlockItems.Dust.LEAD, 0.5F);
        addScrapboxDrop(BlockItems.Dust.NICKEL, 0.5F);
        addScrapboxDrop(BlockItems.Dust.ZINC, 0.5F);
        addScrapboxDrop(BlockItems.Dust.BRASS, 0.5F);
        addScrapboxDrop(BlockItems.Dust.STEEL, 0.5F);
        addScrapboxDrop(BlockItems.Dust.OBSIDIAN, 1.5F);
        addScrapboxDrop(IC2Items.getItem("dust", "sulfur"), 1.5F);
        addScrapboxDrop(BlockItems.Dust.SALTPETER, 2);
        addScrapboxDrop(BlockItems.Dust.LAZURITE, 2);
    }

    private static void addScrapboxDrop(IItemProvider item, float value) {
        addScrapboxDrop(item.getItemStack(), value);
    }

    private static void addScrapboxDrop(Item item, float value) {
        addScrapboxDrop(new ItemStack(item), value);
    }

    private static void addScrapboxDrop(ItemStack stack, float value) {
        Recipes.scrapboxDrops.addDrop(stack, value);
    }

    public static void loadRecyclerBlackList() {
        GregTechMod.LOGGER.info("Adding stuff to the Recycler blacklist");

        if (GregTechConfig.DISABLED_RECIPES.easyMobGrinderRecycling) {
            addToRecyclerBlacklist(Items.ARROW);
            addToRecyclerBlacklist(Items.BONE);
            addToRecyclerBlacklist(new ItemStack(Items.DYE, 1, 15));
            addToRecyclerBlacklist(Items.ROTTEN_FLESH);
            addToRecyclerBlacklist(Items.STRING);
            addToRecyclerBlacklist(Items.EGG);
        }

        if (GregTechConfig.DISABLED_RECIPES.easyStoneRecycling) {
            addToRecyclerBlacklist(Blocks.SAND);
            addToRecyclerBlacklist(new ItemStack(Blocks.SANDSTONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.RED_SANDSTONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(Blocks.GLASS);
            addToRecyclerBlacklist(Items.GLASS_BOTTLE);
            addToRecyclerBlacklist(Items.POTIONITEM);
            addToRecyclerBlacklist(Items.SPLASH_POTION);
            addToRecyclerBlacklist(Items.LINGERING_POTION);
            ItemStack stone = new ItemStack(Blocks.STONE);
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(FurnaceRecipes.instance().getSmeltingResult(stone));
            ModHandler.getRecipeOutput(stone, ItemStack.EMPTY, stone, ItemStack.EMPTY, stone)
                .ifPresent(MachineRecipeLoader::addToRecyclerBlacklist);
            if (ModHandler.buildcraftTransport) {
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_stone_item"));
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_cobble_item"));
                addToRecyclerBlacklist(ModHandler.getModItem("buildcrafttransport", "pipe_sandstone_item"));
            }
            addToRecyclerBlacklist(Blocks.GLASS_PANE);
            addToRecyclerBlacklist(Blocks.STAINED_GLASS);
            addToRecyclerBlacklist(Blocks.STAINED_GLASS_PANE);
            addToRecyclerBlacklist(Blocks.COBBLESTONE);
            addToRecyclerBlacklist(Blocks.COBBLESTONE_WALL);
            addToRecyclerBlacklist(Blocks.SANDSTONE_STAIRS);
            addToRecyclerBlacklist(Blocks.RED_SANDSTONE_STAIRS);
            addToRecyclerBlacklist(Blocks.STONE_STAIRS);
            addToRecyclerBlacklist(Blocks.STONE_BRICK_STAIRS);
            addToRecyclerBlacklist(Blocks.FURNACE);
            addToRecyclerBlacklist(new ItemStack(Blocks.WOODEN_SLAB, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_SLAB, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_SLAB2));
            addToRecyclerBlacklist(new ItemStack(Blocks.PURPUR_SLAB));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_PRESSURE_PLATE));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONE_BUTTON));
            addToRecyclerBlacklist(new ItemStack(Blocks.STONEBRICK, 1, OreDictionary.WILDCARD_VALUE));
            addToRecyclerBlacklist(Blocks.LEVER);
            Recipes.recyclerBlacklist.add(Recipes.inputFactory.forOreDict("rodStone"));
        }
        addToRecyclerBlacklist(Items.SNOWBALL);
        addToRecyclerBlacklist(Blocks.ICE);
        addToRecyclerBlacklist(Blocks.SNOW);
        addToRecyclerBlacklist(Blocks.SNOW_LAYER);
    }

    public static void addUUCraftingTableRecipe(String name, int count, Object... pattern) {
        OptionalItemStack output = OreDictUnificator.getFirstOre(name, count);
        if (output.isPresent()) {
            List<Object> args = new ArrayList<>(Arrays.asList(pattern));
            args.add('U');
            args.add("craftingUUMatter");
            args.add(ATTRIBUTES);
            Recipes.advRecipes.addRecipe(output.get(), args.toArray());
        }
    }

    private static void addUUCraftingTableRecipe(ItemStack output, Object... pattern) {
        List<Object> args = new ArrayList<>(Arrays.asList(pattern));
        args.add('U');
        args.add("craftingUUMatter");
        args.add(ATTRIBUTES);
        Recipes.advRecipes.addRecipe(output, args.toArray());
    }

    private static void addToRecyclerBlacklist(Block block) {
        addToRecyclerBlacklist(new ItemStack(block));
    }

    private static void addToRecyclerBlacklist(Item item) {
        addToRecyclerBlacklist(new ItemStack(item));
    }

    private static void addToRecyclerBlacklist(ItemStack stack) {
        if (!stack.isEmpty()) Recipes.recyclerBlacklist.add(Recipes.inputFactory.forStack(stack));
    }
}
