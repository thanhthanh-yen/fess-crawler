/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.robot.client.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;
import org.seasar.extension.timer.TimeoutTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shinsuke
 * 
 */
public class HcConnectionMonitorTarget implements TimeoutTarget {
    private static final Logger logger = LoggerFactory // NOPMD
        .getLogger(HcConnectionMonitorTarget.class);

    private ClientConnectionManager clientConnectionManager;

    private long idleConnectionTimeout;

    public HcConnectionMonitorTarget(
            ClientConnectionManager clientConnectionManager,
            long idleConnectionTimeout) {
        this.clientConnectionManager = clientConnectionManager;
        this.idleConnectionTimeout = idleConnectionTimeout;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.extension.timer.TimeoutTarget#expired()
     */
    public void expired() {
        try {
            // Close expired connections
            clientConnectionManager.closeExpiredConnections();
            // Close idle connections
            clientConnectionManager.closeIdleConnections(
                idleConnectionTimeout,
                TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.warn("A connection monitoring exception occurs.", e);
        }
    }

}
