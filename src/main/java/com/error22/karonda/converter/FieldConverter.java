package com.error22.karonda.converter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.KField;

public class FieldConverter extends FieldVisitor {
	private KField field;

	public FieldConverter(KField field) {
		super(Opcodes.ASM5);
		this.field = field;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		throw new NotImplementedException();
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		throw new NotImplementedException();
	}

	@Override
	public void visitAttribute(Attribute attr) {
		throw new NotImplementedException();
	}

}
