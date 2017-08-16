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
	private Map<FieldSignature, KField> fields;
	private KClass superClass;
	private KClass[] interfaces;

	public KClass(String name, ClassType type, boolean specialMethodResolve, String superName,
			String[] interfaceNames) {
		this.name = name;
		this.type = type;
		this.specialMethodResolve = specialMethodResolve;
		this.superName = superName;
		this.interfaceNames = interfaceNames;
		methods = new ConcurrentHashMap<MethodSignature, KMethod>();
		fields = new ConcurrentHashMap<FieldSignature, KField>();
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

	public KMethod findMethod(MethodSignature signature, boolean checkInterfaces, boolean throwException) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		for (KMethod method : methods.values()) {
			if (method.getSignature().matches(signature)) {
				return method;
			}
		}

		if (superClass != null) {
			KMethod method = superClass.findMethod(signature, false, false);
			if (method != null)
				return method;
		}

		if (checkInterfaces) {
			ArrayList<KMethod> interfaceMethods = new ArrayList<KMethod>();
			findDefaultInterfaceMethods(interfaceMethods, signature);
			if (interfaceMethods.size() > 0) {
				if (interfaceMethods.size() != 1)
					throw new IllegalArgumentException("More than one default interface member found");
				return interfaceMethods.get(0);
			}
		}

		if (throwException)
			throw new RuntimeException("Failed to find method " + signature);
		else
			return null;
	}

	public void findDefaultInterfaceMethods(List<KMethod> list, MethodSignature signature) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		if (type == ClassType.Interface) {
			for (KMethod method : methods.values()) {
				if (method.getSignature().matches(signature) && !method.isAbstract()) {
					list.add(method);
				}
			}
		}

		for (KClass i : interfaces) {
			i.findDefaultInterfaceMethods(list, signature);
		}

		if (superClass != null)
			superClass.findDefaultInterfaceMethods(list, signature);
	}

	public KField findField(FieldSignature signature) {
		if (!resolved)
			throw new IllegalStateException("Class has not been resolved");

		for (KField field : fields.values()) {
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
		fields.addAll(this.fields.values());
	}

	public boolean shouldSpecialMethodResolve() {
		return specialMethodResolve;
	}

	public void addMethod(KMethod method) {
		methods.put(method.getSignature(), method);
	}

	public KMethod getMethod(MethodSignature signature) {
		return methods.get(signature);
	}

	public void addField(KField field) {
		fields.put(field.getSignature(), field);
	}

	public Collection<KField> getFields() {
		return fields.values();
	}

	public KField getField(FieldSignature signature) {
		return fields.get(signature);
	}

	public String getName() {
		return name;
	}

}
