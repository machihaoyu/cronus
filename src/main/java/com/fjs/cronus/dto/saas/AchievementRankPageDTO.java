package com.fjs.cronus.dto.saas;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/24 0024.
 */
public class AchievementRankPageDTO implements Serializable {

    private static final long serialVersionUID = -7203877309487034645L;

    private List<AchievementRankDTO> dpage;
    private String p;
    private String total;
    private String perpage;

    public List<AchievementRankDTO> getDpage() {
        return dpage;
    }

    public void setDpage(List<AchievementRankDTO> dpage) {
        this.dpage = dpage;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPerpage() {
        return perpage;
    }

    public void setPerpage(String perpage) {
        this.perpage = perpage;
    }
}
