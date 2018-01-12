package com.fjs.cronus.service.thea;

import com.alibaba.fastjson.JSONObject;
import com.fjs.cronus.Common.DatumIntegrConstant;
import com.fjs.cronus.dto.thea.EarnCategoryInfoDTO;
import com.fjs.cronus.enums.CategoryEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DatumIntegrModelMapper;
import com.fjs.cronus.model.AttachmentModel;
import com.fjs.cronus.model.thea.DatumIntegrModelDTO;
import com.fjs.cronus.service.RContractDocumentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by chenjie on 2017/12/18.
 */
@Service
public class DatumIntegrModelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DatumIntegrModelMapper datumIntegrModelMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RContractDocumentService rContractDocumentService;

    /**
     * 判断用户是否存在身份证、房产证、户口本、收入证明、婚姻证明附件材料
     * @param customerId 客户id
     * @return
     */
    public DatumIntegrModelDTO judgeDatum(Long customerId) {
        DatumIntegrModelDTO datumIntegrModelDTO = new DatumIntegrModelDTO();
        //身份证
        Integer result = datumIntegrModelMapper.identity(customerId);
        if (result == null || result == 0){
            datumIntegrModelDTO.setIdentity(0);
        }else {
            datumIntegrModelDTO.setIdentity(1);
        }

        //房产证
        result = datumIntegrModelMapper.houseRegistration(customerId);
        if (result == null || result == 0) {
            datumIntegrModelDTO.setHouseRegistration(0);
        }else {
            datumIntegrModelDTO.setHouseRegistration(1);
        }

        //户口簿
        result = datumIntegrModelMapper.householdRegister(customerId);
        if (result == null || result == 0){
            datumIntegrModelDTO.setHouseholdRegister(0);
        }else {
            datumIntegrModelDTO.setHouseholdRegister(1);
        }

        //收入证明
        result = datumIntegrModelMapper.proofOfEarnings(customerId);
        if (result == null || result == 0) {
            datumIntegrModelDTO.setProofOfEarnings(0);
        }else {
            datumIntegrModelDTO.setProofOfEarnings(1);
        }

        //婚姻证明
        result = datumIntegrModelMapper.proofOfMarriage(customerId);
        if (result == null || result == 0) {
            datumIntegrModelDTO.setProofOfMarriage(0);
        }else {
            datumIntegrModelDTO.setProofOfMarriage(1);
        }

        return datumIntegrModelDTO;
    }

    /**
     * 获取身份证、户口簿、房产证、婚姻证明、放款凭证附件分类名称、id
     * @param type 要获取的附件类型
     * @param telephone 登录人手机号
     * @return
     */
    public List<AttachmentModel> getCategoryInfo(Integer type, String telephone) {
        CategoryEnum enumByCode = CategoryEnum.getEnumByCode(type);
        if (enumByCode == null)
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, CronusException.Type.CRM_PARAMS_ERROR.getError() + "||附件类型");

        List<AttachmentModel> list = null;
        String res;

        if (CategoryEnum.IDENTITY.getCode().equals(enumByCode.getCode())){
            //身份证
            res = redisGet(DatumIntegrConstant.REDIS_CLIENT_IDENTITY);
            if (StringUtils.isNotEmpty(res)){
                list = JSONObject.parseArray(res, AttachmentModel.class);
            }else {
                list = datumIntegrModelMapper.identityId();
                redisSet(DatumIntegrConstant.REDIS_CLIENT_IDENTITY, JSONObject.toJSONString(list));
            }
            picSet(list,telephone);
            return list;
        } else if (CategoryEnum.HOUSEHOLDREGISTER.getCode().equals(enumByCode.getCode())) {
            //户口簿
            res = redisGet(DatumIntegrConstant.REDIS_CLIENT_HOUSEHOLDREGISTER);
            if (StringUtils.isNotEmpty(res)){
                list = JSONObject.parseArray(res, AttachmentModel.class);
            }else {
                list = datumIntegrModelMapper.householdRegisterId();
                List<AttachmentModel> modelList = null;
                if (list != null){
                    modelList = new ArrayList<>();
                    for (AttachmentModel attachmentModel : list) {
                        copy(modelList, attachmentModel, DatumIntegrConstant.HOUSEHOLDREGISTER_AMOUNT);
                    }
                }
                list = modelList;
                redisSet(DatumIntegrConstant.REDIS_CLIENT_HOUSEHOLDREGISTER, JSONObject.toJSONString(list));
            }
            picSets(list,telephone,DatumIntegrConstant.HOUSEHOLDREGISTER_AMOUNT);
            return list;
        } else if (CategoryEnum.PROPERTYCERTIFICATE.getCode().equals(enumByCode.getCode())) {
            //房产证
            res = redisGet(DatumIntegrConstant.REDIS_CLIENT_PROPERTYCERTIFICATE);
            if (StringUtils.isNotEmpty(res)){
                list = JSONObject.parseArray(res, AttachmentModel.class);
            }else {
                list = datumIntegrModelMapper.houseRegistrationId();
                List<AttachmentModel> modelList = null;
                if (list != null){
                    modelList = new ArrayList<>();
                    for (AttachmentModel attachmentModel : list) {
                        copy(modelList, attachmentModel, DatumIntegrConstant.HOUSEREGISTRATION_AMOUNT);
                    }
                }
                list = modelList;
                redisSet(DatumIntegrConstant.REDIS_CLIENT_PROPERTYCERTIFICATE, JSONObject.toJSONString(list));
            }
            picSets(list,telephone,DatumIntegrConstant.HOUSEREGISTRATION_AMOUNT);
            return list;
        } else if (CategoryEnum.MARRIAGECERTIFICATE.getCode().equals(enumByCode.getCode())) {
            //结婚证
            res = redisGet(DatumIntegrConstant.REDIS_CLIENT_MARRIAGECERTIFICATE);
            if (StringUtils.isNotEmpty(res)){
                list = JSONObject.parseArray(res, AttachmentModel.class);
            }else {
                list = datumIntegrModelMapper.proofOfMarriageId();
                redisSet(DatumIntegrConstant.REDIS_CLIENT_MARRIAGECERTIFICATE, JSONObject.toJSONString(list));
            }
            picSet(list,telephone);
            return list;
        } else if (CategoryEnum.VOUCHER.getCode().equals(enumByCode.getCode())) {
            //放款凭证
            res = redisGet(DatumIntegrConstant.REDIS_CLIENT_VOUCHER);
            if (StringUtils.isNotEmpty(res)){
                list = JSONObject.parseArray(res, AttachmentModel.class);
            }else {
                list = datumIntegrModelMapper.voucherId();
                redisSet(DatumIntegrConstant.REDIS_CLIENT_VOUCHER, JSONObject.toJSONString(list));
            }
            picSet(list,telephone);
            return list;
        }
        return list;
    }


    /**
     *  复制对象
     * @param modelList
     * @param attachmentModel
     * @param size
     */
    private void copy(List<AttachmentModel> modelList, AttachmentModel attachmentModel, Integer size){
        modelList.add(attachmentModel);
        for (int i = 1; i < size; i++){
            AttachmentModel copy = AttachmentModel.copy(attachmentModel);
            modelList.add(copy);
        }
    }


    /**
     * 获取收入证明附件分类名称、id
     * @param telephone 登录人手机号
     * @return
     */
    public EarnCategoryInfoDTO getEarnCategoryInfo(String telephone) {
        //收入证明
        String proofOfEarningsIdStr = redisGet(DatumIntegrConstant.REDIS_CLIENT_PROOFOFEARNINGS);
        List<AttachmentModel> proofOfEarningsIds;
        if (StringUtils.isNotEmpty(proofOfEarningsIdStr)){
            proofOfEarningsIds = JSONObject.parseArray(proofOfEarningsIdStr, AttachmentModel.class);
        }else {
            proofOfEarningsIds = datumIntegrModelMapper.proofOfEarningsId();
            redisSet(DatumIntegrConstant.REDIS_CLIENT_PROOFOFEARNINGS, JSONObject.toJSONString(proofOfEarningsIds));
        }
        picSet(proofOfEarningsIds, telephone);

        //银行流水
        String bankStatementIdStr = redisGet(DatumIntegrConstant.REDIS_CLIENT_BANK_STATEMENT);
        List<AttachmentModel> bankStatementIds;
        if (StringUtils.isNotEmpty(bankStatementIdStr)){
            bankStatementIds = JSONObject.parseArray(bankStatementIdStr, AttachmentModel.class);
        }else {
            bankStatementIds = datumIntegrModelMapper.bankStatementId();
            List<AttachmentModel> modelList = null;
            if (bankStatementIds != null){
                modelList = new ArrayList<>();
                for (AttachmentModel attachmentModel : bankStatementIds){
                    copy(modelList, attachmentModel, DatumIntegrConstant.STATEMENT_AMOUNT);
                }
            }
            bankStatementIds = modelList;
            redisSet(DatumIntegrConstant.REDIS_CLIENT_BANK_STATEMENT, JSONObject.toJSONString(bankStatementIds));
        }
        picSets(bankStatementIds, telephone, DatumIntegrConstant.STATEMENT_AMOUNT);

        //个人资产
        String financialAssetsIdStr = redisGet(DatumIntegrConstant.REDIS_CLIENT_FINANCIAL);
        List<AttachmentModel> financialAssetsIds;
        if (StringUtils.isNotEmpty(financialAssetsIdStr)){
            financialAssetsIds = JSONObject.parseArray(financialAssetsIdStr, AttachmentModel.class);
        }else {
            financialAssetsIds = datumIntegrModelMapper.financialAssetsId();
            redisSet(DatumIntegrConstant.REDIS_CLIENT_FINANCIAL, JSONObject.toJSONString(financialAssetsIds));
        }
        picSet(financialAssetsIds, telephone);

        EarnCategoryInfoDTO earnCategoryInfoDTO = new EarnCategoryInfoDTO();
        earnCategoryInfoDTO.setProofOfEarningsIds(proofOfEarningsIds);
        earnCategoryInfoDTO.setBankStatementIds(bankStatementIds);
        earnCategoryInfoDTO.setFinancialAssetsIds(financialAssetsIds);
        return earnCategoryInfoDTO;
    }


    /**
     * 封装图片
     * @param list
     */
    private void picSet(List<AttachmentModel> list, String telephone){
        if (list == null || list.size() < 1)
            return;
        for (AttachmentModel attachmentModel : list) {
            getCatagoryPic(telephone,attachmentModel);
        }
    }

    /**
     * 封装多图片
     * @param list
     * @param telephone
     * @param step 步进
     */
    private void picSets(List<AttachmentModel> list, String telephone, Integer step){
        if (list == null || list.size() < 1)
            return;
        for (int i = 0; i < list.size(); i = i + step) {
            getCatagoryPics(list, list.get(i), step, telephone, i);
        }
    }


    /**
     * 获取redis中的缓存
     * @param key
     * @return
     */
    public String redisGet(String key){
        try {
            ValueOperations<String, String> redisOptions = redisTemplate.opsForValue();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            String redisDataStr = redisOptions.get(key);
            return redisDataStr;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * 向redis中放值
     * @param key redis key值
     * @param value redis value值
     */
    public void redisSet(String key,String value){
        try{
            ValueOperations<String, String> redisOptions = redisTemplate.opsForValue();
            redisOptions.set(key, value, DatumIntegrConstant.REDIS_CLIENT_CASE_LOANPRODUCT_TIME, TimeUnit.SECONDS);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    /**
     * 获取单张附件图片
     * @param telephone 当前登录人手机号
     * @param attachmentModel 附件信息
     * @return
     */
    public void getCatagoryPic(String telephone, AttachmentModel attachmentModel){
        try{
            if (StringUtils.isEmpty(telephone) || attachmentModel == null || attachmentModel.getId() == null)
                return;
            Map<String,String> map = rContractDocumentService.getListBase64(telephone, attachmentModel.getId());
           if (map != null && StringUtils.isNotEmpty(map.get("documentId"))){
               attachmentModel.setDocumentId(map.get("documentId"));
               if (StringUtils.isNotEmpty(map.get("bytes")))
                   attachmentModel.setPicture("data:image/jpeg;base64," + map.get("bytes"));
           }
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取多张附件图片
     * @param list
     * @param attachmentModel 当前需要获取的附件类型
     * @param step 步进
     * @param telephone
     * @param j 外层循环角标,我也不知道自己写的啥,凑合看吧
     */
    private void getCatagoryPics(List<AttachmentModel> list, AttachmentModel attachmentModel, Integer step, String telephone, int j) {
        try {
            if (StringUtils.isEmpty(telephone) || attachmentModel == null || attachmentModel.getId() == null)
                return;
            List<Map<String, String>> baseList = rContractDocumentService.getBaseList(telephone, attachmentModel.getId());
            if (baseList == null || baseList.size() < 1)
                return;
            for (int i = 0; i < baseList.size() && i < step; i++){
                if (baseList.get(i) != null && StringUtils.isNotEmpty(baseList.get(i).get("documentId"))){
                    AttachmentModel model = list.get(j);
                    model.setDocumentId(baseList.get(i).get("documentId"));
                    if (StringUtils.isNotEmpty(baseList.get(i).get("url")))
                        model.setPicture(baseList.get(i).get("url"));
                    model.setConfirm(Integer.parseInt(baseList.get(i).get("confirm")));
                }
                j++;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
    }

}
