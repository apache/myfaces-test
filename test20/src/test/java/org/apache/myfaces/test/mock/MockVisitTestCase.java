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
import org.apache.myfaces.test.mock.visit.MockVisitCallback;
import org.apache.myfaces.test.mock.visit.MockVisitContextFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;

/**
 * Test case for <code>MockVisitContext</code>
 */
public class MockVisitTestCase extends AbstractJsfTestCase
{

    public MockVisitTestCase(String name)
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
        return (new TestSuite(MockVisitTestCase.class));
    }

    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testVisitTree()
    {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        VisitContextFactory factory = new MockVisitContextFactory();
        VisitContext visitContext = factory.getVisitContext(facesContext, null,
                null);

        VisitCallback callback = new MockVisitCallback();
        UIComponent component = facesContext.getViewRoot();
        assertEquals(VisitResult.ACCEPT, visitContext.invokeVisitCallback(
                component, callback));
    }

}
