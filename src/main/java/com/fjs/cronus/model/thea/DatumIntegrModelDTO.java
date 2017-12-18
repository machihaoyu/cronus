package com.fjs.cronus.model.thea;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by chenjie on 2017/12/15.
 */
public class DatumIntegrModelDTO implements Serializable {

    private static final long serialVersionUID = 6372790354111002693L;

    @ApiModelProperty(name = "identity", value = "是否有身份证材料 1:有;0:无")
    private Integer identity;//是否有身份证材料 1:有;0:无

    @ApiModelProperty(name = "houseRegistration", value = "是否有房产证材料 1:有;0:无")
    private Integer houseRegistration;//是否有房产证材料 1:有;0:无

    @ApiModelProperty(name = "householdRegister", value = "是否有户口本材料 1:有;0:无")
    private Integer householdRegister;//是否有户口本材料 1:有;0:无

    @ApiModelProperty(name = "proofOfEarnings", value = "是否有收入证明材料 1:有;0:无")
    private Integer proofOfEarnings;//是否有收入证明材料 1:有;0:无

    @ApiModelProperty(name = "proofOfMarriage", value = "是否有婚姻证明材料 1:有;0:无")
    private Integer proofOfMarriage;//是否有婚姻证明材料 1:有;0:无

    public Integer getIdentity() {
        return identity;
    }

    public void setIdentity(Integer identity) {
        this.identity = identity;
    }

    public Integer getHouseRegistration() {
        return houseRegistration;
    }

    public void setHouseRegistration(Integer houseRegistration) {
        this.houseRegistration = houseRegistration;
    }

    public Integer getHouseholdRegister() {
        return householdRegister;
    }

    public void setHouseholdRegister(Integer householdRegister) {
        this.householdRegister = householdRegister;
    }

    public Integer getProofOfEarnings() {
        return proofOfEarnings;
    }

    public void setProofOfEarnings(Integer proofOfEarnings) {
        this.proofOfEarnings = proofOfEarnings;
    }

    public Integer getProofOfMarriage() {
        return proofOfMarriage;
    }

    public void setProofOfMarriage(Integer proofOfMarriage) {
        this.proofOfMarriage = proofOfMarriage;
    }
}
