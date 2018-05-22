package com.fjs.cronus.service;

import com.fjs.cronus.Common.CommonRedisConst;
import com.fjs.cronus.dto.dealgo.DealgoData;
import com.fjs.cronus.dto.dealgo.Profile;
import com.fjs.cronus.dto.dealgo.Profiles;
import com.fjs.cronus.mappers.DealgoMapper;
import com.fjs.cronus.model.DealgoProfile;
import com.fjs.cronus.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/5/17.
 */
@Service
public class DealgoService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${dealgo.profileUrl}")
    private String profileUrl;

    @Autowired
    RestTemplate restTemplate;

    @Resource
    RedisTemplate<String,String> redisConfigTemplete;


    /*
    * 定时获取dealgo数据
    * */
    public void initProfileTask() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--initProfileTask");
            Date date = new Date();
            Integer hour = DateUtils.getHour(date);
            ValueOperations<String, String> redis = redisConfigTemplete.opsForValue();
            String done = redis.get("initProfileTask");
            if (StringUtils.isNoneEmpty(done) && done.equals("1")) {
                stringBuilder.append("--done");
                return;
            } else {
                if ( 11< hour && hour < 12) {
                    stringBuilder.append("--exe time");
                    new Thread(() -> {
                        initProfile();
                    }).run();
                }
                else {
                    stringBuilder.append("-- not exe time");
                }

            }
            logger.info(stringBuilder.toString());
        }catch (Exception e)
        {
            logger.error("initProfileTask",e);
        }
    }

    public void initProfile()
    {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("--start");
            Date date;
            DealgoProfile dealgoProfile = getLatestDealgoProfile();
            if (dealgoProfile!=null && dealgoProfile.getDate()!=null) {
                date = dealgoProfile.getDate();
            }
            else {
                date = DateUtils.addDay(new Date(),-2);
            }
            stringBuilder.append("--");
            stringBuilder.append(date.toString());
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
//            String url = "http://api.rcrai.com/fangjs/customer/profile?start=" + getFetchDate(date);
            String url = profileUrl+"?start=" + getFetchDate(date);

            if (DateUtils.getTodayStartTime().compareTo(date) == 1)
            {
                stringBuilder.append("--get dealgo data");
                DealgoData dealgoData = restTemplate.getForObject(url, DealgoData.class);
                batchInsert(dealgoData, date);
            }
            stringBuilder.append("--end");
            logger.info(stringBuilder.toString());
            ValueOperations<String, String> redis = redisConfigTemplete.opsForValue();
            redis.set("initProfileTask","1",60, TimeUnit.MINUTES);
        }catch (Exception e)
        {
            logger.error("initProfile",e);
        }
    }

    private String getFetchDate(Date date)
    {
        return DateUtils.format(DateUtils.addDay(date,1),DateUtils.FORMAT_SHORT2);
    }

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private DealgoMapper dealgoMapper;

    public void batchInsert(DealgoData dealgoData,Date date){
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        try {
            int i=0;
            for (Profiles fs : dealgoData.getProfiles()) {
                for (Profile profile:
                        fs.getProfile()) {
                    DealgoProfile dealgoProfile = new DealgoProfile();
                    BeanUtils.copyProperties(profile,dealgoProfile);
                    dealgoProfile.setDealid(profile.getId());
                    dealgoProfile.setNumber(fs.getNumber());
                    dealgoProfile.setUniqueId(fs.getUniqueId());
                    dealgoProfile.setDate(date);
                    dealgoMapper.insertDealgoProfile(dealgoProfile);
                }
                if (i % 500 == 0 || i == dealgoData.getProfiles().size()-1) {
                    session.commit();
                    session.clearCache();
                }
                i++;
            }
        }catch (Exception e) {
            //没有提交的数据可以回滚
            session.rollback();
        } finally{
            session.close();
        }
    }


    public List<DealgoProfile> getDealgoData(String telephone, String name) {
        return dealgoMapper.getDealgoProfiles(telephone,name);
    }

    public DealgoProfile getLatestDealgoProfile()
    {
        return dealgoMapper.getLatestDealgoProfile();
    }

}
