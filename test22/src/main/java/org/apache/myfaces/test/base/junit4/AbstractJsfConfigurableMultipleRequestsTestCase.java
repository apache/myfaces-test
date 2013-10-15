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

package org.apache.myfaces.test.base.junit4;

import java.net.URL;
import java.net.URLClassLoader;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;

import org.apache.myfaces.test.config.ResourceBundleVarNames;
import org.apache.myfaces.test.mock.MockExternalContext;
import org.apache.myfaces.test.mock.MockFacesContext;
import org.apache.myfaces.test.mock.MockFacesContextFactory;
import org.apache.myfaces.test.mock.MockHttpServletRequest;
import org.apache.myfaces.test.mock.MockHttpServletResponse;
import org.apache.myfaces.test.mock.MockHttpSession;
import org.apache.myfaces.test.mock.MockRenderKit;
import org.apache.myfaces.test.mock.MockServletConfig;
import org.apache.myfaces.test.mock.MockServletContext;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycle;
import org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory;
import org.junit.After;
import org.junit.Before;

/**
 * <p>Abstract JUnit 4.5 test case base class, which sets up the JavaServer Faces
 * mock object environment for multiple simulated request.  The following
 * protected variables are initialized in the <code>setUp()</code> method, and
 * cleaned up in the <code>tearDown()</code> method:</p>
 * <ul>
 * <li><code>application</code> (<code>Application</code>)</li>
 * <li><code>config</code> (<code>MockServletConfig</code>)</li>
 * <li><code>externalContext</code> (<code>ExternalContext</code>)</li>
 * <li><code>facesContext</code> (<code>FacesContext</code>)</li>
 * <li><code>lifecycle</code> (<code>Lifecycle</code>)</li>
 * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
 * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
 * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
 * <li><code>session</code> (<code>MockHttpSession</code>)</li>
 * </ul>
 *
 * <p>In addition, appropriate factory classes will have been registered with
 * <code>javax.faces.FactoryFinder</code> for <code>Application</code> and
 * <code>RenderKit</code> instances.  The created <code>FacesContext</code>
 * instance will also have been registered in the proper thread local
 * variable, to simulate what a servlet container would do.</p>
 *
 * <p><strong>WARNING</strong> - If you choose to subclass this class, be sure
 * your <code>setUp()</code> and <code>tearDown()</code> methods call
 * <code>super.setUp()</code> and <code>super.tearDown()</code> respectively,
 * and that you implement your own <code>suite()</code> method that exposes
 * the test methods for your test case. Additionally, check on each test
 * that setupRequest() and tearDownRequest() are called correctly.</p>
 * 
 * @since 1.0.3
 */

public abstract class AbstractJsfConfigurableMultipleRequestsTestCase
{

    // ------------------------------------------------------------ Constructors

    /**
     * <p>Construct a new instance of this test case.</p>
     *
     * @param name Name of this test case
     */
    public AbstractJsfConfigurableMultipleRequestsTestCase()
    {
    }

    // ---------------------------------------------------- Overall Test Methods

    /**
     * <p>Set up instance variables required by this test case.</p>
     */
    @Before
    public void setUp() throws Exception
    {

        // Set up a new thread context class loader
        threadContextClassLoader = Thread.currentThread()
                .getContextClassLoader();
        Thread.currentThread()
                .setContextClassLoader(
                        new URLClassLoader(new URL[0], this.getClass()
                                .getClassLoader()));

        // Set up JSF Factories
        FactoryFinder.releaseFactories();

        setFactories();

        // Setup servlet context and session
        setUpServletContextAndSession();
        
        setUpLifecycle();
        
        setUpApplication();
        
        setUpRenderKit();
    }

    /**
     * This method initialize an new empty request.
     */
    public void setupRequest() throws Exception
    {
        // Set up Servlet API Objects
        setUpServletRequestAndResponse();

        setUpJSFRequestObjects();
    }

    /**
     * <p>Setup JSF object used for the test. By default it calls to the following
     * methods in this order:</p>
     * 
     * <ul>
     * <li><code>setUpExternalContext();</code></li>
     * <li><code>setUpLifecycle();</code></li>
     * <li><code>setUpFacesContext();</code></li>
     * <li><code>setUpView();</code></li>
     * <li><code>setUpApplication();</code></li>
     * <li><code>setUpRenderKit();</code></li>
     * </ul>
     * 
     * @throws Exception
     */
    protected void setUpJSFRequestObjects() throws Exception
    {
        setUpExternalContext();
        setUpFacesContext();
        setUpView();
    }

    /**
     * <p>Setup servlet objects that will be used for the test:</p>
     * 
     * <ul>
     * <li><code>config</code> (<code>MockServletConfig</code>)</li>
     * <li><code>servletContext</code> (<code>MockServletContext</code>)</li>
     * <li><code>request</code> (<code>MockHttpServletRequest</code></li>
     * <li><code>response</code> (<code>MockHttpServletResponse</code>)</li>
     * <li><code>session</code> (<code>MockHttpSession</code>)</li>
     * </ul>
     * 
     * @throws Exception
     */
    protected void setUpServletContextAndSession() throws Exception
    {
        servletContext = new MockServletContext();
        config = new MockServletConfig(servletContext);
        session = new MockHttpSession();
        session.setServletContext(servletContext);
    }

    protected void setUpServletRequestAndResponse() throws Exception
    {
        request = new MockHttpServletRequest(session);
        request.setServletContext(servletContext);
        response = new MockHttpServletResponse();
    }

