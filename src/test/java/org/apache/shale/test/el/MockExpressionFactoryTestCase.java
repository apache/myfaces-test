/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shale.test.el;

import javax.el.ELContext;
import javax.el.PropertyNotWritableException;
import javax.el.ValueExpression;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.MockApplication12;

/**
 * <p>Test case for <code>MockExpressionFactory.</p>
 */
public class MockExpressionFactoryTestCase extends AbstractJsfTestCase {
    

    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.
    public MockExpressionFactoryTestCase(String name) {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.
    protected void setUp() throws Exception {

        super.setUp();
        factory = (MockExpressionFactory)
          ((MockApplication12) application).getExpressionFactory();

    }


    // Return the tests included in this test case.
    public static Test suite() {

        return (new TestSuite(MockExpressionFactoryTestCase.class));

    }


    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception {

        factory = null;
        super.tearDown();

    }


    // -------------------------------------------------------- Static Variables


    /**
     * <p>Input values to be converted, of all the interesting types.</p>
     */
    private static final Object[] INPUT_VALUES = {
        (String) null,
        "",
        "1234",
        Boolean.TRUE,
        Boolean.FALSE,
        new Byte((byte) 123),
        new Double(234),
        new Float(345),
        new Integer(456),
        new Long(567),
        new Short((short) 678),
    };


    /**
     * <p>Output values when converted to Boolean.</p>
     */
    private static final Boolean[] OUTPUT_BOOLEAN = {
        Boolean.FALSE,
        Boolean.FALSE,
        Boolean.FALSE,
        Boolean.TRUE,
        Boolean.FALSE,
        null,
        null,
        null,
        null,
        null,
        null
    };


    /**
     * <p>Output values when converted to Integer.</p>
     */
    private static final Integer[] OUTPUT_INTEGER = {
        new Integer(0),
        new Integer(0),
        new Integer(1234),
        new Integer(1),
        new Integer(0),
        new Integer(123),
        new Integer(234),
        new Integer(345),
        new Integer(456),
        new Integer(567),
        new Integer(678)
    };


    /**
     * <p>Output values when converted to String.</p>
     */
    private static final String[] OUTPUT_STRING = {
        "",
        "",
        "1234",
        "true",
        "false",
        "123",
        "234.0",
        "345.0",
        "456",
        "567",
        "678"
    };


    // ------------------------------------------------------ Instance Variables


    /**
     * <p>The expression factory to be tested.</p>
     */
    private MockExpressionFactory factory = null;


    // ------------------------------------------------- Individual Test Methods


    // Test coercion when expectedType is passed as Boolean.
    public void testCoerceToBoolean() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            try {
                result = factory.coerceToType(INPUT_VALUES[i], Boolean.class);
                if ((INPUT_VALUES[i] == null)
                 || (INPUT_VALUES[i] instanceof String)
                 || (INPUT_VALUES[i] instanceof Boolean)) {
                    assertEquals("[" + i + "]", OUTPUT_BOOLEAN[i], result);
                } else {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                }
            } catch (IllegalArgumentException e) {
                if ((INPUT_VALUES[i] == null)
                 || (INPUT_VALUES[i] instanceof String)
                 || (INPUT_VALUES[i] instanceof Boolean)) {
                    fail("[" + i + "] threw IllegalArgumentException");
                } else {
                    ; // Expected result
                }
            }
        }

    }


    // Test coercion when expectedType is passed as null.  We should
    // get the original object back
    public void testCoerceToNull() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], null);
            if (INPUT_VALUES[i] == null) {
                assertNull(result);
            } else {
                assertTrue("[" + i + "]", result == INPUT_VALUES[i]);
            }
        }

    }


    // Test coercion when expectedType is Object.  We should
    // get the original object back.
    public void testCoerceToObject() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], Object.class);
            if (INPUT_VALUES[i] == null) {
                assertNull(result);
            } else {
                assertTrue("[" + i + "]", result == INPUT_VALUES[i]);
            }
        }

    }


    // Test coercion when expectedType is Integer
    public void testCoerceToInteger() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            try {
                result = factory.coerceToType(INPUT_VALUES[i], Integer.class);
                if ((INPUT_VALUES[i] != null)
                 && (INPUT_VALUES[i] instanceof Boolean)) {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                } else {
                    assertEquals("[" + i + "]", OUTPUT_INTEGER[i], result);
                }
            } catch (IllegalArgumentException e) {
                if ((INPUT_VALUES[i] != null)
                 && (INPUT_VALUES[i] instanceof Boolean)) {
                    ; // Expected result
                } else {
                    fail("[" + i + "] should have thrown IllegalArgumentException");
                }
            }
        }

    }


    // Test coercion when expectedType is String.
    public void testCoerceToString() {

        Object result = null;
        for (int i = 0; i < INPUT_VALUES.length; i++) {
            result = factory.coerceToType(INPUT_VALUES[i], String.class);
            assertEquals("[" + i + "]", OUTPUT_STRING[i], result);
        }

    }


    // Test ValueExpression that wraps a literal String object and conversion to Integer
    public void testLiteralValueExpressionInteger() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression("123", Integer.class);
        assertEquals(Integer.class, expr.getExpectedType());
        assertEquals(String.class, expr.getType(context));
        assertEquals(new Integer(123), expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, "234");
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression that wraps a literal String object and no conversion
    public void testLiteralValueExpressionNone() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression("abc", String.class);
        assertEquals(String.class, expr.getExpectedType());
        assertEquals(String.class, expr.getType(context));
        assertEquals("abc", expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, "def");
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression that wraps a literal Integer object and conversion to String
    public void testLiteralValueExpressionString() {

        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression(new Integer(123), String.class);
        assertEquals(String.class, expr.getExpectedType());
        assertEquals(Integer.class, expr.getType(context));
        assertEquals("123", expr.getValue(context));
        assertTrue(expr.isLiteralText());
        assertTrue(expr.isReadOnly(context));
        try {
            expr.setValue(context, new Integer(234));
            fail("Should have thrown PropertyNotWritableException");
        } catch (PropertyNotWritableException e) {
            ; // Expected result
        }

    }


    // Test ValueExpression
    public void testValueExpressionString() {

        request.setAttribute("org.apache.shale.test", new Integer(123));
        ELContext context = facesContext.getELContext();

        ValueExpression expr = factory.createValueExpression(context, "#{requestScope['org.apache.shale.test']}", String.class);
        Object ref = expr.getValue(context);
        assertNotNull(ref);
        assertTrue(ref instanceof String);
        assertEquals("123", ref);
    }

    public void testPristine() {

        assertNotNull(factory);

    }



}
