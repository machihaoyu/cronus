package com.fjs.cronus.dto.thea;

import com.fjs.cronus.model.AttachmentModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenjie on 2017/12/25.
 */
public class EarnCategoryInfoDTO implements Serializable{

    private static final long serialVersionUID = 4717599493868105760L;

    @ApiModelProperty(name = "proofOfEarningsIds", value = "收入证明-收入证明分类")
    private List<AttachmentModel> proofOfEarningsIds;

    @ApiModelProperty(name = "bankStatementIds", value = "收入证明-银行流水分类")
    private List<AttachmentModel> bankStatementIds;

    @ApiModelProperty(name = "financialAssetsIds", value = "收入证明-个人资产证明分类")
    private List<AttachmentModel> financialAssetsIds;

    public List<AttachmentModel> getProofOfEarningsIds() {
        return proofOfEarningsIds;
    }

    public void setProofOfEarningsIds(List<AttachmentModel> proofOfEarningsIds) {
        this.proofOfEarningsIds = proofOfEarningsIds;
    }

    public List<AttachmentModel> getBankStatementIds() {
        return bankStatementIds;
    }

    public void setBankStatementIds(List<AttachmentModel> bankStatementIds) {
        this.bankStatementIds = bankStatementIds;
    }

    public List<AttachmentModel> getFinancialAssetsIds() {
        return financialAssetsIds;
    }

    public void setFinancialAssetsIds(List<AttachmentModel> financialAssetsIds) {
        this.financialAssetsIds = financialAssetsIds;
    }
}
