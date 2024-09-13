package surreal.ttweaker;

import crafttweaker.CraftTweakerAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import surreal.ttweaker.crafttweaker.BrewingFuel;
import surreal.ttweaker.crafttweaker.expansions.*;

@Mod(modid = TTweaker.MODID, name = "TTweaker", version = Tags.VERSION, dependencies = "required-after:crafttweaker;after:pizzacraft")
public class TTweaker {

    public static final String MODID = "ttweaker";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        register(
                BrewingFuel.class,

                BlockDefinitionExpansion.class,
                GameExpansion.class,
                ItemDefinitionExpansion.class,
                LiquidDefinitionExpansion.class,
                LiquidStackExpansion.class,
                WorldExpansions.class
        );
    }

    private void register(Class<?>... classes) {

        for (Class<?> cls : classes) {
            CraftTweakerAPI.registerClass(cls);
        }
    }
}
