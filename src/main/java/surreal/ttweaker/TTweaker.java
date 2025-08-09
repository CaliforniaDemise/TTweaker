package surreal.ttweaker;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import surreal.ttweaker.integration.ModList;

@Mod(modid = TTweaker.MODID, name = "TTweaker", version = Tags.VERSION, dependencies = "after:crafttweaker;after:groovyscript;after:pizzacraft")
public class TTweaker {

    public static final String MODID = "ttweaker";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModList.preInit();
    }
}
