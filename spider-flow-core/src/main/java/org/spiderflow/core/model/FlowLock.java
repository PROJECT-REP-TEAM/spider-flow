package org.spiderflow.core.model;

import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sp_lock")
public class FlowLock {
    private String lockName;

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }
}