    /**
     * <p>Set JSF factories using FactoryFinder method setFactory.</p>
     * 
     * @throws Exception
     */
    protected void setFactories() throws Exception
    {
        FactoryFinder.setFactory(FactoryFinder.APPLICATION_FACTORY,
                "org.apache.myfaces.test.mock.MockApplicationFactory");
        FactoryFinder.setFactory(FactoryFinder.FACES_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.MockFacesContextFactory");
        FactoryFinder.setFactory(FactoryFinder.LIFECYCLE_FACTORY,
                "org.apache.myfaces.test.mock.lifecycle.MockLifecycleFactory");
        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY,
                "org.apache.myfaces.test.mock.MockRenderKitFactory");
        FactoryFinder.setFactory(FactoryFinder.EXCEPTION_HANDLER_FACTORY,
                "org.apache.myfaces.test.mock.MockExceptionHandlerFactory");
        FactoryFinder.setFactory(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.MockPartialViewContextFactory");
        FactoryFinder.setFactory(FactoryFinder.VISIT_CONTEXT_FACTORY,
                "org.apache.myfaces.test.mock.visit.MockVisitContextFactory");
        FactoryFinder.setFactory(FactoryFinder.CLIENT_WINDOW_FACTORY,
                "org.apache.myfaces.test.mock.MockClientWindowFactory");
    }

    /**
     * Setup the <code>externalContext</code> variable, using the 
     * servlet variables already initialized.
     * 
     * @throws Exception
     */
    protected void setUpExternalContext() throws Exception
    {
        externalContext = new MockExternalContext(servletContext, request,
                response);
    }

    /**
     * Setup the <code>lifecycle</code> and <code>lifecycleFactory</code>
     * variables.
     * 
     * @throws Exception
     */
    protected void setUpLifecycle() throws Exception
    {
        lifecycleFactory = (MockLifecycleFactory) FactoryFinder
                .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = (MockLifecycle) lifecycleFactory
                .getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }

    /**
     * Setup the <code>facesContextFactory</code> and <code>facesContext</code>
     * variable. Before end, by default it override <code>externalContext</code>
     * variable from the value retrieved from facesContext.getExternalContext(),
     * because sometimes it is possible facesContext overrides externalContext
     * internally.
     * 
     * @throws Exception
     */
    protected void setUpFacesContext() throws Exception
    {
        facesContextFactory = (MockFacesContextFactory) FactoryFinder
                .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
        facesContext = (MockFacesContext) facesContextFactory.getFacesContext(
                servletContext, request, response, lifecycle);
        if (facesContext.getExternalContext() != null)
        {
            externalContext = (MockExternalContext) facesContext
                    .getExternalContext();
        }
        if (facesContext instanceof MockFacesContext)
        {
            ((MockFacesContext) facesContext).setApplication(application);
        }
    }

    /**
     * By default, create an instance of UIViewRoot, set its viewId as "/viewId"
     * and assign it to the current facesContext.
     * 
     * @throws Exception
     */
    protected void setUpView() throws Exception
    {
        UIViewRoot root = new UIViewRoot();
        root.setViewId("/viewId");
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
        facesContext.setViewRoot(root);
    }

    /**
     * Setup the <code>application</code> variable and before
     * the end by default it is assigned to the <code>facesContext</code>
     * variable, calling <code>facesContext.setApplication(application)</code>
     * 
     * @throws Exception
     */
    protected void setUpApplication() throws Exception
    {
        ApplicationFactory applicationFactory = (ApplicationFactory) FactoryFinder
                .getFactory(FactoryFinder.APPLICATION_FACTORY);
        application = applicationFactory.getApplication();
    }

    /**
     * Setup the <code>renderKit</code> variable. This is a good place to use
     * <code>ConfigParser</code> to register converters, validators, components
     * or renderkits.
     * 
     * @throws Exception
     */
    protected void setUpRenderKit() throws Exception
    {
        RenderKitFactory renderKitFactory = (RenderKitFactory) FactoryFinder
                .getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        renderKit = new MockRenderKit();
        renderKitFactory.addRenderKit(RenderKitFactory.HTML_BASIC_RENDER_KIT,
            renderKit);
    }

    /**
     * <p>Tear down instance variables required by this test case.</p>
     */
    @After
    public void tearDown() throws Exception
    {

        application = null;
        config = null;
        externalContext = null;
        if (facesContext != null)
        {
            facesContext.release();
        }
        facesContext = null;
        lifecycle = null;
        lifecycleFactory = null;
        renderKit = null;
        request = null;
        response = null;
        servletContext = null;
        session = null;
        FactoryFinder.releaseFactories();
        ResourceBundleVarNames.resetNames();

        Thread.currentThread().setContextClassLoader(threadContextClassLoader);
        threadContextClassLoader = null;
    }

    /**
     * This method ends the current request.
     */
    public void tearDownRequest()
    {
        externalContext = null;
        if (facesContext != null)
        {
            facesContext.release();
        }
        facesContext = null;
        request = null;
        response = null;
    }

    // ------------------------------------------------------ Instance Variables

    // Mock object instances for our tests
    protected Application application = null;
    protected MockServletConfig config = null;
    protected ExternalContext externalContext = null;
    protected FacesContext facesContext = null;
    protected FacesContextFactory facesContextFactory = null;
    protected Lifecycle lifecycle = null;
    protected LifecycleFactory lifecycleFactory = null;
    protected RenderKit renderKit = null;
    protected MockHttpServletRequest request = null;
    protected MockHttpServletResponse response = null;
    protected MockServletContext servletContext = null;
    protected MockHttpSession session = null;

    // Thread context class loader saved and restored after each test
    private ClassLoader threadContextClassLoader = null;

}