package com.fjs.cronus.service;

import com.fjs.cronus.entity.SalesmanCallData;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.SalemanRecordUploadLogMapper;
import com.fjs.cronus.mappers.SalesmanCallDataMapper;
import com.fjs.cronus.util.OssUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

@Service
public class SalemanRecordUploadLogService {

    @Autowired
    private SalemanRecordUploadLogMapper salemanRecordUploadLogMapper;

    @Autowired
    private SalesmanCallDataMapper salesmanCallDataMapper;

    /**
     * 上传业务员通话录音.
     */
    public String updateLoad(HttpServletRequest request, Long salesmanCallDataId, Long loginUid){

        // 数据校验
        SalesmanCallData record = new SalesmanCallData();
        record.setId(salesmanCallDataId);
        SalesmanCallData salesmanCallData = salesmanCallDataMapper.selectOne(record);
        if (salesmanCallData == null) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "根据 salesmanCallDataId，未找到通话记录");
        }
        if (StringUtils.isNotBlank(salesmanCallData.getRecordingUrl())) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "该通话记录，已上传语音，不能重复上传");
        }

        // 上传文件
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartRequest.getFileNames();
        if (!fileNames.hasNext()) throw new CronusException("上传文件丢失");

        MultipartFile file = multipartRequest.getFile(fileNames.next());
        String fileName = file.getOriginalFilename();

        // 数据校验：语音文件必须和通话记录数据相匹配（根据文件名，文件名规则为：顾客id_通话开始时间戳_业务员id）
        if (StringUtils.isBlank(fileName)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "语音文件名不能为空");
        }
        String[] split = fileName.split("_");
        if (split.length != 3) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "语音文件名格式不正确（约定格式为：顾客id_通话开始时间戳_业务员id）");
        }
        Long customerid = Long.valueOf(split[0]);
        Long startTime = Long.valueOf(split[1]);
        Long salesManId = Long.valueOf(split[2]);
        if (!salesmanCallData.getCustomerid().equals(customerid) || !salesmanCallData.getStartTime().equals(startTime) || !salesmanCallData.getSalesManId().equals(salesManId)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "语音文件错误，该语音文件不属于该通话记录");
        }

        // 上传
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = OssUtil.salemanRecordUpload(fileName, inputStream);
        if (StringUtils.isBlank(url)) {
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, "阿里oss系统异常，上传成功后，响应url为空");
        }

        // 将上传记录保存
        SalesmanCallData updateRecord = new SalesmanCallData();
        updateRecord.setRecordingUrl(url);
        updateRecord.setUpdated(new Date());
        updateRecord.setUpdateid(loginUid);

        Example example = new Example(SalesmanCallData.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", salesmanCallDataId);

        salesmanCallDataMapper.updateByExampleSelective(updateRecord, example);

        return url;
    }
}
