package com.fjs.cronus.service.thea;

import com.fjs.cronus.mappers.DatumIntegrModelMapper;
import com.fjs.cronus.model.thea.DatumIntegrModelDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
