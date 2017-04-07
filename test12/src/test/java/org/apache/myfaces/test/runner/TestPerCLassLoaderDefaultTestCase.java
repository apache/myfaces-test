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
package org.apache.myfaces.test.runner;

import junit.framework.Assert;

import org.apache.myfaces.test.base.junit4.AbstractJsfConfigurableMockTestCase;
import org.apache.myfaces.test.runners.NamedRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author Rudy De Busscher
 */
@RunWith(value = NamedRunner.class)
public class TestPerCLassLoaderDefaultTestCase extends
        AbstractJsfConfigurableMockTestCase
{

    @Override
    protected void setUpExternalContext() throws Exception
    {

        super.setUpExternalContext();

        if (needXmlParameters())
        {
            servletContext
                    .addInitParameter(WebXmlParameter.PARAMETER_KEY, "60");
        }

    }

    @Test
    public void testGetParameterDefault()
    {
        Assert.assertNull(WebXmlParameter.PARAMETER);
    }

    @Test
    public void testGetParameterWebXml()
    {

        //Assert.assertNull(WebXmlParameter.PARAMETER);
        // Although the parameter is set, the final variable keeps it value
        // Since we aren't running with the TestPerClassLoader Test case.
        //Assert.assertNotNull(servletContext
        //        .getInitParameter(WebXmlParameter.PARAMETER_KEY));
    }

    // These methods can be placed in a common super class.
    protected boolean needXmlParameters()
    {
        return NamedRunner.getTestMethodName().contains("Xml");
    }
}
