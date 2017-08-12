package com.error22.karonda.converter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.InstructionAdapter;

import com.error22.karonda.ir.KMethod;

public class MethodConverter extends InstructionAdapter {
	private KMethod kMethod;

	public MethodConverter(KMethod kMethod) {
		super(Opcodes.ASM5, null);
		this.kMethod = kMethod;
	}
	
}
