package surreal.ttweaker.core.transformers;

import com.tiviacz.pizzacraft.tileentity.TileEntityMortarAndPestle;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import surreal.ttweaker.core.Dumbformer;

import java.util.Iterator;
import java.util.List;

// TODO Add support for that cooker
/**
 * Add support for PizzaCraft
 **/
public class PizzaCraftTransformer extends Dumbformer {

    private static final String HOOKS = "surreal/ttweaker/core/transformers/PizzaCraftTransformer$Hooks";

    // Transform CommonProxy to load ore dict entries early. They somehow initialize after CraftTweaker adds the recipes.
    public static byte[] transformCommonProxy(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("initRegistries")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKESTATIC && ((MethodInsnNode) node).name.equals("registerOres")) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
        return write(cls);
    }

    // Load ore dictionary entries after items get registered.
    public static byte[] transformCommonEventHandler(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
           if (method.name.equals("onItemRegister")) {
               AbstractInsnNode node = method.instructions.getLast();
               while (node.getOpcode() != RETURN) node = node.getPrevious();
               method.instructions.insertBefore(node, new MethodInsnNode(INVOKESTATIC, "com/tiviacz/pizzacraft/init/OreDictInit", "registerOres", "()V", false));
               break;
           }
        }
        return write(cls);
    }

    public static byte[] transformBlockPizza(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        { // TT$isItem
            MethodVisitor m = cls.visitMethod(ACC_PUBLIC | ACC_STATIC, "TT$isItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item;Ljava/lang/String;)Z", null, null);
            {
                m.visitVarInsn(ALOAD, 0);
                m.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getName("isEmpty", "func_190926_b"), "()Z", false);
                Label l_con = new Label();
                m.visitJumpInsn(IFEQ, l_con);
                m.visitInsn(ICONST_0);
                m.visitInsn(IRETURN);
                m.visitLabel(l_con);
                m.visitFrame(F_SAME, 0, null, 0, null);
            }
            {
                m.visitVarInsn(ALOAD, 0);
                m.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getName("getItem", "func_77973_b"), "()Lnet/minecraft/item/Item;", false);
                m.visitVarInsn(ALOAD, 1);
                Label l_con = new Label();
                m.visitJumpInsn(IF_ACMPNE, l_con);
                m.visitInsn(ICONST_1);
                m.visitInsn(IRETURN);
                m.visitLabel(l_con);
                m.visitFrame(F_SAME, 0, null, 0 ,null);
            }
            m.visitVarInsn(ALOAD, 0);
            m.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/oredict/OreDictionary", "getOreIDs", "(Lnet/minecraft/item/ItemStack;)[I", false);
            m.visitVarInsn(ASTORE, 3);
            m.visitInsn(ICONST_0);
            m.visitVarInsn(ISTORE, 4);
            m.visitVarInsn(ALOAD, 2);
            m.visitMethodInsn(INVOKESTATIC, "net/minecraftforge/oredict/OreDictionary", "getOreID", "(Ljava/lang/String;)I", false);
            m.visitVarInsn(ISTORE, 5);
            Label l_con_goto = new Label();
            m.visitLabel(l_con_goto);
            m.visitFrame(F_APPEND, 3, new Object[] { "[I", INTEGER, INTEGER }, 0, null);
            m.visitVarInsn(ILOAD, 4);
            m.visitVarInsn(ALOAD, 3);
            m.visitInsn(ARRAYLENGTH);
            Label l_con = new Label();
            m.visitJumpInsn(IF_ICMPGE, l_con);
            m.visitVarInsn(ALOAD, 3);
            m.visitVarInsn(ILOAD, 4);
            m.visitInsn(IALOAD);
            m.visitVarInsn(ILOAD, 5);
            Label l_con_ore = new Label();
            m.visitJumpInsn(IF_ICMPNE, l_con_ore);
            m.visitInsn(ICONST_1);
            m.visitInsn(IRETURN);
            m.visitLabel(l_con_ore);
            m.visitFrame(F_SAME, 0, null, 0, null);
            m.visitVarInsn(ILOAD, 4);
            m.visitInsn(ICONST_1);
            m.visitInsn(IADD);
            m.visitVarInsn(ISTORE, 4);
            m.visitLabel(l_con);
            m.visitFrame(F_CHOP, 3, null, 0, null);
            m.visitInsn(ICONST_0);
            m.visitInsn(IRETURN);
        }
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("onBlockActivated", "func_180639_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC) {
                        FieldInsnNode field = (FieldInsnNode) node;
                        if (field.name.equals("PEEL")) {
                            InsnList list = new InsnList();
                            list.add(new FieldInsnNode(GETSTATIC, "com/tiviacz/pizzacraft/init/ModItems", "PEEL", "Lnet/minecraft/item/Item;"));
                            list.add(new LdcInsnNode("toolPeel"));
                            list.add(new MethodInsnNode(INVOKESTATIC, cls.name, "TT$isItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item;Ljava/lang/String;)Z", false));
                            method.instructions.insert(node, list);
                            method.instructions.remove(node.getPrevious());
                            iterator.remove();
                            ((JumpInsnNode) iterator.next()).setOpcode(IFEQ);
                        }
                        else if (field.name.equals("KNIFE")) {
                            InsnList list = new InsnList();
                            list.add(new FieldInsnNode(GETSTATIC, "com/tiviacz/pizzacraft/init/ModItems", "KNIFE", "Lnet/minecraft/item/Item;"));
                            list.add(new LdcInsnNode("toolKnife"));
                            list.add(new MethodInsnNode(INVOKESTATIC, cls.name, "TT$isItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item;Ljava/lang/String;)Z", false));
                            method.instructions.insert(node, list);
                            method.instructions.remove(node.getPrevious());
                            iterator.remove();
                            ((JumpInsnNode) iterator.next()).setOpcode(IFEQ);
                            break;
                        }
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformBlockChoppingBoard(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("onBlockActivated", "func_180639_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC && ((FieldInsnNode) node).name.equals("KNIFE")) {
                        InsnList list = new InsnList();
                        list.add(new FieldInsnNode(GETSTATIC, "com/tiviacz/pizzacraft/init/ModItems", "KNIFE", "Lnet/minecraft/item/Item;"));
                        list.add(new LdcInsnNode("toolKnife"));
                        list.add(new MethodInsnNode(INVOKESTATIC, "com/tiviacz/pizzacraft/blocks/BlockPizza", "TT$isItem", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item;Ljava/lang/String;)Z", false));
                        method.instructions.insert(node, list);
                        method.instructions.remove(node.getPrevious());
                        iterator.remove();
                        ((JumpInsnNode) iterator.next()).setOpcode(IFEQ);
                        i++;
                        if (i == 2) break;
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformChoppingBoardRecipes(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        Iterator<MethodNode> methodIterator = cls.methods.iterator();
        while (methodIterator.hasNext()) {
            MethodNode method = methodIterator.next();
            if (method.name.equals("<init>")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKESTATIC) {
                        InsnList list = new InsnList();
                        list.add(new TypeInsnNode(NEW, "surreal/ttweaker/util/ItemStackMap"));
                        list.add(new InsnNode(DUP));
                        list.add(new FieldInsnNode(GETSTATIC, "net/minecraft/item/ItemStack", getName("EMPTY", "field_190927_a"), "Lnet/minecraft/item/ItemStack;"));
                        list.add(new MethodInsnNode(INVOKESPECIAL, "surreal/ttweaker/util/ItemStackMap", "<init>", "(Ljava/lang/Object;)V", false));
                        method.instructions.insertBefore(node, list);
                        iterator.remove();
                        break;
                    }
                }
            }
            else if (method.name.equals("getChoppingResult")) {
                methodIterator.remove();
                break;
            }
        }
        { // getChoppingResult
            MethodVisitor m = cls.visitMethod(ACC_PUBLIC, "getChoppingResult", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", null, null);
            m.visitVarInsn(ALOAD, 0);
            m.visitFieldInsn(GETFIELD, cls.name, "choppingList", "Ljava/util/Map;");
            m.visitVarInsn(ALOAD, 1);
            m.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", true);
            m.visitTypeInsn(CHECKCAST, "net/minecraft/item/ItemStack");
            m.visitInsn(ARETURN);
        }
        return write(cls);
    }

    public static byte[] transformChoppingBoardUtils(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        Iterator<MethodNode> iterator = cls.methods.iterator();
        while (iterator.hasNext()) {
            MethodNode method = iterator.next();
            if (method.name.equals("isItemValid")) {
                iterator.remove();
                break;
            }
        }
        { // isItemValid
            MethodVisitor m = cls.visitMethod(ACC_PUBLIC | ACC_STATIC, "isItemValid", "(Lnet/minecraft/item/ItemStack;)Z", null, null);
            m.visitMethodInsn(INVOKESTATIC, "com/tiviacz/pizzacraft/crafting/chopping/ChoppingBoardRecipes", "instance", "()Lcom/tiviacz/pizzacraft/crafting/chopping/ChoppingBoardRecipes;", false);
            m.visitVarInsn(ALOAD, 0);
            m.visitMethodInsn(INVOKEVIRTUAL, "com/tiviacz/pizzacraft/crafting/chopping/ChoppingBoardRecipes", "getChoppingResult", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false);
            m.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", getName("isEmpty", "func_190926_b"), "()Z", false);
            Label l_con = new Label();
            m.visitJumpInsn(IFNE, l_con);
            m.visitLabel(new Label());
            m.visitInsn(ICONST_1);
            m.visitInsn(IRETURN);
            m.visitLabel(l_con);
            m.visitFrame(F_SAME, 0, null, 0, null);
            m.visitInsn(ICONST_0);
            m.visitInsn(IRETURN);
        }
        return write(cls);
    }

    public static byte[] transformMortarRecipeUtils(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        Iterator<MethodNode> iterator = cls.methods.iterator();
        String desc = "";
        while (iterator.hasNext()) {
            MethodNode method = iterator.next();
            if (method.name.equals("onTake")) {
                desc = method.desc;
                iterator.remove();
                break;
            }
        }
        { // onTake
            MethodVisitor m = cls.visitMethod(ACC_PUBLIC | ACC_STATIC, "onTake", desc, null, null);
            m.visitVarInsn(ALOAD, 0);
            m.visitVarInsn(ALOAD, 1);
            m.visitMethodInsn(INVOKESTATIC, HOOKS, "$MortarOnTake", desc, false);
            m.visitInsn(RETURN);
        }
        return write(cls);
    }

    public static byte[] transformPizzaCraftPlugin(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("handleBakewareRecipes")) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new LdcInsnNode(Type.getType("Lsurreal/ttweaker/integration/pizzacraft/impl/BakewareRecipe;")));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "mezz/jei/api/IModRegistry", "getJeiHelpers", "()Lmezz/jei/api/IJeiHelpers;", true));
                list.add(new MethodInsnNode(INVOKESTATIC, "surreal/ttweaker/integration/pizzacraft/jei/BakewareWrapper", "factory", "(Lmezz/jei/api/IJeiHelpers;)Lmezz/jei/api/recipe/IRecipeWrapperFactory;", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "mezz/jei/api/IModRegistry", "handleRecipes", "(Ljava/lang/Class;Lmezz/jei/api/recipe/IRecipeWrapperFactory;Ljava/lang/String;)V", true));
                method.instructions.insertBefore(node, list);
            }
            else if (method.name.equals("handleMortarAndPestleRecipes")) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new LdcInsnNode(Type.getType("Lsurreal/ttweaker/integration/pizzacraft/impl/MortarRecipe;")));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "mezz/jei/api/IModRegistry", "getJeiHelpers", "()Lmezz/jei/api/IJeiHelpers;", true));
                list.add(new MethodInsnNode(INVOKESTATIC, "surreal/ttweaker/integration/pizzacraft/jei/MortarWrapper", "factory", "(Lmezz/jei/api/IJeiHelpers;)Lmezz/jei/api/recipe/IRecipeWrapperFactory;", false));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "mezz/jei/api/IModRegistry", "handleRecipes", "(Ljava/lang/Class;Lmezz/jei/api/recipe/IRecipeWrapperFactory;Ljava/lang/String;)V", true));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformJEIUtils(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("setMortarRecipe")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == BIPUSH) {
                        IntInsnNode intsn = (IntInsnNode) node;
                        int value = intsn.operand;
                        if (value == -5) intsn.operand = -2;
                        else if (value == 30) intsn.operand = 37;
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    @SuppressWarnings("unused")
    public static class Hooks {
        public static void $MortarOnTake(TileEntityMortarAndPestle te, List<ItemStack> list) {
            World world = te.getWorld();
            BlockPos pos = te.getPos();
            ItemStackHandler inv = te.getInventory();
            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack invStack = inv.getStackInSlot(i);
                if (invStack.isEmpty()) continue;
                invStack.shrink(1);
                ItemStack remaining = list.get(i);
                if (!remaining.isEmpty()) {
                    remaining = inv.insertItem(i, remaining, false);
                }
                if (!remaining.isEmpty() && !world.isRemote) {
                    EntityItem entity = new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, remaining);
                    world.spawnEntity(entity);
                }
            }
        }
    }
}
