package com.fjs.cronus.mappers;

import com.fjs.cronus.mappers.provider.DatumIntegrModelProvider;
import org.apache.ibatis.annotations.SelectProvider;

/**
 * Created by chenjie on 2017/12/16.
 */
public interface DatumIntegrModelMapper {

    /**
     * 获取用户身份证附件分类已确认上传数量
     * @param customerId 客户id
     * @return
     */
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "identity")
    Integer identity(Long customerId);


    /**
     * 获取用户房产证附件分类已确认上传数量
     * @param customerId 客户id
     * @return
     */
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "houseRegistration")
    Integer houseRegistration(Long customerId);


    /**
     * 获取用户户口簿附件分类已确认上传数量
     * @param customerId 客户id
     * @return
     */
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "householdRegister")
    Integer householdRegister(Long customerId);


    /**
     * 获取用户收入证明附件分类已确认上传数量
     * @param customerId 客户id
     * @return
     */
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "proofOfEarnings")
    Integer proofOfEarnings(Long customerId);


    /**
     * 获取用户婚姻证明附件分类已确认上传数量
     * @param customerId 客户id
     * @return
     */
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "proofOfMarriage")
    Integer proofOfMarriage(Long customerId);
}
