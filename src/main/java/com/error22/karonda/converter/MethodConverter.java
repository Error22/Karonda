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
import com.error22.karonda.instructions.CompareInstruction;
import com.error22.karonda.instructions.CompareInstruction.CompareOp;
import com.error22.karonda.instructions.ConvertInstruction;
import com.error22.karonda.instructions.DuplicateInstruction;
import com.error22.karonda.instructions.DuplicateInstruction.DuplicateMode;
import com.error22.karonda.instructions.FieldInstruction;
import com.error22.karonda.instructions.FieldInstruction.FieldOperation;
import com.error22.karonda.instructions.IInstruction;
import com.error22.karonda.instructions.InvokeInstruction;
import com.error22.karonda.instructions.InvokeInstruction.InvokeType;
import com.error22.karonda.instructions.JumpInstruction;
import com.error22.karonda.instructions.JumpInstruction.JumpType;
import com.error22.karonda.instructions.LoadConstantInstruction;
import com.error22.karonda.instructions.LoadStringInstruction;
import com.error22.karonda.instructions.LocalInstruction;
import com.error22.karonda.instructions.LocalInstruction.LocalOperation;
import com.error22.karonda.instructions.MathInstruction;
import com.error22.karonda.instructions.MathInstruction.MathOp;
import com.error22.karonda.instructions.NewArrayInstruction;
import com.error22.karonda.instructions.NewInstruction;
import com.error22.karonda.instructions.ReturnInstruction;
import com.error22.karonda.instructions.ThrowInstruction;
import com.error22.karonda.ir.ArrayType;
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
			addInstruction(new LoadConstantInstruction(PrimitiveType.Void, null));
			break;
		case Opcodes.ICONST_M1:
		case Opcodes.ICONST_0:
		case Opcodes.ICONST_1:
		case Opcodes.ICONST_2:
		case Opcodes.ICONST_3:
		case Opcodes.ICONST_4:
		case Opcodes.ICONST_5:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Int, opcode - Opcodes.ICONST_0));
			break;
		case Opcodes.LCONST_0:
		case Opcodes.LCONST_1:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Long, opcode - Opcodes.LCONST_0));
			break;
		case Opcodes.FCONST_0:
		case Opcodes.FCONST_1:
		case Opcodes.FCONST_2:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Float, opcode - Opcodes.FCONST_0));
			break;
		case Opcodes.DCONST_0:
		case Opcodes.DCONST_1:
			addInstruction(new LoadConstantInstruction(PrimitiveType.Double, opcode - Opcodes.DCONST_0));
			break;
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
			addInstruction(new MathInstruction(MathOp.SubtractInts));
			break;
		case Opcodes.LSUB:
			addInstruction(new MathInstruction(MathOp.SubtractLongs));
			break;
		case Opcodes.FSUB:
			addInstruction(new MathInstruction(MathOp.SubtractFloats));
			break;
		case Opcodes.DSUB:
			addInstruction(new MathInstruction(MathOp.SubtractDoubles));
			break;
		case Opcodes.IMUL:
			addInstruction(new MathInstruction(MathOp.MultiplyInts));
			break;
		case Opcodes.LMUL:
			addInstruction(new MathInstruction(MathOp.MultiplyLongs));
			break;
		case Opcodes.FMUL:
			addInstruction(new MathInstruction(MathOp.MultiplyFloats));
			break;
		case Opcodes.DMUL:
			addInstruction(new MathInstruction(MathOp.MultiplyDoubles));
			break;
		case Opcodes.IDIV:
			addInstruction(new MathInstruction(MathOp.DivideInts));
			break;
		case Opcodes.LDIV:
			addInstruction(new MathInstruction(MathOp.DivideLongs));
			break;
		case Opcodes.FDIV:
			addInstruction(new MathInstruction(MathOp.DivideFloats));
			break;
		case Opcodes.DDIV:
			addInstruction(new MathInstruction(MathOp.DivideDoubles));
			break;
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
			throw new NotImplementedException("OP: " + opcode);
		case Opcodes.I2L:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Long));
			break;
		case Opcodes.I2F:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Float));
			break;
		case Opcodes.I2D:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Double));
			break;
		case Opcodes.L2I:
			addInstruction(new ConvertInstruction(PrimitiveType.Long, PrimitiveType.Int));
			break;
		case Opcodes.L2F:
			addInstruction(new ConvertInstruction(PrimitiveType.Long, PrimitiveType.Float));
			break;
		case Opcodes.L2D:
			addInstruction(new ConvertInstruction(PrimitiveType.Long, PrimitiveType.Double));
			break;
		case Opcodes.F2I:
			addInstruction(new ConvertInstruction(PrimitiveType.Float, PrimitiveType.Int));
			break;
		case Opcodes.F2L:
			addInstruction(new ConvertInstruction(PrimitiveType.Float, PrimitiveType.Long));
			break;
		case Opcodes.F2D:
			addInstruction(new ConvertInstruction(PrimitiveType.Float, PrimitiveType.Double));
			break;
		case Opcodes.D2I:
			addInstruction(new ConvertInstruction(PrimitiveType.Double, PrimitiveType.Int));
			break;
		case Opcodes.D2L:
			addInstruction(new ConvertInstruction(PrimitiveType.Double, PrimitiveType.Long));
			break;
		case Opcodes.D2F:
			addInstruction(new ConvertInstruction(PrimitiveType.Double, PrimitiveType.Float));
			break;
		case Opcodes.I2B:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Byte));
			break;
		case Opcodes.I2C:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Char));
			break;
		case Opcodes.I2S:
			addInstruction(new ConvertInstruction(PrimitiveType.Int, PrimitiveType.Short));
			break;
		case Opcodes.LCMP:
			addInstruction(new CompareInstruction(CompareOp.Longs));
			break;
		case Opcodes.FCMPL:
			addInstruction(new CompareInstruction(CompareOp.FloatsNegNaN));
			break;
		case Opcodes.FCMPG:
			addInstruction(new CompareInstruction(CompareOp.FloatsPosNaN));
			break;
		case Opcodes.DCMPL:
			addInstruction(new CompareInstruction(CompareOp.DoublesNegNaN));
			break;
		case Opcodes.DCMPG:
			addInstruction(new CompareInstruction(CompareOp.DoublesPosNaN));
			break;
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
			throw new NotImplementedException("OP: " + opcode);
		case Opcodes.ATHROW:
			addInstruction(new ThrowInstruction());
			break;
		case Opcodes.MONITORENTER:
		case Opcodes.MONITOREXIT:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void visitLdcInsn(Object cst) {
		if (cst instanceof Integer) {
			int val = ((Integer) cst).intValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Int, val));
		} else if (cst instanceof Byte) {
			int val = ((Byte) cst).intValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Byte, val));
		} else if (cst instanceof Character) {
			int val = ((Character) cst).charValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Char, val));
		} else if (cst instanceof Short) {
			int val = ((Short) cst).intValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Short, val));
		} else if (cst instanceof Boolean) {
			int val = ((Boolean) cst).booleanValue() ? 1 : 0;
			addInstruction(new LoadConstantInstruction(PrimitiveType.Boolean, val));
		} else if (cst instanceof Float) {
			float val = ((Float) cst).floatValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Float, val));
		} else if (cst instanceof Long) {
			long val = ((Long) cst).longValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Long, val));
		} else if (cst instanceof Double) {
			double val = ((Double) cst).doubleValue();
			addInstruction(new LoadConstantInstruction(PrimitiveType.Double, val));
		} else if (cst instanceof String) {
			addInstruction(new LoadStringInstruction((String) cst));
		} else if (cst instanceof Type) {
			throw new NotImplementedException("OP: ldc " + cst);
		} else if (cst instanceof Handle) {
			throw new NotImplementedException("OP: ldc " + cst);
		} else {
			throw new IllegalArgumentException();
		}
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
			addInstruction(new NewInstruction((ObjectType) t));
			break;
		case Opcodes.ANEWARRAY:
			addInstruction(new NewArrayInstruction(new ArrayType(t, 1), 1));
			break;
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
			addInstruction(new LocalInstruction(LocalOperation.Load, PrimitiveType.Int, var));
			break;
		case Opcodes.LLOAD:
			addInstruction(new LocalInstruction(LocalOperation.Load, PrimitiveType.Long, var));
			break;
		case Opcodes.FLOAD:
			addInstruction(new LocalInstruction(LocalOperation.Load, PrimitiveType.Float, var));
			break;
		case Opcodes.DLOAD:
			addInstruction(new LocalInstruction(LocalOperation.Load, PrimitiveType.Double, var));
			break;
		case Opcodes.ALOAD:
			addInstruction(new LocalInstruction(LocalOperation.Load, OBJECT_TYPE, var));
			break;
		case Opcodes.ISTORE:
			addInstruction(new LocalInstruction(LocalOperation.Store, PrimitiveType.Int, var));
			break;
		case Opcodes.LSTORE:
			addInstruction(new LocalInstruction(LocalOperation.Store, PrimitiveType.Long, var));
			break;
		case Opcodes.FSTORE:
			addInstruction(new LocalInstruction(LocalOperation.Store, PrimitiveType.Float, var));
			break;
		case Opcodes.DSTORE:
			addInstruction(new LocalInstruction(LocalOperation.Store, PrimitiveType.Double, var));
			break;
		case Opcodes.ASTORE:
			addInstruction(new LocalInstruction(LocalOperation.Store, OBJECT_TYPE, var));
			break;
		case Opcodes.RET:
			throw new NotImplementedException("OP: " + opcode);
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		switch (opcode) {
		case Opcodes.GETSTATIC:
			addInstruction(new FieldInstruction(FieldOperation.LoadStatic,
					ConversionUtils.parseFieldSignature(owner, name, desc)));
			break;
		case Opcodes.PUTSTATIC:
			addInstruction(new FieldInstruction(FieldOperation.StoreStatic,
					ConversionUtils.parseFieldSignature(owner, name, desc)));
			break;
		case Opcodes.GETFIELD:
			addInstruction(new FieldInstruction(FieldOperation.LoadLocal,
					ConversionUtils.parseFieldSignature(owner, name, desc)));
			break;
		case Opcodes.PUTFIELD:
			addInstruction(new FieldInstruction(FieldOperation.StoreLocal,
					ConversionUtils.parseFieldSignature(owner, name, desc)));
			break;
		default:
			throw new IllegalArgumentException();
		}
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
			switch (operand) {
			case Opcodes.T_BOOLEAN:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Boolean, 1), 1));
				break;
			case Opcodes.T_CHAR:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Char, 1), 1));
				break;
			case Opcodes.T_BYTE:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Byte, 1), 1));
				break;
			case Opcodes.T_SHORT:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Short, 1), 1));
				break;
			case Opcodes.T_INT:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Int, 1), 1));
				break;
			case Opcodes.T_FLOAT:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Float, 1), 1));
				break;
			case Opcodes.T_LONG:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Long, 1), 1));
				break;
			case Opcodes.T_DOUBLE:
				addInstruction(new NewArrayInstruction(new ArrayType(PrimitiveType.Double, 1), 1));
				break;
			default:
				throw new IllegalArgumentException();
			}
			break;
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
		addInstruction(new NewArrayInstruction((ArrayType) ConversionUtils.convertType(Type.getType(desc)), dims));
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		throw new NotImplementedException("OP: tableswitch");
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		switch (opcode) {
		case Opcodes.IFEQ:
			addInstruction(new JumpInstruction(JumpType.EqualZero, label));
			break;
		case Opcodes.IFNE:
			addInstruction(new JumpInstruction(JumpType.NotEqualZero, label));
			break;
		case Opcodes.IFLT:
			addInstruction(new JumpInstruction(JumpType.LessThanZero, label));
			break;
		case Opcodes.IFGE:
			addInstruction(new JumpInstruction(JumpType.GreaterThanEqualZero, label));
			break;
		case Opcodes.IFGT:
			addInstruction(new JumpInstruction(JumpType.GreaterThanZero, label));
			break;
		case Opcodes.IFLE:
			addInstruction(new JumpInstruction(JumpType.LessThanEqualZero, label));
			break;
		case Opcodes.IF_ICMPEQ:
			addInstruction(new JumpInstruction(JumpType.IntsEqual, label));
			break;
		case Opcodes.IF_ICMPNE:
			addInstruction(new JumpInstruction(JumpType.IntsNotEqual, label));
			break;
		case Opcodes.IF_ICMPLT:
			addInstruction(new JumpInstruction(JumpType.IntsLessThan, label));
			break;
		case Opcodes.IF_ICMPGE:
			addInstruction(new JumpInstruction(JumpType.IntsGreaterThanEqual, label));
			break;
		case Opcodes.IF_ICMPGT:
			addInstruction(new JumpInstruction(JumpType.IntsGreaterThan, label));
			break;
		case Opcodes.IF_ICMPLE:
			addInstruction(new JumpInstruction(JumpType.IntsLessThanEqual, label));
			break;
		case Opcodes.IF_ACMPEQ:
			addInstruction(new JumpInstruction(JumpType.ReferenceEqual, label));
			break;
		case Opcodes.IF_ACMPNE:
			addInstruction(new JumpInstruction(JumpType.ReferenceNotEqual, label));
			break;
		case Opcodes.GOTO:
			addInstruction(new JumpInstruction(JumpType.Goto, label));
			break;
		case Opcodes.JSR:
			addInstruction(new JumpInstruction(JumpType.Subroutine, label));
			break;
		case Opcodes.IFNULL:
			addInstruction(new JumpInstruction(JumpType.Null, label));
			break;
		case Opcodes.IFNONNULL:
			addInstruction(new JumpInstruction(JumpType.NotNull, label));
			break;
		default:
			throw new IllegalArgumentException();
		}
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
