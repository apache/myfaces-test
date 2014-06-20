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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.faces.application.StateManager.SerializedView;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;

/**
 * Mock class that encode view state in hex format
 * 
 * @since 1.0.0
 * @author Leonardo Uribe (latest modification by $Author: lu4242 $)
 * @version $Revision: 951803 $ $Date: 2010-06-05 21:13:20 -0500 (Sáb, 05 Jun 2010) $
 */
public class MockResponseStateManager extends ResponseStateManager
{
    // ------------------------------------------------------------ Constructors

    // ------------------------------------------------------ Instance Variables

    private static final int TREE_PARAM = 0;
    private static final int STATE_PARAM = 1;
    private static final int VIEWID_PARAM = 2;
    private static final String ZIP_CHARSET = "ISO-8859-1";

    // ----------------------------------------------------- Mock Object Methods

    // -------------------------------------------------- ResponseStateManager Methods

    public Object getState(FacesContext facesContext, String viewId)
    {
        Object[] savedState = getSavedState(facesContext);
        if (savedState == null)
        {
            return null;
        }

        return new Object[] { savedState[TREE_PARAM], savedState[STATE_PARAM] };
    }

    public Object getTreeStructureToRestore(FacesContext facesContext,
            String viewId)
    {
        // Although this method won't be called anymore,
        // it has been kept for backward compatibility.
        Object[] savedState = getSavedState(facesContext);
        if (savedState == null)
        {
            return null;
        }

        return savedState[TREE_PARAM];
    }

    public Object getComponentStateToRestore(FacesContext facesContext)
    {
        // Although this method won't be called anymore,
        // it has been kept for backward compatibility.
        Object[] savedState = getSavedState(facesContext);
        if (savedState == null)
        {
            return null;
        }

        return savedState[STATE_PARAM];
    }

    public boolean isPostback(FacesContext context)
    {
        return context.getExternalContext().getRequestParameterMap()
                .containsKey(ResponseStateManager.VIEW_STATE_PARAM);
    }

    public void writeState(FacesContext facescontext,
            SerializedView serializedview) throws IOException
    {
        ResponseWriter responseWriter = facescontext.getResponseWriter();

        Object[] savedState = new Object[3];

        if (facescontext.getApplication().getStateManager()
                .isSavingStateInClient(facescontext))
        {
            Object treeStruct = serializedview.getStructure();
            Object compStates = serializedview.getState();

            if (treeStruct != null)
            {
                savedState[TREE_PARAM] = treeStruct;
            }

            if (compStates != null)
            {
                savedState[STATE_PARAM] = compStates;
            }
        }
        else
        {
            // write viewSequence
            Object treeStruct = serializedview.getStructure();
            if (treeStruct != null)
            {
                if (treeStruct instanceof String)
                {
                    savedState[TREE_PARAM] = treeStruct;
                }
            }
        }

        savedState[VIEWID_PARAM] = facescontext.getViewRoot().getViewId();

        // write the view state field
        writeViewStateField(facescontext, responseWriter, savedState);

        // renderKitId field
        writeRenderKitIdField(facescontext, responseWriter);
    }

    private void writeViewStateField(FacesContext facesContext,
            ResponseWriter responseWriter, Object savedState)
            throws IOException
    {

        String serializedState = construct(facesContext, savedState);
        responseWriter.startElement("input", null);
        responseWriter.writeAttribute("type", "hidden", null);
        responseWriter.writeAttribute("name", VIEW_STATE_PARAM, null);
        responseWriter.writeAttribute("value", serializedState, null);
        responseWriter.endElement("input");
    }

    private void writeRenderKitIdField(FacesContext facesContext,
            ResponseWriter responseWriter) throws IOException
    {

        String defaultRenderKitId = facesContext.getApplication()
                .getDefaultRenderKitId();
        if (defaultRenderKitId != null
                && !RenderKitFactory.HTML_BASIC_RENDER_KIT
                        .equals(defaultRenderKitId))
        {
            responseWriter.startElement("input", null);
            responseWriter.writeAttribute("type", "hidden", null);
            responseWriter.writeAttribute("name",
                    ResponseStateManager.RENDER_KIT_ID_PARAM, null);
            responseWriter.writeAttribute("value", defaultRenderKitId, null);
            responseWriter.endElement("input");
        }
    }

    private String construct(FacesContext facesContext, Object savedState)
            throws IOException
    {
        byte[] bytes = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try
        {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(savedState);
            bytes = baos.toByteArray();
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                }
                finally
                {
                    oos = null;
                }
            }
            if (baos != null)
            {
                try
                {
                    baos.close();
                }
                catch (IOException e)
                {
                }
                finally
                {
                    baos = null;
                }
            }
        }
        return new String(new _Hex().encode(bytes), ZIP_CHARSET);
    }

    private Object reconstruct(FacesContext facesContext, String encodedState)
            throws IOException
    {
        byte[] bytes = encodedState.getBytes(ZIP_CHARSET);

        try
        {
            bytes = new _Hex().decode(bytes); 
        }
        catch(Exception e)
        {
            throw new IOException(e.getMessage());
        }

        ByteArrayInputStream input = null;
        ObjectInputStream s = null;
        Object object = null;

        try
        {
            input = new ByteArrayInputStream(bytes);
            s = new ObjectInputStream(input);
            object = s.readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new IOException(e.getMessage());
        }
        finally
        {
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (IOException e)
                {
                }
                finally
                {
                    s = null;
                }
            }
            if (input != null)
            {
                try
                {
                    input.close();
                }
                catch (IOException e)
                {
                }
                finally
                {
                    input = null;
                }
            }
        }
        return object;
    }

    private Object[] getSavedState(FacesContext facesContext)
    {
        Object encodedState = facesContext.getExternalContext()
                .getRequestParameterMap().get(VIEW_STATE_PARAM);
        if (encodedState == null || (((String) encodedState).length() == 0))
        {
            return null;
        }

        Object[] savedState = null;

        try
        {
            savedState = (Object[]) reconstruct(facesContext,
                    (String) encodedState);
        }
        catch (IOException e)
        {
            facesContext.getExternalContext().log(
                    "Cannot reconstruct view state", e);
        }

        if (savedState == null)
        {
            return null;
        }

        String restoredViewId = (String) savedState[VIEWID_PARAM];

        if (restoredViewId == null)
        {
            return null;
        }
        return savedState;
    }
}
