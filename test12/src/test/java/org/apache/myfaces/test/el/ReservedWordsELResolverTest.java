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
package org.apache.myfaces.test.el;

import org.apache.myfaces.test.base.AbstractJsfTestCase;

import javax.el.ELContext;

/**
 * @author Matt Benson
 */
public class ReservedWordsELResolverTest extends AbstractJsfTestCase {

    /**
     * <p>Construct a new instance of this test case.</p>
     *
     * @param name Name of this test case
     */
    public ReservedWordsELResolverTest(String name) {
        super(name);
    }

    public void testGetReservedWords() {
        ELContext elContext = facesContext.getELContext();
        assertEquals(Boolean.TRUE, application.getExpressionFactory()
                .createValueExpression(elContext, "#{true}", Boolean.class)
                .getValue(elContext));
        assertEquals(Boolean.FALSE, application.getExpressionFactory()
                .createValueExpression(elContext, "#{false}", Boolean.class)
                .getValue(elContext));
        assertNull(application.getExpressionFactory()
                .createValueExpression(elContext, "#{null}", Object.class)
                .getValue(elContext));
    }
}
