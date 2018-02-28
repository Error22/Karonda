package com.error22.karonda.ir;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.error22.karonda.vm.ClassPool;

public class KClass {
	private String name, superName;
	private String[] interfaceNames;
	private ClassType type;
	private boolean specialMethodResolve, resolved;
	private Map<MethodSignature, KMethod> methods;
	private Map<FieldSignature, KField> fieldMap;
	private List<KField> fields;
	private KClass superClass;
	private KClass[] interfaces;
	private int firstFieldId, lastFieldId;

	public KClass(String name, ClassType type, boolean specialMethodResolve, String superName,
			String[] interfaceNames) {
		this.name = name;
		this.type = type;
		this.specialMethodResolve = specialMethodResolve;
		this.superName = superName;
		this.interfaceNames = interfaceNames;
		methods = new ConcurrentHashMap<MethodSignature, KMethod>();
		fieldMap = new ConcurrentHashMap<FieldSignature, KField>();
		fields = new ArrayList<KField>();
	}

	public void bootstrapResolve(ClassPool pool) {
		if (resolved)
			return;
		resolved = true;

		superClass = superName != null ? pool.bootstrapResolve(superName) : null;

		interfaces = new KClass[interfaceNames.length];
		for (int i = 0; i < interfaceNames.length; i++) {
			interfaces[i] = pool.bootstrapResolve(interfaceNames[i]);
		}

		firstFieldId = -1;
		lastFieldId = superClass != null ? superClass.lastFieldId : -1;

		for (KField field : fields) {
			lastFieldId++;
			if (firstFieldId == -1) {
				firstFieldId = lastFieldId;
			}
			field.setIndex(lastFieldId);
		}
	}

	public boolean isParent(KClass clazz) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");
		if (superClass == null)
			return clazz == null;
		if (clazz.equals(superClass))
			return true;
		return superClass.isParent(clazz);
	}

	public boolean isImplemented(KClass clazz) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");
		for (KClass iface : interfaces) {
			if (iface.equals(clazz) || iface.isImplemented(clazz)) {
				return true;
			}
		}
		if (superClass != null)
			return superClass.isImplemented(clazz);
		return false;
	}

	public KMethod findMethod(MethodSignature signature, boolean throwException) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		for (KMethod method : methods.values()) {
			if (method.getSignature().matches(signature) & !method.isAbstract()) {
				return method;
			}
		}

		if (superClass != null) {
			KMethod method = superClass.findMethod(signature, false);
			if (method != null)
				return method;
		}

		ArrayList<KMethod> interfaceMethods = new ArrayList<KMethod>();
		for (KClass iface : interfaces) {
			interfaceMethods.add(iface.findMethod(signature, false));
		}

		if (interfaceMethods.size() > 0) {
			if (interfaceMethods.size() != 1)
				throw new IllegalArgumentException("More than one default interface member found");
			return interfaceMethods.get(0);
		}

		if (throwException)
			throw new RuntimeException("Failed to find method " + signature);
		else
			return null;
	}

	public KField findField(FieldSignature signature) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		for (KField field : fieldMap.values()) {
			if (field.getSignature().matches(signature)) {
				return field;
			}
		}
		if (superClass == null)
			throw new RuntimeException("Failed to find field " + signature);
		return superClass.findField(signature);
	}

	public void getAllFields(List<KField> fields) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		if (superClass != null)
			superClass.getAllFields(fields);
		fields.addAll(this.fieldMap.values());
	}

	public boolean shouldSpecialMethodResolve() {
		return specialMethodResolve;
	}

	public void addMethod(KMethod method) {
		if (resolved)
			throw new IllegalStateException("Class has been resolved");
		methods.put(method.getSignature(), method);
	}

	public KMethod getMethod(MethodSignature signature) {
		return methods.get(signature);
	}

	public void addField(KField field) {
		if (resolved)
			throw new IllegalStateException("Class has been resolved");
		fieldMap.put(field.getSignature(), field);
	}

	public Collection<KField> getFields() {
		return fieldMap.values();
	}

	public KField getField(FieldSignature signature) {
		return fieldMap.get(signature);
	}

	public KClass getSuperClass() {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");
		return superClass;
	}

	public ClassType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
