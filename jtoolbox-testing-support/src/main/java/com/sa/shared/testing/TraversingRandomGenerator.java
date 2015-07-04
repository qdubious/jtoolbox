package com.sa.shared.testing;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.RandomGenerator;
import com.openpojo.random.collection.CollectionRandomGenerator;
import com.openpojo.random.map.MapRandomGenerator;
import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.PojoField;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.utils.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;

public class TraversingRandomGenerator implements RandomGenerator {
	static final Logger logger = LoggerFactory.getLogger(TraversingRandomGenerator.class);

	Collection<Class<?>> types = new HashSet<>();
	Map<PojoField, Object> generated = new HashMap<>();

	public TraversingRandomGenerator(Collection<Class<?>> types) {
		assert types != null;
		this.types.addAll(types);
	}

	@Override
	public Collection<Class<?>> getTypes() {
		return this.types;
	}

	@Override
	public Object doGenerate(Class<?> type) {
		Object result = RandomFactory.getRandomValue(type);
		this.visitProperties(result);
		return result;
	}

	<T> void visitProperties(T obj) {
		PojoClass pojo = PojoClassFactory.getPojoClass(obj.getClass());
		for (PojoField pField : pojo.getPojoFields()) {
			if (!(pField.hasSetter() && pField.hasGetter()))
				continue;
			Object value = pField.invokeGetter(obj);
			if (this.generated.containsKey(pField))
				value = this.generated.get(pField);
			else if (value == null) {
				if (Collection.class.isAssignableFrom(pField.getType()))
					value = this.handleCollections(pField);
				else if (Map.class.isAssignableFrom(pField.getType()))
					value = this.handleMaps(pField);
				else
					value = RandomFactory.getRandomValue(pField.getType());

				if (!pField.getType().getPackage().getName().startsWith("java")) {
					this.generated.put(pField, value);
					this.visitProperties(value);
				}
			}
			pField.invokeSetter(obj, value);
		}
	}

	@SuppressWarnings("unchecked")
	<T extends Collection> T handleCollections(PojoField field) {
		if (!field.isParameterized())
			return (T) Collections.emptyList();
		List<Type> typeArguments = field.getParameterTypes();
		if (typeArguments.isEmpty())
			return (T) Collections.emptyList();
		Collection collection = CollectionRandomGenerator.getInstance().doGenerate(field.getType());
		collection.clear();
		collection.add(RandomFactory.getRandomValue((Class<Object>) typeArguments.iterator().next()));
		return (T) collection;
	}

	@SuppressWarnings("unchecked")
	<T extends Map> T handleMaps(PojoField field) {
		if (!field.isParameterized())
			return (T) Collections.emptyMap();
		List<Type> typeArguments = field.getParameterTypes();
		if (typeArguments.isEmpty())
			return (T) Collections.emptyMap();
		
        Type keyType = typeArguments.get(0);
        Object key = ValidationHelper.getBasicInstance(PojoClassFactory.getPojoClass((Class<?>) keyType));
        this.visitProperties(key);
        
		Type valueType = typeArguments.get(1);
        Object value = ValidationHelper.getBasicInstance(PojoClassFactory.getPojoClass((Class<?>) valueType));
        this.visitProperties(value); 

		Map map = MapRandomGenerator.getInstance().doGenerate(field.getType());
		map.clear();
        map.put(key, value);
		return null;
	}
}
