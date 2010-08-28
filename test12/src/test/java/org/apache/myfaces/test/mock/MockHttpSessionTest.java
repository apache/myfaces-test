/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.myfaces.test.mock;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.base.AbstractJsfTestCase;

/**
 * Test class for MockHttpSession.
 * 
 * @author Jakob Korherr (latest modification by $Author$)
 * @version $Revision$ $Date$
 */
public class MockHttpSessionTest extends AbstractJsfTestCase
{

    public static Test suite()
    {
        return (new TestSuite(MockHttpSessionTest.class));
    }

    private MockHttpSession session;

    public MockHttpSessionTest(String name)
    {
        super(name);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        session = new MockHttpSession();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();

        session = null;
    }

    /**
     * Tests if the session is correctly invalidated.
     */
    public void testSessionInvalidate()
    {
        // first store a value in the session and retrieve it again
        session.setAttribute("someAttribute", "someValue");
        assertEquals("someValue", session.getAttribute("someAttribute"));

        // invalidate the session
        session.invalidate();

        try
        {
            session.getAttribute("someAttribute");
            fail("Session was already invalidated, getAttribute() has to throw an IllegalStateException.");
        }
        catch (IllegalStateException e)
        {
            // expected Exception
        }

        try
        {
            session.setAttribute("someAttribute", "anotherValue");
            fail("Session was already invalidated, setAttribute() has to throw an IllegalStateException.");
        }
        catch (IllegalStateException e)
        {
            // expected Exception
        }

        try
        {
            session.removeAttribute("someAttribute");
            fail("Session was already invalidated, removeAttribute() has to throw an IllegalStateException.");
        }
        catch (IllegalStateException e)
        {
            // expected Exception
        }

        try
        {
            session.invalidate();
            fail("Session was already invalidated, invalidate() has to throw an IllegalStateException.");
        }
        catch (IllegalStateException e)
        {
            // expected Exception
        }

        try
        {
            session.getAttributeNames();
            fail("Session was already invalidated, getAttributeNames() has to throw an IllegalStateException.");
        }
        catch (IllegalStateException e)
        {
            // expected Exception
        }
    }

}
