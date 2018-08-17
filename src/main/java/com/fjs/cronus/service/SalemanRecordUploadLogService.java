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
