package com.error22.karonda.converter;

import java.util.Arrays;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import com.error22.karonda.ir.ClassType;
import com.error22.karonda.ir.KClass;

public class ClassConverter extends ClassVisitor {
	private KClass kClass;

	public ClassConverter() {
		super(Opcodes.ASM5);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		System.out.println("Visit version=" + version + " access=" + access + " name=" + name + " signature="
				+ signature + " superName=" + superName + " interfaces=" + Arrays.deepToString(interfaces));

		ClassType type;
		if ((access & Opcodes.ACC_ENUM) == Opcodes.ACC_ENUM)
			type = ClassType.Enum;
		else if ((access & Opcodes.ACC_ANNOTATION) == Opcodes.ACC_ANNOTATION)
			type = ClassType.Annotation;
		else if ((access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE)
			type = ClassType.Interface;
		else if ((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT)
			type = ClassType.Abstract;
		else
			type = ClassType.Class;

		boolean specialResolve = (access & Opcodes.ACC_SUPER) == Opcodes.ACC_SUPER;
		kClass = new KClass(name, type, specialResolve, superName);
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		System.out.println("visitOuterClass owner=" + owner + " name=" + name + " desc=" + desc);
		super.visitOuterClass(owner, name, desc);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		System.out.println("visitAnnotation desc=" + desc + " visible=" + visible);
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		System.out.println("visitTypeAnnotation typeRef=" + typeRef + " typePath=" + typePath + " desc=" + desc
				+ " visible=" + visible);
		return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		System.out.println("visitInnerClass name=" + name + " outerName=" + outerName + " innerName=" + innerName
				+ " access=" + access);
		super.visitInnerClass(name, outerName, innerName, access);
	}

	@Override
	public void visitAttribute(Attribute attr) {
		System.out.println("visitAttribute attr=" + attr);
		super.visitAttribute(attr);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		System.out.println("visitField access=" + access + " name=" + name + " desc=" + desc + " signature=" + signature
				+ " value=" + value);
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		System.out.println("visitMethod access=" + access + " name=" + name + " desc=" + desc + " signature="
				+ signature + " exceptions=" + Arrays.deepToString(exceptions));
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

	@Override
	public void visitEnd() {
		System.out.println("visitEnd");
		super.visitEnd();
	}

	@Override
	public void visitSource(String source, String debug) {
		System.out.println("visitSource source=" + source + " debug=" + debug);
		super.visitSource(source, debug);
	}

}
