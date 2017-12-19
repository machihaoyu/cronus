package com.fjs.cronus.mappers;

import com.fjs.cronus.mappers.provider.DatumIntegrModelProvider;
import com.fjs.cronus.model.AttachmentModel;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

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

    /**
     * 获取身份证分类的id,分类名
     * @return
     */
    @Results(id = "category", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "document_c_name", property = "documentName")
    })
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "identityId")
    List<AttachmentModel> identityId();


    /**
     * 获取户口簿分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "householdRegisterId")
    List<AttachmentModel> householdRegisterId();


    /**
     * 获取房产证分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "houseRegistrationId")
    List<AttachmentModel> houseRegistrationId();


    /**
     * 获取结婚证分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "proofOfMarriageId")
    List<AttachmentModel> proofOfMarriageId();


    /**
     * 获取放款凭证分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "voucherId")
    List<AttachmentModel> voucherId();


    /**
     * 获取收入证明分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "proofOfEarningsId")
    List<AttachmentModel> proofOfEarningsId();


    /**
     * 获取银行流水分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "bankStatementId")
    List<AttachmentModel> bankStatementId();


    /**
     * 获取个人资产证明分类的id,分类名
     * @return
     */
    @ResultMap("category")
    @SelectProvider(type = DatumIntegrModelProvider.class, method = "financialAssetsId")
    List<AttachmentModel> financialAssetsId();
}
