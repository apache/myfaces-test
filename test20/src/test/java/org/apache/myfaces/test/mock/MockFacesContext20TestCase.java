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

package org.apache.myfaces.test.mock;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.myfaces.test.base.AbstractJsfTestCase;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import java.util.List;

/**
 * Test case for <code>MockFacesContext20</code>
 */
public class MockFacesContext20TestCase extends AbstractJsfTestCase
{

    private MockFacesContext20 _context;

    public MockFacesContext20TestCase(String name)
    {
        super(name);
    }

    // Set up instance variables required by this test case.

    protected void setUp() throws Exception
    {
        super.setUp();
        _context = (MockFacesContext20) facesContext;
    }

    // Return the tests included in this test case.

    public static Test suite()
    {
        return (new TestSuite(MockFacesContext20TestCase.class));
    }

    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testSetGetExceptionHandler()
    {
        ExceptionHandlerFactory handlerFactory = new MockExceptionHandlerFactory();
        ExceptionHandler handler = handlerFactory.getExceptionHandler();
        _context.setExceptionHandler(handler);
        assertEquals(handler, _context.getExceptionHandler());
    }

    public void testGetMessageList()
    {
        FacesMessage message1 = new FacesMessage("test message1");
        FacesMessage message2 = new FacesMessage("test message2");
        _context.addMessage("clientid1", message1);
        _context.addMessage("clientid2", message2);

        List<FacesMessage> messageList = _context.getMessageList();
        assertEquals(2, messageList.size());
        assertTrue(messageList.contains(message1));
        assertTrue(messageList.contains(message2));

        List<FacesMessage> messageListClientId = _context
                .getMessageList("clientid1");
        assertEquals(1, messageListClientId.size());
        assertTrue(messageListClientId.contains(message1));
    }

    public void testValidationFailed()
    {
        assertFalse(_context.isValidationFailed());
        _context.validationFailed();
        assertTrue(_context.isValidationFailed());
    }
}
