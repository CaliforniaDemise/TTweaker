package surreal.ttweaker.core;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

// 3 T's
public class TTTransformer implements IClassTransformer, Opcodes {

    private final Map<String, Function<byte[], byte[]>> functions;

    private final int COMPUTE_ALL = ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
    private final boolean deobf = FMLLaunchHandler.isDeobfuscatedEnvironment();
    private final Logger logger = LogManager.getLogger("TTweaker");

    private final String HOOKS = "surreal/ttweaker/core/TTHooks";

    public TTTransformer() {
        this.functions = new Object2ObjectOpenHashMap<>();
        put("net.minecraft.tileentity.TileEntityBrewingStand", this::transformTEBrewingStand);
        put("net.minecraft.inventory.ContainerBrewingStand", this::transformContainerBrewingStand);
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        var function = functions.get(transformedName);
        if (function != null) basicClass = function.apply(basicClass);

        return basicClass;
    }

    private void put(String name, Function<byte[], byte[]> function) {
        this.functions.put(name, function);
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

        ClassWriter writer = new ClassWriter(COMPUTE_ALL);
        cls.accept(writer);

        return writer.toByteArray();
    }
}
