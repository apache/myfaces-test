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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.test.mock.resource.MockResourceHandler;

import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.application.ResourceHandler;
import javax.faces.component.behavior.Behavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.event.SystemEventListenerHolder;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>Mock implementation of <code>Application</code> that includes the semantics
 * added by JavaServer Faces 2.0.</p>
 * 
 * @author Leonardo Uribe
 * @since 1.0.0
 *
 */
public class MockApplication20 extends MockApplication12
{

    // ------------------------------------------------------------ Constructors

    public MockApplication20()
    {
        super();

        // install the 2.0-ViewHandler-Mock
        this.setViewHandler(new MockViewHandler20());
        this.setResourceHandler(new MockResourceHandler());
    }

    private static class SystemListenerEntry
    {
        private List<SystemEventListener> _lstSystemEventListener;
        private Map<Class<?>, List<SystemEventListener>> _sourceClassMap;

        public SystemListenerEntry()
        {
        }

        public void addListener(SystemEventListener listener)
        {
            assert listener != null;

            addListenerNoDuplicate(getAnySourceListenersNotNull(), listener);
        }

        public void addListener(SystemEventListener listener, Class<?> source)
        {
            assert listener != null;

            if (source == null)
            {
                addListener(listener);
            }
            else
            {
                addListenerNoDuplicate(
                        getSpecificSourceListenersNotNull(source), listener);
            }
        }

        public void removeListener(SystemEventListener listener)
        {
            assert listener != null;

            if (_lstSystemEventListener != null)
            {
                _lstSystemEventListener.remove(listener);
            }
        }

        public void removeListener(SystemEventListener listener,
                Class<?> sourceClass)
        {
            assert listener != null;

            if (sourceClass == null)
            {
                removeListener(listener);
            }
            else
            {
                if (_sourceClassMap != null)
                {
                    List<SystemEventListener> listeners = _sourceClassMap
                            .get(sourceClass);
                    if (listeners != null)
                    {
                        listeners.remove(listener);
                    }
                }
            }
        }

        public void publish(Class<? extends SystemEvent> systemEventClass,
                Class<?> classSource, Object source, SystemEvent event)
        {
            if (source != null && _sourceClassMap != null)
            {
                event = _traverseListenerList(_sourceClassMap.get(classSource),
                        systemEventClass, source, event);
            }

            _traverseListenerList(_lstSystemEventListener, systemEventClass,
                    source, event);
        }

        private void addListenerNoDuplicate(
                List<SystemEventListener> listeners,
                SystemEventListener listener)
        {
            if (!listeners.contains(listener))
            {
                listeners.add(listener);
            }
        }

        private synchronized List<SystemEventListener> getAnySourceListenersNotNull()
        {
            if (_lstSystemEventListener == null)
            {
                /*
                 * TODO: Check if modification occurs often or not, might have to use a synchronized list instead.
                 * 
                 * Registrations found:
                 */
                _lstSystemEventListener = new CopyOnWriteArrayList<SystemEventListener>();
            }

            return _lstSystemEventListener;
        }

        private synchronized List<SystemEventListener> getSpecificSourceListenersNotNull(
                Class<?> sourceClass)
        {
            if (_sourceClassMap == null)
            {
                _sourceClassMap = new ConcurrentHashMap<Class<?>, List<SystemEventListener>>();
            }

            List<SystemEventListener> list = _sourceClassMap.get(sourceClass);
            if (list == null)
            {
                /*
                 * TODO: Check if modification occurs often or not, might have to use a synchronized list instead.
                 * 
                 * Registrations found:
                 */
                list = new CopyOnWriteArrayList<SystemEventListener>();
                _sourceClassMap.put(sourceClass, list);
            }

            return list;
        }
    }

    // ------------------------------------------------------ Instance Variables

    private static final Log log = LogFactory.getLog(MockApplication20.class);

    private final Map<Class<? extends SystemEvent>, SystemListenerEntry> _systemEventListenerClassMap = new ConcurrentHashMap<Class<? extends SystemEvent>, SystemListenerEntry>();

    private Map<String, String> _defaultValidatorsIds = new HashMap<String, String>();

    private ProjectStage _projectStage;

    private final Map<String, Class<?>> _behaviorClassMap = new ConcurrentHashMap<String, Class<?>>();

