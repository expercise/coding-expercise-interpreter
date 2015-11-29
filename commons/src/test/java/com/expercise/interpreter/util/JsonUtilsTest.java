package com.expercise.interpreter.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class JsonUtilsTest {

    @Test
    public void shouldConvertTestObjectToJsonString() {
        String jsonString = JsonUtils.toJsonString(new TestObject(13, "Thirteen"));

        assertEquals(jsonString, "{\"fieldOne\":13,\"fieldTwo\":\"Thirteen\"}");
    }

    @Test
    public void shouldConvertJsonStringToTestObject() {
        TestObject object = JsonUtils.fromJsonString("{\"fieldOne\":13,\"fieldTwo\":\"Thirteen\"}", TestObject.class);

        assertEquals(object.getFieldOne(), 13);
        assertEquals(object.getFieldTwo(), "Thirteen");
    }

    @Test
    public void shouldConvertCollectionToJsonString() {
        String jsonString = JsonUtils.toJsonString(Arrays.asList(0, 1, 1, 2, 3));

        assertEquals(jsonString, "[0,1,1,2,3]");
    }

    private static class TestObject {

        private int fieldOne;

        private String fieldTwo;

        public TestObject() {
        }

        public TestObject(int fieldOne, String fieldTwo) {
            this.fieldOne = fieldOne;
            this.fieldTwo = fieldTwo;
        }

        public int getFieldOne() {
            return fieldOne;
        }

        public void setFieldOne(int fieldOne) {
            this.fieldOne = fieldOne;
        }

        public String getFieldTwo() {
            return fieldTwo;
        }

        public void setFieldTwo(String fieldTwo) {
            this.fieldTwo = fieldTwo;
        }

    }

}