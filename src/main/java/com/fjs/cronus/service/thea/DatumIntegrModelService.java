package com.fjs.cronus.service.thea;

import com.fjs.cronus.enums.CategoryEnum;
import com.fjs.cronus.exception.CronusException;
import com.fjs.cronus.mappers.DatumIntegrModelMapper;
import com.fjs.cronus.model.AttachmentModel;
import com.fjs.cronus.model.thea.DatumIntegrModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenjie on 2017/12/18.
 */
@Service
public class DatumIntegrModelService {

    @Autowired
    private DatumIntegrModelMapper datumIntegrModelMapper;

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
     * @return
     */
    public List<AttachmentModel> getCategoryInfo(Integer type) {
        CategoryEnum enumByCode = CategoryEnum.getEnumByCode(type);
        if (enumByCode == null)
            throw new CronusException(CronusException.Type.CRM_PARAMS_ERROR, CronusException.Type.CRM_PARAMS_ERROR.getError() + "||附件类型");

        List<AttachmentModel> list = null;

        if (CategoryEnum.IDENTITY.getCode().equals(enumByCode.getCode())){
            //身份证
            list = datumIntegrModelMapper.identityId();
            return list;
        } else if (CategoryEnum.HOUSEHOLDREGISTER.getCode().equals(enumByCode.getCode())) {
            //户口簿
            list = datumIntegrModelMapper.householdRegisterId();
            return list;
        } else if (CategoryEnum.PROPERTYCERTIFICATE.getCode().equals(enumByCode.getCode())) {
            //房产证
            list = datumIntegrModelMapper.houseRegistrationId();
            return list;
        } else if (CategoryEnum.MARRIAGECERTIFICATE.getCode().equals(enumByCode.getCode())) {
            //结婚证
            list = datumIntegrModelMapper.proofOfMarriageId();
            return list;
        } else if (CategoryEnum.VOUCHER.getCode().equals(enumByCode.getCode())) {
            //放款凭证
            list = datumIntegrModelMapper.voucherId();
            return list;
        }
        return list;
    }

    /**
     * 获取收入证明附件分类名称、id
     * @return
     */
    public List<List<AttachmentModel>> getEarnCategoryInfo() {
        List<AttachmentModel> proofOfEarningsIds = datumIntegrModelMapper.proofOfEarningsId();
        List<AttachmentModel> bankStatementIds = datumIntegrModelMapper.bankStatementId();
        List<AttachmentModel> financialAssetsIds = datumIntegrModelMapper.financialAssetsId();
        List<List<AttachmentModel>> lists = new ArrayList<>(3);
        lists.add(proofOfEarningsIds);
        lists.add(bankStatementIds);
        lists.add(financialAssetsIds);
        return lists;
    }
}
