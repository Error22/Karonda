package com.error22.karonda.instructions;

import java.util.Arrays;

import com.error22.karonda.NotImplementedException;
import com.error22.karonda.ir.PrimitiveType;
import com.error22.karonda.vm.StackFrame;

public class LoadConstantInstruction implements IInstruction {
	private int[] data;
	private boolean isObject;

	public LoadConstantInstruction(PrimitiveType type, Object value, boolean isObject) {
		this.isObject = isObject;
		switch (type) {
		case Void:
			data = new int[] { 0 };
			break;
		case Byte:
		case Boolean:
		case Char:
		case Short:
		case Int:
			data = new int[] { ((Number) value).intValue() };
			break;
		case Long: {
			long lval = ((Number) value).longValue();
			data = new int[] { (int) (lval >> 32), (int) lval };
			break;
		}
		case Float: {
			float fval = ((Number) value).floatValue();
			int ival = Float.floatToRawIntBits(fval);
			data = new int[] { ival };
			break;
		}
		case Double: {
			double dval = ((Number) value).doubleValue();
			long lval = Double.doubleToRawLongBits(dval);
			data = new int[] { (int) (lval >> 32), (int) lval };
			break;
		}
		default:
			throw new NotImplementedException();
		}
	}

	@Override
	public void execute(StackFrame stackFrame) {
		stackFrame.push(data, isObject);
	}

	@Override
	public String toString() {
		return "LoadConstantInstruction [data=" + Arrays.toString(data) + "]";
	}

}
