/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.myfaces.test.mock;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.config.ResourceBundleVarNames;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;

/**
 * @author Rudy De Busscher
 */
public class ResourceBundleTest extends AbstractJsfTestCase
{

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ResourceBundleVarNames.addVarName("msg", "org.apache.myfaces.test.config.test");
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * <p>Construct a new instance of this test case.</p>
     *
     * @param name Name of this test case
     */
    public ResourceBundleTest(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return (new TestSuite(ResourceBundleTest.class));
    }

    public void testResourceBundleNonExistent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ResourceBundle rb = application.getResourceBundle(context, "xx");
        Assert.assertNull(rb);

    }

    public void testResourceBundleExistent() {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ResourceBundle rb = application.getResourceBundle(context, "msg");
        Assert.assertNotNull(rb);

    }
}
