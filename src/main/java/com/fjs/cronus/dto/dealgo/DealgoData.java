package com.fjs.cronus.dto.dealgo;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */
public class DealgoData {

    private long elapsed;
    private List<Profiles> profiles;

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setProfiles(List<Profiles> profiles) {
        this.profiles = profiles;
    }

    public List<Profiles> getProfiles() {
        return profiles;
    }
}
