package com.sa.shared.testing;

import com.openpojo.random.RandomFactory;
import com.openpojo.random.RandomGenerator;

import java.util.Collection;

public class RandomService {
    static {
		RandomFactory.addRandomGenerator(new TraversingRandomGenerator());
    }

	public <T> T generate(Class<T> type) {
		return null;
	}

	static class TraversingRandomGenerator implements RandomGenerator {
        @Override
        public Collection<Class<?>> getTypes() {
            return null;
        }

        @Override
        public Object doGenerate(Class<?> type) {
            return null;
        }
    }
}
