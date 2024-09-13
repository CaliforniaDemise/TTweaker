package surreal.ttweaker.core;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

// 3 T's
public class TTTransformer implements IClassTransformer, Opcodes {

    private final Map<String, Function<byte[], byte[]>> functions;

    private final int COMPUTE_ALL = ClassWriter.COMPUTE_MAXS;
    private final boolean deobf = FMLLaunchHandler.isDeobfuscatedEnvironment();
    private final Logger logger = LogManager.getLogger("TTweaker");

    private final String HOOKS = "surreal/ttweaker/core/TTHooks";

    public TTTransformer() {
        this.functions = new Object2ObjectOpenHashMap<>();
        put("net.minecraft.tileentity.TileEntityBrewingStand", this::transformTEBrewingStand);
        put("net.minecraft.inventory.ContainerBrewingStand", this::transformContainerBrewingStand);

        put("com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardRecipes", this::transformChoppingBoardRecipes);
        put("com.tiviacz.pizzacraft.blocks.BlockPizza", this::transformBlockPizza);
        put("com.tiviacz.pizzacraft.blocks.BlockChoppingBoard", this::transformChoppingBoard);
        put("com.tiviacz.pizzacraft.crafting.chopping.ChoppingBoardUtils", this::transformChoppingBoardUtils);
        put("com.tiviacz.pizzacraft.proxy.CommonProxy", this::transformCommonProxy);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        Function<byte[], byte[]> function = functions.get(transformedName);
        if (function != null) basicClass = function.apply(basicClass);
        return basicClass;
    }

    private void put(String name, Function<byte[], byte[]> function) {
        this.functions.put(name, function);
    }

    private byte[] transformCommonProxy(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        for (MethodNode method : cls.methods) {
            if (method.name.equals("preInitRegistries")) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                method.instructions.insertBefore(node, new MethodInsnNode(INVOKESTATIC, "com/tiviacz/pizzacraft/init/OreDictInit", "registerOres", "()V", false));
            }
            else if (method.name.equals("initRegistries")) {
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

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformChoppingBoardRecipes(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        Iterator<MethodNode> methodIterator = cls.methods.iterator();
        while (methodIterator.hasNext()) {
            MethodNode method = methodIterator.next();
            if (method.name.equals("<init>")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKESTATIC) {
                        method.instructions.insertBefore(node, new MethodInsnNode(INVOKESTATIC, HOOKS, "createMap", "()Ljava/util/Map;", false));
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

        ClassWriter writer = new ClassWriter(COMPUTE_ALL);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformChoppingBoardUtils(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

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
            m.visitMethodInsn(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", deobf ? "isEmpty" : "func_190926_b", "()Z", false);
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

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformBlockPizza(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        for (MethodNode method : cls.methods) {
            if (method.name.equals(deobf ? "onBlockActivated" : "func_180639_a")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC) {
                        FieldInsnNode field = (FieldInsnNode) node;
                        if (field.name.equals("PEEL")) {
                            method.instructions.insert(node, new MethodInsnNode(INVOKESTATIC, HOOKS, "isPeel", "(Lnet/minecraft/item/ItemStack;)Z", false));
                            method.instructions.remove(node.getPrevious());
                            iterator.remove();
                            ((JumpInsnNode) iterator.next()).setOpcode(IFEQ);
                        }
                        else if (field.name.equals("KNIFE")) {
                            method.instructions.insert(node, new MethodInsnNode(INVOKESTATIC, HOOKS, "isKnife", "(Lnet/minecraft/item/ItemStack;)Z", false));
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

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformChoppingBoard(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        for (MethodNode method : cls.methods) {
            if (method.name.equals(deobf ? "onBlockActivated" : "func_180639_a")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC && ((FieldInsnNode) node).name.equals("KNIFE")) {
                        method.instructions.insert(node, new MethodInsnNode(INVOKESTATIC, HOOKS, "isKnife", "(Lnet/minecraft/item/ItemStack;)Z", false));
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

        ClassWriter writer = new ClassWriter(COMPUTE_ALL);
        cls.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformContainerBrewingStand(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        for (MethodNode method : cls.methods) {
            if (method.name.equals("<init>")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                String fuelSlot = HOOKS + "$FuelSlot";

                while (iterator.hasNext()) {

                    AbstractInsnNode node = iterator.next();

                    if (node.getOpcode() == NEW && ((TypeInsnNode) node).desc.endsWith("$Fuel")) {
                        method.instructions.insertBefore(node, new TypeInsnNode(NEW, fuelSlot));
                        iterator.remove();
                    }
                    else if (node.getOpcode() == INVOKESPECIAL && ((MethodInsnNode) node).owner.endsWith("$Fuel")) {
                        method.instructions.insertBefore(node, new MethodInsnNode(INVOKESPECIAL, fuelSlot, "<init>", "(Lnet/minecraft/inventory/IInventory;III)V", false));
                        iterator.remove();
                        break;
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(COMPUTE_ALL);
        cls.accept(writer);

        return writer.toByteArray();
    }

    private byte[] transformTEBrewingStand(byte[] basicClass) {

        ClassReader reader = new ClassReader(basicClass);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);

        for (MethodNode method : cls.methods) {

            if (method.name.equals(deobf ? "update" : "func_73660_a")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                boolean remove = false;
                int count = 0;

                while (iterator.hasNext()) {

                    AbstractInsnNode node = iterator.next();

                    if (count == 0 && node.getOpcode() == ALOAD && ((VarInsnNode) node).var == 1) {
                        remove = true;
                        count++;
                        continue;
                    }

                    if (node.getOpcode() == IF_ACMPNE) {
                        ((JumpInsnNode) node).setOpcode(IFNE);
                        method.instructions.insertBefore(node, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/item/ItemStack", deobf ? "isEmpty" : "func_190926_b", "()Z", false));
                        remove = false;
                    }

                    if (count == 1 && node.getOpcode() == ALOAD) {
                        remove = true;
                        count++;
                    }

                    if (remove) {

                        if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).desc.equals("()V")) {
                            remove = false;

                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(ALOAD, 0));
                            list.add(new VarInsnNode(ALOAD, 1));
                            list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "handleFuel", "(Lnet/minecraft/tileentity/TileEntityBrewingStand;Lnet/minecraft/item/ItemStack;)V", false));

                            method.instructions.insert(node, list);
                        }

                        iterator.remove();

                        if (!remove) break;
                    }
                }
            }
            else if (method.name.equals(deobf ? "isItemValidForSlot" : "func_94041_b")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                boolean remove = false;

                while (iterator.hasNext()) {

                    AbstractInsnNode node = iterator.next();

                    if (node.getOpcode() == ALOAD && ((VarInsnNode) node).var == 3) {
                        iterator.remove();
                        node = iterator.next();

                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 2));
                        list.add(new MethodInsnNode(INVOKESTATIC, HOOKS, "isFuel", "(Lnet/minecraft/item/ItemStack;)Z", false));
                        list.add(new InsnNode(IRETURN));

                        ((JumpInsnNode) node.getNext()).setOpcode(IFEQ);

                        method.instructions.insert(node, list);

                        iterator.remove();
                    }

                }

                break;
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cls.accept(writer);

        return writer.toByteArray();
    }
}
