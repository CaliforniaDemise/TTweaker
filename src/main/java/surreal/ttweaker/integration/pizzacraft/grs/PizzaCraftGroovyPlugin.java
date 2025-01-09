package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class PizzaCraftGroovyPlugin implements GroovyPlugin {

    @Override
    public @NotNull String getModId() {
        return "pizzacraft";
    }

    @Override
    public @NotNull String getContainerName() {
        return "PizzaCraft";
    }

    @Override
    public @Nullable GroovyPropertyContainer createGroovyPropertyContainer() {
        return new PizzaCraftContainer();
    }

    @Override
    public @NotNull Collection<String> getAliases() {
        ImmutableList.Builder<String> list = new ImmutableList.Builder<>();
        list.add("pizzacraft");
        list.add("pc");
        return list.build();
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {

    }
}
