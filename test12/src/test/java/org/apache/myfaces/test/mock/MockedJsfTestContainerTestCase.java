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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class MockedJsfTestContainerTestCase
{

    @Test
    public void testSimpleInit1()
    {
        MockedJsfTestContainer container = new MockedJsfTestContainer();
        
        container.setUp();
        
        Assert.assertNotNull(container.getServletContext());
        Assert.assertNotNull(container.getApplication());
        
        container.tearDown();
    }
    
    @Test
    public void testSimpleInit2()
    {
        MockedJsfTestContainer container = new MockedJsfTestContainer();
        
        container.setUpAll();
        
        Assert.assertNotNull(container.getFacesContext());
        Assert.assertNotNull(container.getExternalContext());
        Assert.assertNotNull(container.getRequest());
        Assert.assertNotNull(container.getResponse());
        
        container.tearDownAll();
    }
    
    @Test
    public void testSimpleInit3()
    {
        MockedJsfTestContainer container = new MockedJsfTestContainer();
        
        container.setUp();
        
        container.startRequest();
        
        container.startSession();
        
        MockHttpSession session1 = (MockHttpSession) 
            container.getRequest().getSession(false);
        
        Assert.assertNotNull(session1);
        
        container.endRequest();
        
        container.startRequest();
        
        MockHttpSession session2 = (MockHttpSession) 
            container.getRequest().getSession(false);
        
        Assert.assertNotNull(session2);
        
        Assert.assertEquals(session2, session1);
        
        container.endSession();
        
        container.endRequest();
        
        container.startRequest();
        
        MockHttpSession session3 = (MockHttpSession) 
            container.getRequest().getSession(false);
        
        Assert.assertNull(session3);
        
        container.endRequest();
    }

}
