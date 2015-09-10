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

package org.apache.sling.jcr.webdav.impl.handler;

import org.apache.jackrabbit.server.io.LockHandler;
import org.apache.jackrabbit.server.io.LockHandlerManagerImpl;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

public class SlingLockHandlerManager extends LockHandlerManagerImpl {
    private static final LockHandler[] LOCKHANDLERS_PROTOTYPE = new LockHandler[0];

    private final SlingHandlerManager<LockHandler> handlerManager;

    public SlingLockHandlerManager(final String referenceName) {
        handlerManager = new SlingHandlerManager<LockHandler>(referenceName);
    }

    @Override
    public void addLockHandler(LockHandler propertyHandler) {
        throw new UnsupportedOperationException(
                "This LockHandlerManager only supports registered LockHandler services");
    }

    @Override
    public LockHandler[] getLockHandlers() {
        return this.handlerManager.getHandlers(LOCKHANDLERS_PROTOTYPE);
    }

    public void setComponentContext(ComponentContext componentContext) {
        this.handlerManager.setComponentContext(componentContext);
    }

    public void bindLockHandler(final ServiceReference lockHandlerReference) {
        this.handlerManager.bindHandler(lockHandlerReference);
    }

    public void unbindLockHandler(
            final ServiceReference lockHandlerReference) {
        this.handlerManager.unbindHandler(lockHandlerReference);
    }
}
