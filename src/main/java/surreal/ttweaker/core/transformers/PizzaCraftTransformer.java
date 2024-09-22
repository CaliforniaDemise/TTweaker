package surreal.ttweaker.core.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;
import surreal.ttweaker.core.Dumbformer;

import java.util.Iterator;

// TODO Add support for that cooker
/**
 * Add support for PizzaCraft
 **/
public class PizzaCraftTransformer extends Dumbformer {

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
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("onBlockActivated", "func_180639_a"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC) {
                        FieldInsnNode field = (FieldInsnNode) node;
                        if (field.name.equals("PEEL")) {
                            method.instructions.insert(node, hook("PizzaCraft$isPeel", "(Lnet/minecraft/item/ItemStack;)Z"));
                            method.instructions.remove(node.getPrevious());
                            iterator.remove();
                            ((JumpInsnNode) iterator.next()).setOpcode(IFEQ);
                        }
                        else if (field.name.equals("KNIFE")) {
                            method.instructions.insert(node, hook("PizzaCraft$isKnife", "(Lnet/minecraft/item/ItemStack;)Z"));
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
                        method.instructions.insert(node, hook("PizzaCraft$isKnife", "(Lnet/minecraft/item/ItemStack;)Z"));
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
                        method.instructions.insertBefore(node, hook("PizzaCraft$createMap", "()Ljava/util/Map;"));
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
            m.visitMethodInsn(INVOKESTATIC, "surreal/ttweaker/core/TTHooks", "PizzaCraft$MortarOnTake", desc, false);
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
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(hook("PizzaCraft$handleBakewareRecipes", method.desc));
                method.instructions.insertBefore(node, list);
            }
            else if (method.name.equals("handleMortarAndPestleRecipes")) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new VarInsnNode(ALOAD, 2));
                list.add(hook("PizzaCraft$handleMortarAndPestleRecipes", method.desc));
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

    public static byte[] transformMortarRecipeCategory(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        { // shaped | Shaped recipe drawable
            cls.visitField(ACC_PUBLIC | ACC_FINAL, "shaped", "Lmezz/jei/api/gui/IDrawable;", null, null);
        }
        for (MethodNode method : cls.methods) {
            if (method.name.equals("<init>")) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 1));
                list.add(new FieldInsnNode(GETSTATIC, cls.name, "BACKGROUND", "Lnet/minecraft/util/ResourceLocation;"));
                list.add(new IntInsnNode(BIPUSH, 10)); // 10 56
                list.add(new IntInsnNode(BIPUSH, 56)); // 52 104
                list.add(new IntInsnNode(BIPUSH, 52));
                list.add(new IntInsnNode(BIPUSH, 104));
                list.add(new MethodInsnNode(INVOKEINTERFACE, "net/jei/api/IGuiHelper", "createDrawable", "(Lnet/minecraft/util/ResourceLocation;IIII)Lmezz/jei/api/gui/IDrawableStatic;", true));
                list.add(new FieldInsnNode(PUTFIELD, cls.name, "shaped", "Lmezz/jei/api/gui/IDrawable;"));
                method.instructions.insertBefore(node, list);
                break;
            }
        }
        return write(cls);
    }
}
