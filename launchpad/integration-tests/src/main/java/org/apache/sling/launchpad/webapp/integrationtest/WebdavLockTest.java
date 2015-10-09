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
package org.apache.sling.launchpad.webapp.integrationtest;

import junitx.framework.Assert;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.LockMethod;
import org.apache.jackrabbit.webdav.client.methods.PropFindMethod;
import org.apache.jackrabbit.webdav.client.methods.UnLockMethod;
import org.apache.jackrabbit.webdav.lock.LockInfo;
import org.apache.jackrabbit.webdav.lock.Scope;
import org.apache.jackrabbit.webdav.lock.Type;
import org.apache.sling.commons.testing.integration.HttpTestBase;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WebdavLockTest extends HttpTestBase{

    private final String testDir = "/sling-test/" + getClass().getSimpleName() + System.currentTimeMillis();
    private final String testDirUrl = HTTP_BASE_URL + testDir;

    private final String DEFAULT_HANDLER   = "default-lock-handler";
    private final String HANDLER_1   = "lock-handler-1";
    private final String HANDLER_2   = "lock-handler-2";

    private final String HANDLER_1_LCK = "locked-up-by-" + HANDLER_1;
    private final String HANDLER_2_LCK = "locked-up-by-" + HANDLER_2;

    private final String LOCK_NODE_DEFAULT   = "/lock-node-default";
    private final String LOCK_NODE_1   = "/lock-node-1";
    private final String LOCK_NODE_2   = "/lock-node-2";

    private final LockInfo lockInfo = new LockInfo(Scope.EXCLUSIVE, Type.WRITE, "admin", 0, false);


    @Override
    public void setUp() throws Exception {
        super.setUp();
        Map<String, String> map = new HashMap<String, String>();
        map.put("jcr:primaryType", "nt:unstructured");
        testClient.createNode(HTTP_BASE_URL + testDir + LOCK_NODE_DEFAULT, map);
        testClient.createNode(HTTP_BASE_URL + testDir + LOCK_NODE_1, map);
        testClient.createNode(HTTP_BASE_URL + testDir + LOCK_NODE_2, map);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testClient.delete(testDirUrl);
    }


    @Test
    public void testLock() throws DavException {

        try {
            LockMethod lockMethod = new LockMethod(HTTP_BASE_URL + testDir + LOCK_NODE_DEFAULT, lockInfo);
            testClient.executeDavMethod(lockMethod);

            GetMethod getMethod = new GetMethod(HTTP_BASE_URL + testDir + LOCK_NODE_DEFAULT + ".json");
            String nodeInfo = testClient.executeMethod(getMethod);

            UnLockMethod unlockMethod = new UnLockMethod(HTTP_BASE_URL + testDir + LOCK_NODE_DEFAULT, lockMethod.getLockToken());
            testClient.executeDavMethod(unlockMethod);

            getMethod = new GetMethod(HTTP_BASE_URL + testDir + LOCK_NODE_DEFAULT + ".json");
            nodeInfo = testClient.executeMethod(getMethod);

            //PropFindMethod propFindMethod = new PropFindMethod("http://localhost:8080/content/slingdemo/abc.txt");
            //ByteArrayOutputStream bout = new ByteArrayOutputStream();
            //propFindMethod.getRequestEntity().writeRequest(bout);

        } catch (IOException ex) {
            Assert.fail(ex);
        }
    }

}
