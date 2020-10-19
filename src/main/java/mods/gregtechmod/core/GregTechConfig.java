package mods.gregtechmod.core;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = GregTechMod.MODID, category = "")
@Config.LangKey(GregTechMod.MODID+".config.title")
@Mod.EventBusSubscriber(modid = GregTechMod.MODID)
public class GregTechConfig {

    @Config.LangKey(GregTechMod.MODID+".config.general")
    public static final General GENERAL = new General();

    @Config.LangKey(GregTechMod.MODID+".config.features")
    public static final Features FEATURES = new Features();

    @Config.LangKey(GregTechMod.MODID+".config.balance")
    public static final Balance BALANCE = new Balance();

    @Config.LangKey(GregTechMod.MODID+".config.machines")
    public static final Machines MACHINES = new Machines();

    public static class General {
        public final boolean connectedMachineCasingTextures = true;
        public final boolean dynamicCentrifugeAnimationSpeed = true;
        public final boolean hiddenOres = true;
    }

    public static class Features {
        public final int quantumChestMaxItemCount = 2000000000;
        public final int quantumTankCapacity = 2000000000;
        public final int digitalChestMaxItemCount = 32768;
        @Config.Comment("The centrifuge's animation speed depends on the amount of overclocker upgrades. The more you give, the faster it goes!")
        public final int upgradeStackSize = 4;
    }

    public static class Balance {
        @Config.Comment("Indicates the amount of universal steam per ic2 steam. This is used by the steam upgrade to fairly convert all kinds of steam to the same value.")
        public final double steamMultiplier = 1.6;
        @Config.Comment("Serves the same purpose as the above, for superheated steam.")
        public final double superHeatedSteamMultiplier = 0.5;
        @Config.Comment("Prevent MV and HV solar panel covers from overloading (and exploding) your machines")
        public final boolean solarPanelCoverOvervoltageProtection = false;
        public final float LVExplosionPower = 2;
        public final float MVExplosionPower = 3;
        public final float HVExplosionPower = 4;
        public final float EVExplosionPower = 5;
        public final float IVExplosionPower = 6;
    }

    public static class Machines {
        public final boolean constantNeedOfEnergy = true;
        public final boolean machineFlammable = true;
        @Config.Comment("Fire causes explosions")
        public final boolean machineFireExplosions = true;
        @Config.Comment("Wirefire on explosion")
        public final boolean machineWireFire = true;
        @Config.Comment("Rain causes explosions")
        public final boolean machineRainExplosions = true;
        @Config.Comment("Lightning causes explosions")
        public final boolean machineThunderExplosions = true;
        @Config.Comment("Nearby explosions cause machines to explode")
        public final boolean machineChainExplosions = true;
    }

    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(GregTechMod.MODID)) {
            ConfigManager.sync(GregTechMod.MODID, Config.Type.INSTANCE);
        }
    }
}
