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
package org.apache.myfaces.test.mock;

import java.util.Enumeration;

import javax.servlet.ServletRequest;

/**
 * ServletRequest parameters as Map.
 * 
 * @author Anton Koinov (latest modification by $Author: grantsmith $)
 * @version $Revision: 472618 $ $Date: 2006-11-08 15:06:54 -0500 (Mié, 08 Nov 2006) $
 */
class _RequestParameterMap extends _AbstractAttributeMap
{
    private final ServletRequest _servletRequest;

    _RequestParameterMap(ServletRequest servletRequest)
    {
        _servletRequest = servletRequest;
    }

    protected Object getAttribute(String key)
    {
        return _servletRequest.getParameter(key);
    }

    protected void setAttribute(String key, Object value)
    {
        if( _servletRequest instanceof MockHttpServletRequest)
        {
            ((MockHttpServletRequest)_servletRequest).addParameter(key, (String)value);
        }
        else
        {
            throw new UnsupportedOperationException(
                "Cannot set ServletRequest Parameter");
        }
    }

    protected void removeAttribute(String key)
    {
        throw new UnsupportedOperationException(
            "Cannot remove ServletRequest Parameter");
    }

    protected Enumeration getAttributeNames()
    {
        return _servletRequest.getParameterNames();
    }
}