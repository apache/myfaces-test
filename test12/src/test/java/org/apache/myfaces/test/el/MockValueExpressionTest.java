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
package org.apache.myfaces.test.el;

import javax.el.ELContext;
import javax.el.ValueExpression;

import org.apache.myfaces.test.base.AbstractJsfTestCase;

/**
 * Test class for ValueExpressions
 * 
 * @author Jakob Korherr (latest modification by $Author$)
 * @author Christoph Gšldner
 * @version $Revision$ $Date$
 */
public class MockValueExpressionTest extends AbstractJsfTestCase
{
    
    public MockValueExpressionTest(String name) 
    {
        super(name);
    }

    public void testSetValue() 
    {
        // set value of #{foo} to BAR via ValueExpression
        ELContext elContext = facesContext.getELContext();
        ValueExpression ve = application.getExpressionFactory()
                .createValueExpression(elContext, "#{foo}", String.class);
        ve.setValue(elContext, "BAR");
        assertEquals("BAR", externalContext.getRequestMap().get("foo"));
    }

    public void testGetValue() 
    {
        // set value of #{foo} to BAR in request scope
        externalContext.getRequestMap().put("foo", "BAR");
        // resolve value of #{foo} via ValueExpression
        ELContext elContext = facesContext.getELContext();
        ValueExpression ve = application.getExpressionFactory()
                .createValueExpression(elContext, "#{foo}", String.class);
        Object value = ve.getValue(elContext);
        assertEquals("BAR", value);
    }
    
}
