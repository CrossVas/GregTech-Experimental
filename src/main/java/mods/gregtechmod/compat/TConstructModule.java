package mods.gregtechmod.compat;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.OreDictUnificator;
import mods.gregtechmod.init.OreDictHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

@Mod.EventBusSubscriber
public class TConstructModule {

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkerRegisterBasinCastingRegister(TinkerRegisterEvent.BasinCastingRegisterEvent event) {
        event.setCanceled(unifyCastingRecipe("basin_casting", event.getRecipe()));
    }

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkerRegisterTableCastingRegister(TinkerRegisterEvent.TableCastingRegisterEvent event) {
        event.setCanceled(unifyCastingRecipe("table_casting", event.getRecipe()));
    }

    private static boolean unifyCastingRecipe(String type, ICastingRecipe recipe) {
        if (recipe instanceof CastingRecipe) {
            ItemStack output = ((CastingRecipe) recipe).getResult();
            if (!output.isEmpty()) {
                String name = OreDictUnificator.getAssociation(output);
                String unifiedName = OreDictHandler.GT_ORE_NAMES.get(name);
                if (unifiedName != null) name = unifiedName;

                ItemStack unified = OreDictUnificator.getUnifiedOre(name);
                if (!unified.isEmpty() && !output.isItemEqual(unified) && GregTechAPI.getDynamicConfig(type, name, true)) {
                    TinkerRegistry.registerBasinCasting(new CastingRecipe(unified, ((CastingRecipe) recipe).cast, ((CastingRecipe) recipe).getFluid(), false, false));
                    return true;
                }
            }
        }
        return false;
    }
}
