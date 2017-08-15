package com.error22.karonda.ir;

public interface IType {

	IObject getDefaultValue();

	boolean isCategoryTwo();

	IObject fieldWrap(IObject value);

	IObject fieldUnwrap(IObject value);

}
