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

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.convert.Converter;
import javax.faces.render.Renderer;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.myfaces.test.base.AbstractJsfTestCase;
import org.apache.myfaces.test.config.ConfigParser;

/**
 * <p>Unit tests for the configuration parser utility class.</p>
 */
public class ConfigParserTestCase extends AbstractJsfTestCase
{

    // ------------------------------------------------------------ Constructors

    // Construct a new instance of this test case.
    public ConfigParserTestCase(String name)
    {
        super(name);
    }

    // ---------------------------------------------------- Overall Test Methods

    // Set up instance variables required by this test case.
    protected void setUp() throws Exception
    {

        super.setUp();
        parser = new ConfigParser();

    }

    // Return the tests included in this test case.
    public static Test suite()
    {

        return (new TestSuite(ConfigParserTestCase.class));

    }

    // Tear down instance variables required by this test case.
    protected void tearDown() throws Exception
    {

        parser = null;
        super.tearDown();

    }

    // ------------------------------------------------------ Instance Variables

    // ConfigParser instance under test
    ConfigParser parser = null;

    // ------------------------------------------------- Individual Test Methods

    // Test access to the platform configuration resources
    public void testPlatform() throws Exception
    {

        // Make sure we can acquire a good set of URLs
        URL[] urls = parser.getPlatformURLs();
        assertNotNull(urls);
        assertEquals(1, urls.length);
        assertNotNull(urls[0]);

        // Now can we actually parse them?
        parser.parse(urls);

    }

    // Test a pristine instance
    public void testPristine()
    {

        assertNotNull(parser);

    }

    // Test loading a simple configuration resource
    public void testSimple() throws Exception
    {

        URL url = this.getClass().getResource(
                "/org/apache/myfaces/test/config/faces-config-1.xml");
        assertNotNull(url);
        parser.parse(url);
        Iterator items = null;
        List list = new ArrayList();

        items = application.getComponentTypes();
        list.clear();
        while (items.hasNext())
        {
            list.add(items.next());
        }
        assertTrue(list.contains("component-type-1"));
        assertTrue(list.contains("component-type-2"));

        items = application.getConverterIds();
        list.clear();
        while (items.hasNext())
        {
            list.add(items.next());
        }
        assertTrue(list.contains("converter-id-1"));
        assertTrue(list.contains("converter-id-2"));

        Converter converter = application.createConverter(Integer.class);
        assertNotNull(converter);
        assertTrue(converter instanceof MyConverter);

        items = application.getValidatorIds();
        list.clear();
        while (items.hasNext())
        {
            list.add(items.next());
        }
        assertTrue(list.contains("validator-id-1"));
        assertTrue(list.contains("validator-id-2"));

        Renderer renderer = renderKit.getRenderer("component-family-1",
                "renderer-type-1");
        assertNotNull(renderer);
        assertTrue(renderer instanceof MyRenderer);

        renderer = renderKit.getRenderer("component-family-2",
                "renderer-type-2");
        assertNotNull(renderer);
        assertTrue(renderer instanceof MyRenderer);

    }

    // --------------------------------------------------------- Private Methods

}
