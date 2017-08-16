package com.error22.karonda.converter;

import java.util.Arrays;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.ClassType;
import com.error22.karonda.ir.FieldSignature;
import com.error22.karonda.ir.KClass;
import com.error22.karonda.ir.KField;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.MethodSignature;

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
		kClass = new KClass(name, type, specialResolve, superName, interfaces);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		throw new NotImplementedException("visitAnnotation desc=" + desc + " visible=" + visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		throw new NotImplementedException("visitTypeAnnotation typeRef=" + typeRef + " typePath=" + typePath + " desc="
				+ desc + " visible=" + visible);
	}

	@Override
	public void visitAttribute(Attribute attr) {
		throw new NotImplementedException("visitAttribute attr=" + attr);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String rawSignature, Object value) {
		System.out.println("visitField access=" + access + " name=" + name + " desc=" + desc + " signature="
				+ rawSignature + " value=" + value);

		FieldSignature signature = ConversionUtils.parseFieldSignature(kClass.getName(), name, desc);
		boolean isStatic = (access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC;
		KField field = new KField(signature, isStatic);
		kClass.addField(field);
		return new FieldConverter(field);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String rawSignature, String[] exceptions) {
		System.out.println("visitMethod access=" + access + " name=" + name + " desc=" + desc + " signature="
				+ rawSignature + " exceptions=" + Arrays.deepToString(exceptions));

		MethodSignature signature = ConversionUtils.parseMethodSignature(kClass.getName(), name, desc);
		boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
		boolean isSynchronized = (access & Opcodes.ACC_SYNCHRONIZED) == Opcodes.ACC_SYNCHRONIZED;
		boolean isNative = (access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE;
		KMethod method = new KMethod(kClass, signature, isAbstract, isSynchronized, isNative);
		kClass.addMethod(method);
		return new MethodConverter(method);
	}

	@Override
	public void visitEnd() {
		System.out.println("visitEnd");
	}

	public KClass getkClass() {
		return kClass;
	}

}