    private final Map<String, Class<?>> _validatorClassMap = new ConcurrentHashMap<String, Class<?>>();

    private ResourceHandler _resourceHandler;

    // ----------------------------------------------------- Mock Object Methods

    public Map<String, String> getDefaultValidatorInfo()
    {
        return Collections.unmodifiableMap(_defaultValidatorsIds);
    }

    private static SystemEvent _traverseListenerList(
            List<? extends SystemEventListener> listeners,
            Class<? extends SystemEvent> systemEventClass, Object source,
            SystemEvent event)
    {
        if (listeners != null && !listeners.isEmpty())
        {
            for (SystemEventListener listener : listeners)
            {
                // Call SystemEventListener.isListenerForSource(java.lang.Object), passing the source argument.
                // If this returns false, take no action on the listener.
                if (listener.isListenerForSource(source))
                {
                    // Otherwise, if the event to be passed to the listener instances has not yet been constructed,
                    // construct the event, passing source as the argument to the one-argument constructor that takes
                    // an Object. This same event instance must be passed to all listener instances.
                    event = _createEvent(systemEventClass, source, event);

                    // Call SystemEvent.isAppropriateListener(javax.faces.event.FacesListener), passing the listener
                    // instance as the argument. If this returns false, take no action on the listener.
                    if (event.isAppropriateListener(listener))
                    {
                        // Call SystemEvent.processListener(javax.faces.event.FacesListener), passing the listener
                        // instance.
                        event.processListener(listener);
                    }
                }
            }
        }

        return event;
    }

    private static SystemEvent _createEvent(
            Class<? extends SystemEvent> systemEventClass, Object source,
            SystemEvent event)
    {
        if (event == null)
        {
            try
            {
                Constructor<? extends SystemEvent> constructor = systemEventClass
                        .getConstructor(Object.class);
                event = constructor.newInstance(source);
            }
            catch (Exception e)
            {
                throw new FacesException(
                        "Couldn't instanciate system event of type "
                                + systemEventClass.getName(), e);
            }
        }

        return event;
    }

    private void checkNull(final Object param, final String paramName)
    {
        if (param == null)
        {
            throw new NullPointerException(paramName + " cannot be null.");
        }
    }

    private void checkEmpty(final String param, final String paramName)
    {
        if (param.length() == 0)
        {
            throw new NullPointerException("String " + paramName
                    + " cannot be empty.");
        }
    }

    public void publishEvent(FacesContext facesContext,
            Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceBaseType, Object source)
    {
        checkNull(systemEventClass, "systemEventClass");
        checkNull(source, "source");

        try
        {
            SystemEvent event = null;
            if (source instanceof SystemEventListenerHolder)
            {
                SystemEventListenerHolder holder = (SystemEventListenerHolder) source;

                // If the source argument implements SystemEventListenerHolder, call 
                // SystemEventListenerHolder.getListenersForEventClass(java.lang.Class) on it, passing the systemEventClass 
                // argument. If the list is not empty, perform algorithm traverseListenerList on the list.
                event = _traverseListenerList(holder
                        .getListenersForEventClass(systemEventClass),
                        systemEventClass, source, event);
            }

            SystemListenerEntry systemListenerEntry = _systemEventListenerClassMap
                    .get(systemEventClass);
            if (systemListenerEntry != null)
            {
                systemListenerEntry.publish(systemEventClass, sourceBaseType,
                        source, event);
            }
        }
        catch (AbortProcessingException e)
        {
            // If the act of invoking the processListener method causes an AbortProcessingException to be thrown, 
            // processing of the listeners must be aborted, no further processing of the listeners for this event must 
            // take place, and the exception must be logged with Level.SEVERE.
            log.error("Event processing was aborted", e);
        }
    }

    public void publishEvent(FacesContext facesContext,
            Class<? extends SystemEvent> systemEventClass, Object source)
    {
        publishEvent(facesContext, systemEventClass, source.getClass(), source);
    }

