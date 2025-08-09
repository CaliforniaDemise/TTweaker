package surreal.ttweaker.integration.pizzacraft.grs;

import com.cleanroommc.groovyscript.api.GroovyPlugin;
import surreal.ttweaker.integration.groovyscript.TContainer;
import surreal.ttweaker.integration.pizzacraft.Bakeware;
import surreal.ttweaker.integration.pizzacraft.ChoppingBoard;
import surreal.ttweaker.integration.pizzacraft.Mortar;

@SuppressWarnings("unused")
public class PizzaCraftContainer extends TContainer implements GroovyPlugin {

    public final Bakeware.GroovyScript bakeware = new Bakeware.GroovyScript();
    public final ChoppingBoard.GroovyScript choppingBoard = new ChoppingBoard.GroovyScript();
    public final Mortar.GroovyScript mortar = new Mortar.GroovyScript();

    public PizzaCraftContainer() {
        super("PizzaCraft", "pizzacraft", "pc");
        this.addProperty(bakeware);
        this.addProperty(choppingBoard);
        this.addProperty(mortar);
    }
}
