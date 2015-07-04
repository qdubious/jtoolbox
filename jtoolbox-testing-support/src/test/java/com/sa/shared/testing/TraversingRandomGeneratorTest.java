package com.sa.shared.testing;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings("unchecked")
public class TraversingRandomGeneratorTest {

	TraversingRandomGenerator generator;

	@Before
	public void before() {
		List<Class<?>> types = new ArrayList<>();
		types.add(SamplePojo.class);
		this.generator = new TraversingRandomGenerator(types);
	}

	@Test
	public void generate_recursively() throws Exception {
		Object result = this.generator.doGenerate(SamplePojo.class);
		assertThat(result, notNullValue());
		assertThat(result, instanceOf(SamplePojo.class));

		SamplePojo rojo = ((SamplePojo) result);
		assertThat(rojo.getName(), notNullValue());
		assertThat(rojo.getInteger(), notNullValue());
		assertThat(rojo.getOneToMany(), notNullValue());
		assertThat(rojo.getOneToMany().isEmpty(), is(false));
		assertThat(rojo.getOneToMany().iterator().next(), instanceOf(SamplePojo.class));
		assertThat(rojo.getManyToOne(), notNullValue());
	}

	enum SampleType {
		ONE, TWO, THREE;
	}

	static class SamplePojo {
		String name;
		Integer integer;
		Collection<SamplePojo> oneToMany;
		SamplePojo manyToOne;
		SampleType sampleType;

		// TODO: handle maps
		// Map<String, SamplePojo> sampleMap;

		public Integer getInteger() {
			return integer;
		}

		public void setInteger(Integer integer) {
			this.integer = integer;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Collection<SamplePojo> getOneToMany() {
			return oneToMany;
		}

		public void setOneToMany(Collection<SamplePojo> oneToMany) {
			this.oneToMany = oneToMany;
		}

		public SamplePojo getManyToOne() {
			return manyToOne;
		}

		public void setManyToOne(SamplePojo manyToOne) {
			this.manyToOne = manyToOne;
		}

		public SampleType getSampleType() {
			return sampleType;
		}

		public void setSampleType(SampleType sampleType) {
			this.sampleType = sampleType;
		}

		// public Map<String, SamplePojo> getSampleMap() {
		// return sampleMap;
		// }

		// public void setSampleMap(Map<String, SamplePojo> sampleMap) {
		// this.sampleMap = sampleMap;
		// }
	}
}