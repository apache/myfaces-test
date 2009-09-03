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

package org.apache.shale.test.mock;

import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextFactory;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.Lifecycle;

public class MockFacesContext20 extends MockFacesContext12 {

    // ------------------------------------------------------------ Constructors

    private boolean _processingEvents = true;
    
    public MockFacesContext20() {
        super();
        setCurrentInstance(this);
    }


    public MockFacesContext20(ExternalContext externalContext) {
        super(externalContext);
    }


    public MockFacesContext20(ExternalContext externalContext, Lifecycle lifecycle) {
        super(externalContext, lifecycle);
    }

    // ----------------------------------------------------- Mock Object Methods

    // ------------------------------------------------------ Instance Variables

    // ----------------------------------------------------- Mock Object Methods

    private boolean postback;

    @Override
    public boolean isPostback()
    {
        return postback;
    }
    
    public void setPostback(boolean value)
    {
        postback = value;
    }

    private PhaseId _currentPhaseId = PhaseId.RESTORE_VIEW;
    
    public PhaseId getCurrentPhaseId()
    {
        return _currentPhaseId;
    }
    
    public void setCurrentPhaseId(PhaseId _currentPhaseId)
    {
        this._currentPhaseId = _currentPhaseId;
    }
    
    private Map<Object,Object> attributes;

    @Override
    public Map<Object, Object> getAttributes()
    {
        if (attributes == null)
        {
            attributes = new HashMap<Object,Object>();
        }
        return attributes;
    }
    
    private PartialViewContext _partialViewContext = null;
    
    @Override
    public PartialViewContext getPartialViewContext()
    {
        if (_partialViewContext == null)
        {
            //Get through factory finder
            PartialViewContextFactory factory = (PartialViewContextFactory)
                FactoryFinder.getFactory(FactoryFinder.PARTIAL_VIEW_CONTEXT_FACTORY);
            _partialViewContext = factory.getPartialViewContext(this);
        }
        return _partialViewContext;
    }
    
    public boolean isProcessingEvents()
    {
        return _processingEvents;
    }
    
    @Override
    public void setProcessingEvents(boolean processingEvents)
    {
        _processingEvents = processingEvents;
    }
    
    // ------------------------------------------------- ExternalContext Methods

}
