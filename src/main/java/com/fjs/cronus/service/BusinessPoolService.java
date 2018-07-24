package com.fjs.cronus.service;


import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonEnum;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.dto.BusinessPoolDTO;
import com.fjs.cronus.dto.MediaCustomerCountDTO;
import com.fjs.cronus.dto.MediaPriceDTO;
import com.fjs.cronus.dto.api.PHPLoginDto;
import com.fjs.cronus.dto.cronus.BaseUcDTO;
import com.fjs.cronus.dto.cronus.UcUserDTO;
import com.fjs.cronus.dto.loan.TheaApiDTO;
import com.fjs.cronus.dto.thea.BaseCommonDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.entity.CustomerPriceEntity;
import com.fjs.cronus.entity.MediaPriceEntity;
import com.fjs.cronus.dto.QueryResult;
import com.fjs.cronus.enums.AccountingMethodEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.CustomerInfoMapper;
import com.fjs.cronus.mappers.CustomerPriceMapper;
import com.fjs.cronus.mappers.MediaCustomerCountMapper;
import com.fjs.cronus.mappers.MediaPriceMapper;
import com.fjs.cronus.model.BusinessPool;
import com.fjs.cronus.model.CustomerInfo;
import com.fjs.cronus.service.client.TheaService;
import com.fjs.cronus.service.client.ThorService;
import com.fjs.cronus.service.thea.TheaClientService;
import com.fjs.cronus.service.uc.UcService;
import com.fjs.cronus.util.FastJsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BusinessPoolService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerInfoMapper customerInfoMapper;

    @Autowired
    private MediaCustomerCountMapper mediaCustomerCountMapper;

    @Autowired
    private MediaPriceMapper mediaPriceMapper;

    @Autowired
    ThorService thorService;

    @Autowired
    private CustomerPriceMapper customerPriceMapper;

    @Autowired
    private UcService ucService;

    @Autowired
    private TheaClientService theaClientService;

    @Autowired
    private PanService panService;

    @Autowired
    private TheaService theaService;

    /**
     * 商机池列表
     * @param nameOrTelephone
     * @param customerSource
     * @param utmSource
     * @param houseStatus
     * @param loanAmount
     * @param city
     * @param createTime
     * @param page
     * @param size
     * @return
     */
    public QueryResult<BusinessPoolDTO> businessPoolList(String nameOrTelephone, String customerSource, String utmSource, String houseStatus, String loanAmount, String city, String createTime,String createTimeEnd,Integer page, Integer size) {

        QueryResult<BusinessPoolDTO> queryResult = new QueryResult<>();
        HashMap<String, Object> map = new HashMap<>();
        if (null != nameOrTelephone && StringUtils.isNotEmpty(nameOrTelephone)){
            map.put("nameOrTelephone",nameOrTelephone);
        }
        if (null != customerSource && StringUtils.isNotEmpty(customerSource)){
            map.put("customerSource",customerSource);
        }
        if (null != utmSource && StringUtils.isNotEmpty(utmSource)){
            map.put("utmSource",utmSource);
        }
        if (null != houseStatus && StringUtils.isNotEmpty(houseStatus)){
            map.put("houseStatus",houseStatus);
        }
        if (null != loanAmount && StringUtils.isNotEmpty(loanAmount)){
            //loanAmount 的格式为0-20万, 或者500万(即500万以上)
            if (loanAmount.equals("500")){
                //500万以上
                map.put("loanAmount",loanAmount);
            }else {
                //0-20万的格式
                String[] loanAmounts = loanAmount.split("-");
                if (loanAmounts.length != 2){
                    throw new CronusException(CronusException.Type.LOAN_AMOUNT_FORMAT_ERROR,CronusException.Type.LOAN_AMOUNT_FORMAT_ERROR.getError());
                }
                List<String> loanAmountList = Arrays.asList(loanAmounts);
                map.put("loanAmountStart",loanAmountList.get(0));
                map.put("loanAmountEnd",loanAmountList.get(1));
            }

        }
        if (null != city && StringUtils.isNotEmpty(city)){
            map.put("city",city);
        }
        if (null != createTime && StringUtils.isNotEmpty(createTime) && null != createTimeEnd && StringUtils.isNotEmpty(createTimeEnd)){
            map.put("createTime",createTime + " 00:00:00");
            map.put("createTimeEnd",createTimeEnd + " 23:59:59");
        }
        map.put("page",(page - 1) * size);
        map.put("size",size);

        //根据条件查询客户
        Integer count = customerInfoMapper.businessPoolListCount(map);
        List<BusinessPool> businessPoolDTOList = customerInfoMapper.businessPoolList(map);
        ArrayList<BusinessPoolDTO> list = new ArrayList<>();
        if (null != businessPoolDTOList && businessPoolDTOList.size() > 0){
            for (BusinessPool businessPool : businessPoolDTOList){
                BusinessPoolDTO businessPoolDTO = new BusinessPoolDTO();
                BeanUtils.copyProperties(businessPool,businessPoolDTO);
                //如果该客户没有设置价格,就查询其媒体的价格
                Integer accountingMethod = businessPool.getAccountingMethod();
                if (null == accountingMethod){
                    //查询媒体定价
                    MediaPriceEntity mediaPriceEntity = mediaPriceMapper.getMediaPrice(businessPool.getMediaCustomerCountId());
                    if (null != mediaPriceEntity){
                        businessPoolDTO.setAccountingMethod(mediaPriceEntity.getAccountingMethod());
                        businessPoolDTO.setPrepurchasePrice(mediaPriceEntity.getPrepurchasePrice());
                        businessPoolDTO.setCommissionRate(mediaPriceEntity.getCommissionRate());
                        businessPoolDTO.setLoanRate(mediaPriceEntity.getLoanRate());
                    }
                }
                list.add(businessPoolDTO);
            }
        }
        queryResult.setTotal(count.toString());
        queryResult.setRows(list);
        return queryResult;
    }


    /**
     * 媒体列表
     * @param utmSource
     * @param page
     * @param size
     * @return
     */
    public QueryResult<MediaCustomerCountDTO> utmSourceList(String utmSource, Integer page, Integer size) {

        QueryResult<MediaCustomerCountDTO> queryResult = new QueryResult<>();
        HashMap<String, Object> map = new HashMap<>();
        if (null != utmSource && StringUtils.isNotEmpty(utmSource)){
            map.put("utmSource",utmSource);
        }
        map.put("page",(page - 1) * size);
        map.put("size",size);
        //查询数据
        Integer count = mediaCustomerCountMapper.utmSourceListCount(map);
        List<MediaCustomerCountDTO> mediaCustomerCountDTOList = mediaCustomerCountMapper.utmSourceList(map);

        queryResult.setTotal(count.toString());
        queryResult.setRows(mediaCustomerCountDTOList);

        return queryResult;
    }


    /**
     * 修改媒体价格
     * @param token
     * @param mediaPriceDTO
     */
    @Transactional
    public void editUtmSourcePrice(String token, MediaPriceDTO mediaPriceDTO) {

        //校验参数
        checkData(mediaPriceDTO);

        //获取当前用户的id
        Integer currentUserId = getCurrentUserId(token);
        //将之前的定价都关闭 : is_close=1
        if (null == mediaPriceDTO.getId()){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR,CronusException.Type.CRM_PARAMS_ERROR.getError());
        }
        mediaPriceMapper.updateIsClose(mediaPriceDTO.getId());
        //新增修改后的媒体价格
        MediaPriceEntity mediaPriceEntity = new MediaPriceEntity();
        mediaPriceEntity.setMediaCustomerCountId(mediaPriceDTO.getId());
        mediaPriceEntity.setAccountingMethod(mediaPriceDTO.getAccountingMethod());
        mediaPriceEntity.setPrepurchasePrice(mediaPriceDTO.getPrepurchasePrice());
        mediaPriceEntity.setCommissionRate(mediaPriceDTO.getCommissionRate());
        mediaPriceEntity.setLoanRate(mediaPriceDTO.getLoanRate());
        mediaPriceEntity.setCreateUser(currentUserId);
        mediaPriceEntity.setLastUpdateUser(currentUserId);
        Integer count = mediaPriceMapper.addMediaPrice(mediaPriceEntity);
        if (count < 1){
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
        }

    }

    /**
     * 校验修改媒体或者客户价格
     * @param mediaPriceDTO
     */
    public void checkData(MediaPriceDTO mediaPriceDTO){

        if (mediaPriceDTO.getAccountingMethod() == null || AccountingMethodEnum.findByCode(mediaPriceDTO.getAccountingMethod()) == null){
            throw new CronusException(CronusException.Type.ACCOUNTING_METHOD_ERROR,CronusException.Type.ACCOUNTING_METHOD_ERROR.getError());
        }

        switch (mediaPriceDTO.getAccountingMethod()){
            case 1:
                //当预购单价的时候, 只有预购单价有值
                if (mediaPriceDTO.getPrepurchasePrice() == null || mediaPriceDTO.getCommissionRate() != null || mediaPriceDTO.getLoanRate() != null){
                    throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());
                }
                break;
            case 2:
                if (mediaPriceDTO.getPrepurchasePrice() != null || mediaPriceDTO.getCommissionRate() == null || mediaPriceDTO.getLoanRate() != null){
                    throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());
                }
                break;
            case 3:
                if (mediaPriceDTO.getPrepurchasePrice() != null || mediaPriceDTO.getCommissionRate() != null || mediaPriceDTO.getLoanRate() == null){
                    throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());
                }
                break;
            case 4:
                if (mediaPriceDTO.getPrepurchasePrice() == null || mediaPriceDTO.getCommissionRate() == null || mediaPriceDTO.getLoanRate() != null){
                    throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());
                }
                break;
            case 5:
                if (mediaPriceDTO.getPrepurchasePrice() == null || mediaPriceDTO.getCommissionRate() != null || mediaPriceDTO.getLoanRate() == null){
                    throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());
                }
                break;
            default:
                throw new CronusException(CronusException.Type.ACCOUNTING_PRICE_ERROR,CronusException.Type.ACCOUNTING_PRICE_ERROR.getError());

        }
    }


    /**
     * 修改客户价格
     * @param token
     * @param mediaPriceDTO
     */
    @Transactional
    public void editCustomerPrice(String token, MediaPriceDTO mediaPriceDTO) {

        //校验参数
        checkData(mediaPriceDTO);

        //获取当前用户的id
        Integer currentUserId = getCurrentUserId(token);
        //将之前的定价都关闭 : is_close=1
        if (null == mediaPriceDTO.getId()){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR,CronusException.Type.CRM_PARAMS_ERROR.getError());
        }
        customerPriceMapper.updateIsClose(mediaPriceDTO.getId());

        //新增修改后的客户价格
        CustomerPriceEntity customerPriceEntity = new CustomerPriceEntity();
        customerPriceEntity.setCustomerInfoId(mediaPriceDTO.getId());
        customerPriceEntity.setAccountingMethod(mediaPriceDTO.getAccountingMethod());
        customerPriceEntity.setPrepurchasePrice(mediaPriceDTO.getPrepurchasePrice());
        customerPriceEntity.setCommissionRate(mediaPriceDTO.getCommissionRate());
        customerPriceEntity.setLoanRate(mediaPriceDTO.getLoanRate());
        customerPriceEntity.setCreateUser(currentUserId);
        customerPriceEntity.setLastUpdateUser(currentUserId);
        customerPriceEntity.setIsClose(CommonEnum.NO.getCode());

        Integer count = customerPriceMapper.addCustomerPrice(customerPriceEntity);
        if (count < 1){
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
        }

    }


    /**
     * 通过token获取当前用户的id
     * @param token
     * @return
     */
    public Integer getCurrentUserId(String token){

        //根据token查询当前用户id
        String result = thorService.getCurrentUserInfo(token,null);
        BaseUcDTO dto = FastJsonUtils.getSingleBean(result,BaseUcDTO.class);
        UcUserDTO userDTO = FastJsonUtils.getSingleBean(dto.getData().toString(),UcUserDTO.class);
        if (null == userDTO){
            throw new CronusException(CronusException.Type.THEA_SYSTEM_ERROR,CronusException.Type.THEA_SYSTEM_ERROR.getError());
        }
        Integer userId = Integer.valueOf(userDTO.getUser_id());
        return userId;
    }


    /**
     * 领取客户
     * @param token
     * @param jsonObject
     */
    @Transactional
    public void receiveCustomerInPool(String token, JSONObject jsonObject) {

        //校验参数
        if (!jsonObject.containsKey("customerId")){
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR,CronusException.Type.CRM_PARAMS_ERROR.getError());
        }
        Integer customerId = jsonObject.getInteger("customerId");
        // 查询该客户是否存在
        CustomerInfo customer = customerInfoMapper.getCustomer(customerId);
        if (null == customer){
            throw new CronusException(CronusException.Type.CRM_CUSTOMEINFO_ERROR,CronusException.Type.CRM_CUSTOMEINFO_ERROR.getError());
        }
        //校验该客户是否已经被领取
        if (customer.getOwnUserId() != -1){
            throw new CronusException(CronusException.Type.CUSTOMER_ALREADY_RECEIVE,CronusException.Type.CUSTOMER_ALREADY_RECEIVE.getError());
        }
        //校验该客户是否有价格(客户价格或者媒体的价格), 没有设置价格的不能领取
        CustomerPriceEntity customerPriceEntity = customerPriceMapper.getCustomerPriceByCustomerId(customerId);
        MediaCustomerCountDTO mediaCustomerCountDTO = mediaCustomerCountMapper.getCustomerPrice(customer.getMediaCustomerCountId());

        if (null == customerPriceEntity){
            if (null == mediaCustomerCountDTO || mediaCustomerCountDTO.getAccountingMethod() == null){
                //该客户没有价格
                throw new CronusException(CronusException.Type.CUSTOMER_NOT_RECEIVE,CronusException.Type.CUSTOMER_NOT_RECEIVE.getError());
            }
        }

        //通过token获取当前登录人信息
        PHPLoginDto resultDto = ucService.getAllUserInfo(token, CommonConst.SYSTEMNAME);
        String[] authority = resultDto.getAuthority();
        if (authority.length > 0) {
            List<String> authList = Arrays.asList(authority);
            if (authList.contains(CommonConst.PULL_LOAN_URL)) {
                throw new CronusException(CronusException.Type.CRM_DATAAUTH_ERROR,CronusException.Type.CRM_DATAAUTH_ERROR.getError());
            }
        }
        UserInfoDTO userInfoDTO = resultDto.getUser_info();
        CustomerInfo customerInfo = null;
        //获取登录人id
        Integer userId = null;
        if (StringUtils.isNotEmpty(userInfoDTO.getUser_id())) {
            userId = Integer.parseInt(userInfoDTO.getUser_id());
        }
        //领取客户
        String maxCount = theaClientService.findValueByName(token, CommonConst.CANPUUMAXCOUNT);
        Integer count = panService.keepCount(userId);
        if (count >= Integer.parseInt(maxCount)){
            throw new CronusException(CronusException.Type.MESSAGE_PULLCUSTOMERCOUNT_ERROR,CronusException.Type.MESSAGE_PULLCUSTOMERCOUNT_ERROR.getError());
        }
        boolean updateResult = panService.pullPan(customerId, userId, token, userInfoDTO.getName());
        if (updateResult == false) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
        }

        //领取客户成功之后, 更新customer_price表(是否领取和领取时间)
        if (null == customerPriceEntity){
            //领取表中无记录, 需要新增记录
            CustomerPriceEntity customerPrice = new CustomerPriceEntity();
            customerPrice.setCustomerInfoId(customerId);
            customerPrice.setAccountingMethod(mediaCustomerCountDTO.getAccountingMethod());
            customerPrice.setPrepurchasePrice(mediaCustomerCountDTO.getPrepurchasePrice());
            customerPrice.setCommissionRate(mediaCustomerCountDTO.getCommissionRate());
            customerPrice.setLoanRate(mediaCustomerCountDTO.getLoanRate());
            customerPrice.setCreateUser(userId);
            customerPrice.setLastUpdateUser(userId);
            customerPrice.setIsReceive(CommonEnum.YES.getCode());
            customerPrice.setGmtReceive(new Date());
            customerPrice.setIsClose(CommonEnum.YES.getCode());
            count = customerPriceMapper.addCustomerPrice(customerPrice);
            if (count < 1){
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
            }
        }else {
            //更新时间
            count = customerPriceMapper.receiveSuccess(customerId);
            if (count < 1){
                throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
            }
        }

        //更新media_customer_count的已购买量
        count = mediaCustomerCountMapper.updatePurchasedNumber(customer.getMediaCustomerCountId());
        if (count < 1){
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR,CronusException.Type.CRM_OTHER_ERROR.getError());
        }

    }


    /**
     * 商机池所有客户来源
     * @return
     */
    public List<String> getCustomerSourceList(String token) {
        ArrayList<String> customerSourceList = new ArrayList<>();
        //查询客户来源, 当做搜索条件
        TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllSource(token);
        if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
        }
        List<BaseCommonDTO> baseCommonDTOList = allMedia.getData();
        if (null != baseCommonDTOList && baseCommonDTOList.size() > 0){
            for (BaseCommonDTO baseCommonDTO : baseCommonDTOList){
                customerSourceList.add(baseCommonDTO.getName());
            }
        }
        return customerSourceList;
    }


    /**
     * 商机池所有媒体
     * @return
     */
    public List<String> getAllUtmSourceList(String token) {

        ArrayList<String> utmSourceList = new ArrayList<>();
        //查询客户媒体, 当做搜索条件
        TheaApiDTO<List<BaseCommonDTO>> allMedia = theaService.getAllMedia(token);
        if (!CommonMessage.SUCCESS.getCode().equals(allMedia.getResult())) {
            throw new CronusException(CronusException.Type.CRM_OTHER_ERROR, allMedia.getMessage());
        }
        List<BaseCommonDTO> baseCommonDTOList = allMedia.getData();
        if (null != baseCommonDTOList && baseCommonDTOList.size() > 0){
            for (BaseCommonDTO baseCommonDTO : baseCommonDTOList){
                utmSourceList.add(baseCommonDTO.getName());
            }
        }

        return utmSourceList;
    }
}
