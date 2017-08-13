package com.error22.karonda.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.instructions.DuplicateInstruction;
import com.error22.karonda.instructions.DuplicateInstruction.DuplicateMode;
import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.InvokeInstruction;
import com.error22.karonda.instructions.InvokeInstruction.InvokeType;
import com.error22.karonda.instructions.MathInstruction.MathOp;
import com.error22.karonda.instructions.LoadConstantInstruction;
import com.error22.karonda.instructions.LoadLocalInstruction;
import com.error22.karonda.instructions.MathInstruction;
import com.error22.karonda.instructions.NewInstruction;
import com.error22.karonda.instructions.ReturnInstruction;
import com.error22.karonda.instructions.StoreLocalInstruction;
import com.error22.karonda.ir.IType;
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
		switch (opcode) {
		case Opcodes.NOP:
			break;
		case Opcodes.ACONST_NULL:
		case Opcodes.ICONST_M1:
		case Opcodes.ICONST_0:
		case Opcodes.ICONST_1:
		case Opcodes.ICONST_2:
		case Opcodes.ICONST_3:
		case Opcodes.ICONST_4:
		case Opcodes.ICONST_5:
		case Opcodes.LCONST_0:
		case Opcodes.LCONST_1:
		case Opcodes.FCONST_0:
		case Opcodes.FCONST_1:
		case Opcodes.FCONST_2:
		case Opcodes.DCONST_0:
		case Opcodes.DCONST_1:
		case Opcodes.IALOAD:
		case Opcodes.LALOAD:
		case Opcodes.FALOAD:
		case Opcodes.DALOAD:
		case Opcodes.AALOAD:
		case Opcodes.BALOAD:
		case Opcodes.CALOAD:
		case Opcodes.SALOAD:
		case Opcodes.IASTORE:
		case Opcodes.LASTORE:
		case Opcodes.FASTORE:
		case Opcodes.DASTORE:
		case Opcodes.AASTORE:
		case Opcodes.BASTORE:
		case Opcodes.CASTORE:
		case Opcodes.SASTORE:
		case Opcodes.POP:
		case Opcodes.POP2:
			throw new NotImplementedException("OP: " + opcode);
		case Opcodes.DUP:
			addInstruction(new DuplicateInstruction(DuplicateMode.SingleCat1));
			break;
		case Opcodes.DUP_X1:
			addInstruction(new DuplicateInstruction(DuplicateMode.SingleCat1TwoDown));
			break;
		case Opcodes.DUP_X2:
			addInstruction(new DuplicateInstruction(DuplicateMode.SingleSpecialDown));
			break;
		case Opcodes.DUP2:
			addInstruction(new DuplicateInstruction(DuplicateMode.TwoSpecial));
			break;
		case Opcodes.DUP2_X1:
			addInstruction(new DuplicateInstruction(DuplicateMode.TwoSpecialDown));
			break;
		case Opcodes.DUP2_X2:
			addInstruction(new DuplicateInstruction(DuplicateMode.TwoSpecialFurtherDown));
			break;
		case Opcodes.SWAP:
			throw new NotImplementedException("OP: " + opcode);
		case Opcodes.IADD:
			addInstruction(new MathInstruction(MathOp.AddInts));
			break;
		case Opcodes.LADD:
			addInstruction(new MathInstruction(MathOp.AddLongs));
			break;
		case Opcodes.FADD:
			addInstruction(new MathInstruction(MathOp.AddFloats));
			break;
		case Opcodes.DADD:
			addInstruction(new MathInstruction(MathOp.AddDoubles));
			break;
		case Opcodes.ISUB:
		case Opcodes.LSUB:
		case Opcodes.FSUB:
		case Opcodes.DSUB:
		case Opcodes.IMUL:
		case Opcodes.LMUL:
		case Opcodes.FMUL:
		case Opcodes.DMUL:
		case Opcodes.IDIV:
		case Opcodes.LDIV:
		case Opcodes.FDIV:
		case Opcodes.DDIV:
		case Opcodes.IREM:
		case Opcodes.LREM:
		case Opcodes.FREM:
		case Opcodes.DREM:
		case Opcodes.INEG:
		case Opcodes.LNEG:
		case Opcodes.FNEG:
		case Opcodes.DNEG:
		case Opcodes.ISHL:
		case Opcodes.LSHL:
		case Opcodes.ISHR:
		case Opcodes.LSHR:
		case Opcodes.IUSHR:
		case Opcodes.LUSHR:
		case Opcodes.IAND:
		case Opcodes.LAND:
		case Opcodes.IOR:
		case Opcodes.LOR:
		case Opcodes.IXOR:
		case Opcodes.LXOR:
		case Opcodes.I2L:
		case Opcodes.I2F:
		case Opcodes.I2D:
		case Opcodes.L2I:
		case Opcodes.L2F:
		case Opcodes.L2D:
		case Opcodes.F2I:
		case Opcodes.F2L:
		case Opcodes.F2D:
		case Opcodes.D2I:
		case Opcodes.D2L:
		case Opcodes.D2F:
		case Opcodes.I2B:
		case Opcodes.I2C:
		case Opcodes.I2S:
		case Opcodes.LCMP:
		case Opcodes.FCMPL:
		case Opcodes.FCMPG:
		case Opcodes.DCMPL:
		case Opcodes.DCMPG:
			throw new NotImplementedException("OP: " + opcode);
		case Opcodes.IRETURN:
			addInstruction(new ReturnInstruction(PrimitiveType.Int));
			break;
		case Opcodes.LRETURN:
			addInstruction(new ReturnInstruction(PrimitiveType.Long));
			break;
		case Opcodes.FRETURN:
			addInstruction(new ReturnInstruction(PrimitiveType.Float));
			break;
		case Opcodes.DRETURN:
			addInstruction(new ReturnInstruction(PrimitiveType.Double));
			break;
		case Opcodes.ARETURN:
			addInstruction(new ReturnInstruction(OBJECT_TYPE));
			break;
		case Opcodes.RETURN:
			addInstruction(new ReturnInstruction(PrimitiveType.Void));
			break;
		case Opcodes.ARRAYLENGTH:
		case Opcodes.ATHROW:
		case Opcodes.MONITORENTER:
		case Opcodes.MONITOREXIT:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}

	}

	@Override
	public void visitLdcInsn(Object cst) {
		throw new NotImplementedException("OP: ldc");
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		throw new NotImplementedException("OP: inc");
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		IType t = ConversionUtils.convertType(Type.getObjectType(type));
		switch (opcode) {
		case Opcodes.NEW:
			addInstruction(new NewInstruction(t));
			break;
		case Opcodes.ANEWARRAY:
		case Opcodes.CHECKCAST:
		case Opcodes.INSTANCEOF:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
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
			addInstruction(new StoreLocalInstruction(PrimitiveType.Int, var));
			break;
		case Opcodes.LSTORE:
			addInstruction(new StoreLocalInstruction(PrimitiveType.Long, var));
			break;
		case Opcodes.FSTORE:
			addInstruction(new StoreLocalInstruction(PrimitiveType.Float, var));
			break;
		case Opcodes.DSTORE:
			addInstruction(new StoreLocalInstruction(PrimitiveType.Double, var));
			break;
		case Opcodes.ASTORE:
			addInstruction(new StoreLocalInstruction(OBJECT_TYPE, var));
			break;
		case Opcodes.RET:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		throw new NotImplementedException("OP: " + opcode);
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		switch (opcode) {
		case Opcodes.BIPUSH:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Int, operand));
			break;
		case Opcodes.SIPUSH:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Int, operand));
			break;
		case Opcodes.NEWARRAY:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		throw new NotImplementedException("OP: lookupswitch");
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		throw new NotImplementedException("OP: multianewarray");
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		throw new NotImplementedException("OP: tableswitch");
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
		switch (opcode) {
		case Opcodes.INVOKESPECIAL:
			addInstruction(new InvokeInstruction(InvokeType.Special,
					ConversionUtils.parseMethodSignature(owner, name, desc), itf));
			break;
		case Opcodes.INVOKEVIRTUAL:
			addInstruction(new InvokeInstruction(InvokeType.Virtual,
					ConversionUtils.parseMethodSignature(owner, name, desc), itf));
			break;
		case Opcodes.INVOKESTATIC:
			addInstruction(new InvokeInstruction(InvokeType.Static,
					ConversionUtils.parseMethodSignature(owner, name, desc), itf));
			break;
		case Opcodes.INVOKEINTERFACE:
			addInstruction(new InvokeInstruction(InvokeType.Interface,
					ConversionUtils.parseMethodSignature(owner, name, desc), itf));
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		throw new NotImplementedException("OP: invokedynamic");
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
