package com.error22.karonda.ir;

import com.error22.karonda.NotImplementedException;

public class ObjectType implements IType {
	private String name;

	public ObjectType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectType other = (ObjectType) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
