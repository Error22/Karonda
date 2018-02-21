package com.error22.karonda.ir;

public interface IType {

	int[] getDefaultValue();

	boolean isCategoryTwo();

	int[] fieldWrap(int[] value);

	int[] fieldUnwrap(int[] value);

}
