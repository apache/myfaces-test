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

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.mock.resource.MockSimpleResource;
import org.apache.myfaces.test.mock.resource.MockSimpleResourceHandler;

/**
 * Test case for resource handling
 */
public class MockResourceTestCase extends AbstractJsfTestCase
{

    private File _documentRoot;

    public MockResourceTestCase(String name)
    {
        super(name);
    }

    // Set up instance variables required by this test case.
    protected void setUp() throws Exception
    {
        super.setUp();
        _documentRoot = new File(
                "src/test/resources/org/apache/myfaces/test/mock/resources");
    }

    // Return the tests included in this test case.
    public static Test suite()
    {
        return (new TestSuite(MockResourceTestCase.class));
    }

    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetResource() throws Exception
    {

        Resource resource = new MockSimpleResource(null, "testlib", null,
                "testfile.js", null, _documentRoot);

        URL resourceUrl = resource.getURL();
        assertNotNull("Could not find resource", resourceUrl);
        assertTrue(resourceUrl.toString().endsWith(
                "org/apache/myfaces/test/mock/resources/testlib/testfile.js"));
    }

    public void testGetNotExistingResource() throws Exception
    {

        Resource resource = new MockSimpleResource(null, "testlib", null,
                "notexisting.js", null, _documentRoot);

        assertNull(resource.getURL());
    }

    public void testGetAsStream() throws Exception
    {
        Resource resource = new MockSimpleResource(null, "testlib", null,
                "testfile.js", null, _documentRoot);
        InputStream stream = resource.getInputStream();
        assertNotNull(stream);
        assertTrue(stream.read() != -1);
    }

    public void testCreateResource() throws Exception
    {
        ResourceHandler handler = new MockSimpleResourceHandler(_documentRoot);
        Resource resource = handler.createResource("testfile.js", "testlib");
        assertNotNull("resource could not be created", resource);
        assertTrue(resource.getURL().toString().endsWith(
                "org/apache/myfaces/test/mock/resources/testlib/testfile.js"));
    }

    public void testResourceHandler() throws Exception
    {
        ResourceHandler handler = new MockSimpleResourceHandler(_documentRoot);

        assertTrue(handler.libraryExists("testlib"));
        assertFalse(handler.libraryExists("notexistinglib"));

        assertEquals("javax.faces.resource.Script", handler
                .getRendererTypeForResourceName("testfile.js"));
    }

}
