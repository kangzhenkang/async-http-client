/*
 * Copyright (c) 2014 AsyncHttpClient Project. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.ning.http.client.providers.netty.timeout;

import static com.ning.http.util.DateUtils.millisTime;

import org.jboss.netty.util.Timeout;

import com.ning.http.client.providers.netty.NettyAsyncHttpProvider;
import com.ning.http.client.providers.netty.NettyResponseFuture;

public class RequestTimeoutTimerTask extends TimeoutTimerTask {

    private final long requestTimeout;
    
    public RequestTimeoutTimerTask(NettyResponseFuture<?> nettyResponseFuture, NettyAsyncHttpProvider provider, TimeoutsHolder timeoutsHolder, long requestTimeout) {
        super(nettyResponseFuture, provider, timeoutsHolder);
        this.requestTimeout = requestTimeout;
    }

    public void run(Timeout timeout) throws Exception {

        // in any case, cancel possible idleConnectionTimeout
        timeoutsHolder.cancel();

        if (provider.isClose() || nettyResponseFuture.isDone()) {
            return;
        }

        String message = "Request timed out to " + nettyResponseFuture.getChannelRemoteAddress() + " of " + requestTimeout + " ms";
        long age = millisTime() - nettyResponseFuture.getStart();
        expire(message, age);
    }
}
