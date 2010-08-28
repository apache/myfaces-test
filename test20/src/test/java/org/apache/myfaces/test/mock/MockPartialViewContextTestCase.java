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

import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;

/**
 * Test case for <code>MockPartialViewContext</code>
 */
public class MockPartialViewContextTestCase extends AbstractJsfTestCase
{

    public MockPartialViewContextTestCase(String name)
    {
        super(name);
    }

    // Set up instance variables required by this test case.

    protected void setUp() throws Exception
    {
        super.setUp();
    }

    // Return the tests included in this test case.

    public static Test suite()
    {
        return (new TestSuite(MockPartialViewContextTestCase.class));
    }

    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testIsAjaxRequest()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        PartialViewContextFactory factory = new MockPartialViewContextFactory();

        PartialViewContext pvContext = factory
                .getPartialViewContext(facesContext);
        assertFalse(pvContext.isAjaxRequest());

        externalContext.addRequestHeader("Faces-Request", "partial/ajax");

        pvContext = factory.getPartialViewContext(facesContext);
        assertTrue(pvContext.isAjaxRequest());
    }
}
