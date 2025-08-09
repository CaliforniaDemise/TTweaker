package surreal.ttweaker.integration;

import com.cleanroommc.groovyscript.GroovyScript;
import net.minecraftforge.fml.common.Loader;
import surreal.ttweaker.integration.vanilla.BrewingFuel;

public class ModList {

    public static final String GROOVYSCRIPT_MODID = "groovyscript";

    public static final boolean GROOVYSCRIPT = Loader.isModLoaded(GROOVYSCRIPT_MODID);

    public static void preInit() {
        if (GROOVYSCRIPT) GroovyScript.getSandbox().registerBinding(new BrewingFuel.GroovyScript());
    }
}
