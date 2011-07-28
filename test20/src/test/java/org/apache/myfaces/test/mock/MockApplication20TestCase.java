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

import javax.faces.application.ProjectStage;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.base.AbstractJsfTestCase;

/**
 * Test case for <code>MockApplication20</code>
 */
public class MockApplication20TestCase extends AbstractJsfTestCase
{

    private MockApplication20 _application;

    public MockApplication20TestCase(String name)
    {
        super(name);
    }

    // Set up instance variables required by this test case.

    protected void setUp() throws Exception
    {
        super.setUp();
        _application = (MockApplication20) application;
    }

    // Return the tests included in this test case.

    public static Test suite()
    {
        return (new TestSuite(MockApplication20TestCase.class));
    }

    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetProjectStage()
    {
        assertEquals(ProjectStage.Production, _application.getProjectStage());
    }

    public void testBehavior()
    {
        String behaviorId = "BehaviorBaseId";

        _application.addBehavior(behaviorId,
                "javax.faces.component.behavior.BehaviorBase");

        assertTrue("Behavior not added", _application.getBehaviorIds()
                .hasNext());
        assertEquals(behaviorId, _application.getBehaviorIds().next());

        Behavior createdBehavior = _application.createBehavior(behaviorId);
        assertNotNull("Behavior not created", createdBehavior);
    }

    public void testDefaultValidator()
    {
        String validatorId = "testValidator";
        String validatorClass = "javax.faces.validator.LengthValidator";

        _application.addValidator(validatorId, validatorClass);
        assertTrue(_application.getValidatorIds().hasNext());

        _application.addDefaultValidatorId(validatorId);
        assertTrue(_application.getDefaultValidatorInfo().containsKey(
                validatorId));
    }
    
    public void testPublishEvent()
    {
        application.subscribeToEvent(PostAddToViewEvent.class, new SystemEventListener()
        {
            
            public void processEvent(SystemEvent event)
            {

            }
            
            public boolean isListenerForSource(Object source)
            {
                return source instanceof UIViewRoot;
            }
        });

        application.publishEvent(facesContext, PostAddToViewEvent.class, facesContext.getViewRoot());
    }
}
