package surreal.ttweaker.core;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Dumbformer implements Opcodes {

    protected static String getName(String mcp, String srg) {
        return FMLLaunchHandler.isDeobfuscatedEnvironment() ? mcp : srg;
    }

    protected static <T extends ClassVisitor> T read(byte[] basicClass, T visitor) {
        ClassReader reader = new ClassReader(basicClass);
        reader.accept(visitor, 0);
        return visitor;
    }

    protected static ClassNode read(byte[] basicClass) {
        return read(basicClass, new ClassNode());
    }

    protected static byte[] write(ClassNode cls, int options) {
        ClassWriter writer = new ClassWriter(options);
        cls.accept(writer);
        return writer.toByteArray();
    }

    protected static byte[] write(ClassNode cls) {
        return write(cls, ClassWriter.COMPUTE_MAXS);
    }

    protected static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/ttweaker/core/TTHooks", name, desc, false);
    }

    protected static void writeClass(ClassNode cls) {
        if (!FMLLaunchHandler.isDeobfuscatedEnvironment()) return;
        FileOutputStream stream;
        File file = new File(Minecraft.getMinecraft().gameDir, "classOutputs/" + cls.name + ".class");
        file.getParentFile().mkdirs();

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cls.accept(writer);

        try {
            stream = new FileOutputStream(file);
            stream.write(writer.toByteArray());
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }}
