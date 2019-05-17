package org.noear.weed.cache;

import java.util.Date;

/**
 * Created by yuety on 14/11/7.
 */
class LocalCacheRecord {
    public Object data;
    public long timeout;

    public LocalCacheRecord(Object val, int seconds) {
        this.timeout = new Date().getTime() + seconds * 1000;
        this.data = val;
    }
}
