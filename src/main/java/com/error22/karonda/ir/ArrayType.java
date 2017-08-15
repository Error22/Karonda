package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public class ArrayType implements IType {
	private IType type;

	public ArrayType(IType type) {
		this.type = type;
	}

	@Override
	public boolean isCategoryTwo() {
		return false;
	}

	@Override
	public IObject getDefaultValue() {
		throw new NotImplementedException();
	}

	@Override
	public IObject fieldWrap(IObject value) {
		throw new NotImplementedException();
	}

	@Override
	public IObject fieldUnwrap(IObject value) {
		throw new NotImplementedException();
	}

}
