package com.fjs.cronus.mappers;

import com.fjs.cronus.model.DealgoProfile;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/5/17.
 */
public interface DealgoMapper {

    @Insert("insert dealgo_profile (number,dealid,bid,sid,cid,uniqueId,name,value,evidence,briefEvidence,mediumEvidence,score,deal_date) " +
            "values(#{number},#{dealid},#{bid},#{sid},#{cid},#{uniqueId},#{name},#{value},#{evidence},#{briefEvidence},#{mediumEvidence},#{score},#{date})")
    Integer insertDealgoProfile(DealgoProfile dealgoProfile);

    @Select("SELECT * from dealgo_profile d where d.number = #{number} and d.`name` = #{name} ORDER BY d.id DESC")
    @ResultMap({"dealgoProfile"})
    List<DealgoProfile> getDealgoProfiles(@Param("number") String telephone,@Param("name") String name);

    @Select("SELECT * from dealgo_profile d ORDER BY d.id DESC LIMIT 0,1")
    @Results(id = "dealgoProfile", value = {
            @Result(property = "date", column = "deal_date")
    })
    DealgoProfile getLatestDealgoProfile();

}
