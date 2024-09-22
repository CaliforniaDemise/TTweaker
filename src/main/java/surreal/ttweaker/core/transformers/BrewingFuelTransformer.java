package surreal.ttweaker.core.transformers;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.*;
import surreal.ttweaker.core.Dumbformer;

import java.util.Iterator;

/**
 * Allow adding custom fuels for Brewing Stand
 **/
public class BrewingFuelTransformer extends Dumbformer {

    public static byte[] transformTileEntityBrewingStand(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("update", "func_73660_a"))) {
                AbstractInsnNode node = method.instructions.getFirst();
                while (node.getOpcode() != GETSTATIC) node = node.getNext();
                method.instructions.remove(node.getPrevious());
                method.instructions.insertBefore(node, hook("BrewingFuel$isFuel", "(Lnet/minecraft/item/ItemStack;)Z"));
                ((JumpInsnNode) node.getNext()).setOpcode(IFEQ);
                method.instructions.remove(node);
            }
            else if (method.name.equals(getName("isItemValidForSlot", "func_94041_b"))) {
               AbstractInsnNode node = method.instructions.getFirst();
               while (node.getOpcode() != GETSTATIC) node = node.getNext();
               method.instructions.remove(node.getPrevious());
               InsnList list = new InsnList();
               list.add(new VarInsnNode(ALOAD, 2));
               list.add(hook("BrewingFuel$isFuel", "(Lnet/minecraft/item/ItemStack;)Z"));
               method.instructions.insertBefore(node, list);
               ((JumpInsnNode) node.getNext()).setOpcode(IFEQ);
               method.instructions.remove(node);
               break;
            }
        }
        return write(cls);
    }

    public static byte[] transformContainerBrewingStand$Fuel(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        String methodName = getName("isValidBrewingFuel", "func_185004_b_");

        Iterator<MethodNode> iterator = cls.methods.iterator();
        while (iterator.hasNext()) {
            MethodNode method = iterator.next();
            if (method.name.equals(methodName)) {
                iterator.remove();
                break;
            }
        }
        { // isValidBrewingFuel
            String desc = "(Lnet/minecraft/item/ItemStack;)Z";
            MethodVisitor m = cls.visitMethod(ACC_PUBLIC | ACC_STATIC, methodName, desc, null, null);
            m.visitVarInsn(ALOAD, 0);
            m.visitMethodInsn(INVOKESTATIC, "surreal/ttweaker/core/TTHooks", "BrewingFuel$isFuel", desc, false);
            m.visitInsn(IRETURN);
        }
        return write(cls);
    }
}
