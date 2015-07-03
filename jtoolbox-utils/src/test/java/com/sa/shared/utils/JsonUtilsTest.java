package com.sa.shared.utils;

import com.openpojo.random.RandomFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class JsonUtilsTest {
	static final Logger logger = LoggerFactory.getLogger(JsonUtilsTest.class);

	static class SamplePojo {
		String property;
		String propertyOne;
		String propertyTwo;

		Map<String, Object> propertyThree;

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;

			if (!(o instanceof SamplePojo))
				return false;

			SamplePojo that = (SamplePojo) o;

			return new EqualsBuilder()
					.append(getProperty(), that.getProperty())
					.append(getPropertyOne(), that.getPropertyOne())
					.append(getPropertyTwo(), that.getPropertyTwo())
					.append(getPropertyThree(), that.getPropertyThree())
					.isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37)
					.append(getProperty())
					.append(getPropertyOne())
					.append(getPropertyTwo())
					.append(getPropertyThree())
					.toHashCode();
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getPropertyOne() {
			return propertyOne;
		}

		public void setPropertyOne(String propertyOne) {
			this.propertyOne = propertyOne;
		}

		public Map<String, Object> getPropertyThree() {
			return propertyThree;
		}

		public void setPropertyThree(Map<String, Object> propertyThree) {
			this.propertyThree = propertyThree;
		}

		public String getPropertyTwo() {
			return propertyTwo;
		}

		public void setPropertyTwo(String propertyTwo) {
			this.propertyTwo = propertyTwo;
		}
	}

	@Test
	public void to_from_json() throws Exception {
		SamplePojo source = RandomFactory.getRandomValue(SamplePojo.class);
		String json = JsonUtils.toJson(source);
		logger.debug("{}", json);
		assertThat(json, notNullValue());
		SamplePojo destination = JsonUtils.fromJson(json, SamplePojo.class);
		logger.debug("{}", ToStringBuilder.reflectionToString(destination));
		assertThat(destination, equalTo(source));
	}

	@Test
	public void from_json_stream() throws Exception {

	}

	@Test
	public void copy_to_string_from_stream() throws Exception {

	}
}