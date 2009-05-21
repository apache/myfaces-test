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

import java.io.IOException;

import javax.faces.el.ValueBinding;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.shale.test.base.AbstractJsfTestCase;
import org.apache.shale.test.mock.data.Bean;

public class ValueBindingTest extends AbstractJsfTestCase
{

  public ValueBindingTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public static Test suite() {
    return (new TestSuite(ValueBindingTest.class));
  }
  
  public void testRenderButtonWithValueBinding() throws IOException {
    ValueBinding vb= facesContext.getApplication().
                     createValueBinding("#{ bean.name }");

    Bean bean = new Bean();
    bean.setName("Matthias");
    
    facesContext.getExternalContext().getRequestMap().put("bean",
        bean);
    
    assertEquals("Matthias", vb.getValue(facesContext).toString());
    
    vb = facesContext.getApplication().createValueBinding("#{requestScope['bean']}");
    
    Bean copy = (Bean) vb.getValue(facesContext);
    assertNotNull(copy);
    assertTrue(bean == copy);

    facesContext.getExternalContext().getRequestMap().put("org.apache.shale.bean",
            bean);

    vb = facesContext.getApplication().createValueBinding("#{requestScope['org.apache.shale.bean']}");
    
    copy = (Bean) vb.getValue(facesContext);
    assertNotNull(copy);
    assertTrue(bean == copy);


  }

}
