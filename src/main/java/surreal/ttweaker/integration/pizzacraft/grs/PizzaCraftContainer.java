package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import surreal.ttweaker.integration.pizzacraft.Bakeware;
import surreal.ttweaker.integration.pizzacraft.ChoppingBoard;
import surreal.ttweaker.integration.pizzacraft.Mortar;

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
