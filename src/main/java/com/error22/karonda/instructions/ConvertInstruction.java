package com.error22.karonda.instructions;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.PrimitiveObject;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class ConvertInstruction implements IInstruction {
	private PrimitiveType from, to;

	public ConvertInstruction(PrimitiveType from, PrimitiveType to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void execute(StackFrame stackFrame) {
		Object pvalue = ((PrimitiveObject) stackFrame.pop()).getValue();
		switch (to) {
		case Byte:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, (int) ((Number) pvalue).byteValue()));
			break;
		case Boolean:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, (int) ((Number) pvalue).intValue() != 0));
			break;
		case Char:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, (int) (char) ((Number) pvalue).intValue()));
			break;
		case Short:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, (int) ((Number) pvalue).shortValue()));
			break;
		case Int:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Int, (int) ((Number) pvalue).byteValue()));
			break;
		case Long:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Long, ((Number) pvalue).longValue()));
			break;
		case Float:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Float, ((Number) pvalue).floatValue()));
			break;
		case Double:
			stackFrame.push(new PrimitiveObject(PrimitiveType.Double, ((Number) pvalue).doubleValue()));
			break;
		default:
			throw new NotImplementedException();
		}
	}

}