    public ProjectStage getProjectStage()
    {
        // If the value has already been determined by a previous call to this
        // method, simply return that value.
        if (_projectStage == null)
        {

            FacesContext context = FacesContext.getCurrentInstance();
            String stageName = context.getExternalContext().getInitParameter(
                    ProjectStage.PROJECT_STAGE_PARAM_NAME);

            // If a value is found found
            if (stageName != null)
            {
                /*
                 * see if an enum constant can be obtained by calling ProjectStage.valueOf(), passing the value from the
                 * initParamMap. If this succeeds without exception, save the value and return it.
                 */
                try
                {
                    _projectStage = ProjectStage.valueOf(stageName);
                    return _projectStage;
                }
                catch (IllegalArgumentException e)
                {
                    //log.log(Level.SEVERE, "Couldn't discover the current project stage", e);
                }
            }

            _projectStage = ProjectStage.Production;
        }

        return _projectStage;
    }

    public void addBehavior(String behaviorId, String behaviorClass)
    {
        checkNull(behaviorId, "behaviorId");
        checkEmpty(behaviorId, "behaviorId");
        checkNull(behaviorClass, "behaviorClass");
        checkEmpty(behaviorClass, "behaviorClass");

        try
        {
            _behaviorClassMap.put(behaviorId, Class.forName(behaviorClass));
        }
        catch (ClassNotFoundException ignore)
        {

        }

    }

    public Iterator<String> getBehaviorIds()
    {
        return _behaviorClassMap.keySet().iterator();
    }

    public Behavior createBehavior(String behaviorId) throws FacesException
    {
        checkNull(behaviorId, "behaviorId");
        checkEmpty(behaviorId, "behaviorId");

        final Class<?> behaviorClass = this._behaviorClassMap.get(behaviorId);
        if (behaviorClass == null)
        {
            throw new FacesException(
                    "Could not find any registered behavior-class for behaviorId : "
                            + behaviorId);
        }

        try
        {
            final Behavior behavior = (Behavior) behaviorClass.newInstance();
            return behavior;
        }
        catch (Exception e)
        {
            throw new FacesException("Could not instantiate behavior: "
                    + behaviorClass, e);
        }
    }

    @Override
    public void addValidator(String validatorId, String validatorClass)
    {
        super.addValidator(validatorId, validatorClass);

        try
        {
            _validatorClassMap.put(validatorId, Class.forName(validatorClass));
        }
        catch (ClassNotFoundException ex)
        {
            throw new FacesException(ex.getMessage());
        }

    }

    public void addDefaultValidatorId(String validatorId)
    {
        if (_validatorClassMap.containsKey(validatorId))
        {
            _defaultValidatorsIds.put(validatorId, _validatorClassMap.get(
                    validatorId).getName());
        }
    }

    public final ResourceHandler getResourceHandler()
    {
        return _resourceHandler;
    }

    public final void setResourceHandler(ResourceHandler resourceHandler)
    {
        checkNull(resourceHandler, "resourceHandler");

        _resourceHandler = resourceHandler;
    }

    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
            SystemEventListener listener)
    {
        subscribeToEvent(systemEventClass, null, listener);
    }

    public void subscribeToEvent(Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceClass, SystemEventListener listener)
    {
        checkNull(systemEventClass, "systemEventClass");
        checkNull(listener, "listener");

        SystemListenerEntry systemListenerEntry;
        synchronized (_systemEventListenerClassMap)
        {
            systemListenerEntry = _systemEventListenerClassMap
                    .get(systemEventClass);
            if (systemListenerEntry == null)
            {
                systemListenerEntry = new SystemListenerEntry();
                _systemEventListenerClassMap.put(systemEventClass,
                        systemListenerEntry);
            }
        }

        systemListenerEntry.addListener(listener, sourceClass);
    }

    public void unsubscribeFromEvent(
            Class<? extends SystemEvent> systemEventClass,
            SystemEventListener listener)
    {
        unsubscribeFromEvent(systemEventClass, null, listener);
    }

    public void unsubscribeFromEvent(
            Class<? extends SystemEvent> systemEventClass,
            Class<?> sourceClass, SystemEventListener listener)
    {
        checkNull(systemEventClass, "systemEventClass");
        checkNull(listener, "listener");

        SystemListenerEntry systemListenerEntry = _systemEventListenerClassMap
                .get(systemEventClass);
        if (systemListenerEntry != null)
        {
            systemListenerEntry.removeListener(listener, sourceClass);
        }
    }
}
