package surreal.ttweaker.integrations.pizzacraft.grs;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import surreal.ttweaker.integrations.pizzacraft.Bakeware;
import surreal.ttweaker.integrations.pizzacraft.ChoppingBoard;
import surreal.ttweaker.integrations.pizzacraft.Mortar;

public class PizzaCraftContainer extends GroovyPropertyContainer {

    private final Bakeware.GroovyScript bakeware = new Bakeware.GroovyScript();
    private final ChoppingBoard.GroovyScript choppingBoard = new ChoppingBoard.GroovyScript();
    private final Mortar.GroovyScript mortar = new Mortar.GroovyScript();

    public PizzaCraftContainer() {
        this.addProperty(bakeware);
        this.addProperty(choppingBoard);
        this.addProperty(mortar);
    }
}
