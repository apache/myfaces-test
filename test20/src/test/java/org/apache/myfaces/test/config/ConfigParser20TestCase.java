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
package org.apache.myfaces.test.config;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.mock.MockApplication20;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test case for <code>ConfigParser20</code>
 */
public class ConfigParser20TestCase extends AbstractJsfTestCase
{


    // ------------------------------------------------------------ Constructors


    // Construct a new instance of this test case.

    public ConfigParser20TestCase(String name)
    {
        super(name);
    }


    // ---------------------------------------------------- Overall Test Methods


    // Set up instance variables required by this test case.

    protected void setUp() throws Exception
    {

        super.setUp();
        parser = new ConfigParser20();
        application20 = (MockApplication20) application;

    }


    // Return the tests included in this test case.

    public static Test suite()
    {

        return (new TestSuite(ConfigParser20TestCase.class));

    }


    // Tear down instance variables required by this test case.

    protected void tearDown() throws Exception
    {

        parser = null;
        super.tearDown();

    }


    // ------------------------------------------------------ Instance Variables


    // ConfigParser instance under test
    ConfigParser20 parser = null;

    MockApplication20 application20 = null;

    // ------------------------------------------------- Individual Test Methods

    @SuppressWarnings("unchecked")
    public void testSimple() throws Exception
    {

        URL url = this.getClass().getResource("/org/apache/myfaces/test/config/myfaces20-faces-config-1.xml");
        assertNotNull(url);
        parser.parse(url);
        Iterator items;
        List list = new ArrayList();

        items = application20.getComponentTypes();
        list.clear();
        while (items.hasNext()) {
            list.add(items.next());
        }
        assertTrue(list.contains("component-type-1"));
        assertTrue(list.contains("component-type-2"));

        items = application20.getBehaviorIds();
        list.clear();
        while (items.hasNext()) {
            list.add(items.next());
        }
        assertTrue(list.contains("behavior-1"));
        assertTrue(list.contains("behavior-2"));
    }
}
