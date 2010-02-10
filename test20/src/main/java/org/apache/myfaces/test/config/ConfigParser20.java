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

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import java.io.IOException;
import java.net.URL;

/**
 * <p>Extended {@link org.apache.myfaces.test.config.ConfigParser ConfigParser} to cover JSF 2.0 artifacts</p>
 * <p/>
 * <p>The following artifacts are registered additionally:</p>
 * <ul>
 * <li><code>Behavior</code></li>
 * </ul>
 *
 * @since 2.0
 */
public class ConfigParser20 extends ConfigParser
{

    /**
     * Creates a new instance of ConfigParser
     */
    public ConfigParser20()
    {
        super();
    }

    /**
     * <p>The <code>Digester</code> instance we will use for parsing.</p>
     */
    private Digester digester = null;

    @Override
    public void parse(URL url) throws IOException, SAXException
    {

        super.parse(url);

        // Acquire and configure the Digester instance we will use
        Digester digester = digester();
        ApplicationFactory factory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        Application application = factory.getApplication();
        digester.push(application);

        // Perform the required parsing
        try {
            digester.parse(url);
        }
        finally {
            digester.clear();
        }

    }


    @Override
    public void parse(URL[] urls) throws IOException, SAXException
    {

        for (URL url : urls) {
            parse(url);
        }
    }


    /**
     * @return Return the <code>Digester</code> instance we will use for parsing,
     *         creating and configuring a new instance if necessary.
     */
    private Digester digester()
    {

        if (this.digester == null) {
            this.digester = new Digester();

            digester.addRule("faces-config/behavior", new BehaviorRule());
            digester.addCallMethod
                ("faces-config/behavior/behavior-id", "setBehaviorId", 0);
            digester.addCallMethod
                ("faces-config/behavior/behavior-class", "setBehaviorClass", 0);
        }
        return this.digester;

    }

    /**
     * <p>Data bean that stores information related to a behavior.</p>
     */
    class BehaviorBean
    {

        private String behaviorClass;

        public String getBehaviorClass()
        {
            return this.behaviorClass;
        }

        @SuppressWarnings("unused")
        public void setBehaviorClass(String behaviorClass)
        {
            this.behaviorClass = behaviorClass;
        }

        private String behaviorId;

        public String getBehaviorId()
        {
            return this.behaviorId;
        }

        @SuppressWarnings("unused")
        public void setBehaviorId(String behaviorId)
        {
            this.behaviorId = behaviorId;
        }

    }


    /**
     * <p>Digester <code>Rule</code> for processing behaviors.</p>
     */
    class BehaviorRule extends Rule
    {

        public void begin(String namespace, String name, Attributes attributes)
        {
            getDigester().push(new BehaviorBean());
        }

        public void end(String namespace, String name)
        {
            BehaviorBean bean = (BehaviorBean) getDigester().pop();
            Application application = (Application) getDigester().peek();
            application.addBehavior(bean.getBehaviorId(), bean.getBehaviorClass());
        }

    }
}
