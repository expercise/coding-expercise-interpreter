package com.expercise.interpreter.core;

import com.expercise.interpreter.core.model.challenge.DataType;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TypeCheckerTest {

    @Test
    public void shouldCheckTypeAsValidIfValueIsIntegerAndSpecifiedDataTypeIsIntegerAlso() {
        assertTrue(TypeChecker.check(49, DataType.Integer));
    }

    @Test
    public void shouldCheckTypeAsValidIfValueIsTextAndSpecifiedDataTypeIsTextAlso() {
        assertTrue(TypeChecker.check("49", DataType.Text));
    }

    @Test
    public void shouldCheckTypeAsInvalidIfValueTypeAndSpecifiedTypeDoesNotMatched() {
        assertFalse(TypeChecker.check("49", DataType.Integer));
        assertFalse(TypeChecker.check(49, DataType.Text));
    }

}