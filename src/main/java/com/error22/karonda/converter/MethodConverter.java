package com.error22.karonda.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.LoadLocalInstruction;
import com.error22.karonda.ir.KMethod;
import com.error22.karonda.ir.ObjectType;
import com.error22.karonda.ir.PrimitiveType;

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
		switch (opcode) {
		case Opcodes.ILOAD:
			addInstruction(new LoadLocalInstruction(PrimitiveType.Int, var));
			break;
		case Opcodes.LLOAD:
			addInstruction(new LoadLocalInstruction(PrimitiveType.Long, var));
			break;
		case Opcodes.FLOAD:
			addInstruction(new LoadLocalInstruction(PrimitiveType.Float, var));
			break;
		case Opcodes.DLOAD:
			addInstruction(new LoadLocalInstruction(PrimitiveType.Double, var));
			break;
		case Opcodes.ALOAD:
			addInstruction(new LoadLocalInstruction(OBJECT_TYPE, var));
			break;
		case Opcodes.ISTORE:
		case Opcodes.LSTORE:
		case Opcodes.FSTORE:
		case Opcodes.DSTORE:
		case Opcodes.ASTORE:
		case Opcodes.RET:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
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

	private final static ObjectType OBJECT_TYPE = new ObjectType("java/lang/Object");
}
