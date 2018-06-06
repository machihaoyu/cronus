package com.fjs.cronus.service.allocatecustomer.v2;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayAllocateData implements Delayed {

    private String phone;

    /**
     * 延迟的时间，注意是时间点的时间.
     */
    private long delaytime;

    public DelayAllocateData(String phone, long delaytime) {
        this.phone = phone;
        this.delaytime = delaytime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delaytime - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        long l = getDelay(TimeUnit.NANOSECONDS) - o.getDelay(TimeUnit.NANOSECONDS);
        return l == 0 ? 0 : (l > 0 ? 1 : -1);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(long delaytime) {
        this.delaytime = delaytime;
    }
}
