package com.error22.karonda.converter;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.InstructionAdapter;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.KMethod;

public class MethodConverter extends InstructionAdapter {
	private KMethod kMethod;

	public MethodConverter(KMethod kMethod) {
		super(Opcodes.ASM5, null);
		this.kMethod = kMethod;
	}

	@Override
	public void visitInsn(int opcode) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitInsn(opcode);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitJumpInsn(opcode, label);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		if (!SUPPORTED_OPS.contains(opcode))
			throw new NotImplementedException("OP: " + opcode);
		super.visitVarInsn(opcode, var);
	}

	@Override
	public void goTo(Label label) {
		throw new NotImplementedException("goto " + label);
	}

	@Override
	public void visitLabel(Label label) {
		throw new NotImplementedException("label " + label);
	}

	private static List<Integer> SUPPORTED_OPS;
	static {
		SUPPORTED_OPS = new ArrayList<Integer>();
	}
}
