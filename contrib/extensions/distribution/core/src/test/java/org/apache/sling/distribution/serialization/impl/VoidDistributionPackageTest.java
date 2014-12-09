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
package org.apache.sling.distribution.serialization.impl;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.sling.distribution.communication.DistributionRequest;
import org.apache.sling.distribution.communication.DistributionRequestType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testcase for {@link VoidDistributionPackage}
 */
public class VoidDistributionPackageTest {

    @Test
    public void testCreatedAndReadPackagesEquality() throws Exception {
        DistributionRequest request = new DistributionRequest(DistributionRequestType.DELETE, "/abc");
        VoidDistributionPackage createdPackage = new VoidDistributionPackage(request);
        VoidDistributionPackage readPackage = VoidDistributionPackage.fromStream(new ByteArrayInputStream(("DELETE:/abc:VOID").getBytes()));
        assertEquals(createdPackage.getType(), readPackage.getType());
        assertEquals(createdPackage.getInfo().getRequestType(), readPackage.getInfo().getRequestType());
        assertEquals(Arrays.toString(createdPackage.getInfo().getPaths()), Arrays.toString(readPackage.getInfo().getPaths()));
        assertEquals(createdPackage.getId(), readPackage.getId());
        assertTrue(IOUtils.contentEquals(createdPackage.createInputStream(), readPackage.createInputStream()));
    }
}