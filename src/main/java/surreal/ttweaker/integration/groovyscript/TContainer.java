package surreal.ttweaker.integration.groovyscript;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import com.cleanroommc.groovyscript.compat.mods.GroovyContainer;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

public class TContainer extends GroovyPropertyContainer implements GroovyPlugin {

    private final String name;
    private final String[] aliases;

    public TContainer(String name, String... aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public final @NotNull String getModId() {
        return this.aliases[0];
    }

    @Override
    public final @NotNull String getContainerName() {
        return this.name;
    }

    @Override
    public final @NotNull GroovyPropertyContainer createGroovyPropertyContainer() {
        return this;
    }

    @Override
    public final @NotNull Collection<String> getAliases() {
        return Arrays.asList(this.aliases);
    }

    @Override
    public void onCompatLoaded(GroovyContainer<?> container) {}
}
