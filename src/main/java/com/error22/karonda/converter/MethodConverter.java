package com.error22.karonda.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.ir.KMethod;

public class MethodConverter extends MethodVisitor {
	private KMethod kMethod;
	private ArrayList<IInstruction> instructions;
	private Map<Label, Integer> labelMap;

	public MethodConverter(KMethod kMethod) {
		super(Opcodes.ASM5, null);
		this.kMethod = kMethod;
		instructions = new ArrayList<IInstruction>();
		labelMap = new HashMap<Label, Integer>();
	}

	private void addInstruction(IInstruction instruction) {
		instructions.add(instruction);
	}

	@Override
	public void visitInsn(int opcode) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		throw new UnsupportedOperationException("Use new ASM5 visitMethodInsn method");
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitLabel(Label label) {
		labelMap.put(label, instructions.size());
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		System.out.println("max: " + maxStack + " " + maxLocals);
		kMethod.setMaxStack(maxStack);
		kMethod.setMaxLocals(maxLocals);
	}

	@Override
	public void visitEnd() {
		System.out.println("instructions: " + instructions.size());
		kMethod.setInstructions(instructions.toArray(new IInstruction[instructions.size()]));
		kMethod.setLabelMap(labelMap);
	}

}
