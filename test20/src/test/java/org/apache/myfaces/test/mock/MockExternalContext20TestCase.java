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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Test case for <code>MockExternalContext20</code>
 */
public class MockExternalContext20TestCase extends AbstractJsfTestCase
{

    private MockExternalContext20 _context;

    public MockExternalContext20TestCase(String name)
    {
        super(name);
    }

    // Set up instance variables required by this test case.

    protected void setUp() throws Exception
    {
        super.setUp();
        _context = (MockExternalContext20) externalContext;
    }

    // Return the tests included in this test case.

    public static Test suite()
    {
        return (new TestSuite(MockExternalContext20TestCase.class));
    }

    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testEncodeBookmarkableURL()
    {
        HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
        List<String> valueList = new ArrayList<String>();
        valueList.add("value1");
        parameters.put("param1", valueList);

        assertEquals("http://localhost:8080?param1=value1", _context
                .encodeBookmarkableURL("http://localhost:8080", parameters));
    }

    public void testEncodeRedirectURL()
    {
        HashMap<String, List<String>> parameters = new HashMap<String, List<String>>();
        List<String> valueList = new ArrayList<String>();
        valueList.add("value1");
        parameters.put("param1", valueList);

        assertEquals("http://localhost:8080?param1=value1", _context
                .encodeRedirectURL("http://localhost:8080", parameters));
    }

    public void testGetContextName()
    {
        assertEquals("MockServletContext", _context.getContextName());
    }

    public void testResponseSendError() throws Exception
    {
        _context.responseSendError(404, "not found");
        assertEquals(404, ((MockHttpServletResponse) _context.getResponse())
                .getStatus());
    }

    public void testResponseHeader() throws Exception
    {
        _context.setResponseHeader("header1", "value1");
        assertEquals("value1", ((MockHttpServletResponse) _context
                .getResponse()).getHeader("header1"));
    }

    public void testGetRequestScheme()
    {
        assertEquals("http", _context.getRequestScheme());
    }

    public void testGetRequestServerName()
    {
        assertEquals("localhost", _context.getRequestServerName());
    }

    public void testGetRequestServerPort()
    {
        assertEquals(8080, _context.getRequestServerPort());
    }

    public void testGetResponseOutputStream() throws Exception
    {
        assertNotNull(_context.getResponseOutputStream());
    }

    public void testGetResponseOutputWriter() throws Exception
    {
        assertNotNull(_context.getResponseOutputWriter());
    }

    public void testGetFlash() throws Exception
    {
        assertNotNull(_context.getFlash());
    }
}
