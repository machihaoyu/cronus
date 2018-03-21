package com.fjs.cronus.mappers.provider;

import com.fjs.cronus.Common.DatumIntegrConstant;

/**
 * Created by chenjie on 2017/12/18.
 */
public class DatumIntegrModelProvider {

    public String identity(Long customerId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT(1) FROM document d WHERE d.id IN " +
                "(SELECT rcd.document_id FROM r_contract_document rcd WHERE rcd.customer_id='" + customerId + "' AND rcd.document_c_id IN " +
                "(SELECT dc.id FROM document_category dc WHERE dc.document_c_name IN ('" + DatumIntegrConstant.IDENTITY_BORROWER_FACE + "','" + DatumIntegrConstant.IDENTITY_BORROWER_BACK + "'," +
                "'" + DatumIntegrConstant.IDENTITY_OTHER_FACE + "','" + DatumIntegrConstant.IDENTITY_OTHER_BACK + "','" + DatumIntegrConstant.IDENTITY_SPOUSE_FACE + "','" + DatumIntegrConstant.IDENTITY_SPOUSE_BACK + "') AND dc.is_deleted=0) " +
                "AND rcd.is_deleted=0) " +
                "AND d.is_flag<>1 AND d.is_deleted=0 LIMIT 1");
        return stringBuffer.toString();
    }


    public String houseRegistration(Long customerId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT(1) FROM document d WHERE d.id IN " +
                "(SELECT rcd.document_id FROM r_contract_document rcd WHERE rcd.customer_id='" + customerId + "' AND rcd.document_c_id IN " +
                "(SELECT dc.id FROM document_category dc WHERE dc.document_c_name IN ('" + DatumIntegrConstant.HOUSEREGISTRATION + "') AND dc.is_deleted=0) " +
                "AND rcd.is_deleted=0) " +
                "AND d.is_flag<>1 AND d.is_deleted=0 LIMIT 1");
        return stringBuffer.toString();
    }


    public String householdRegister(Long customerId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT(1) FROM document d WHERE d.id IN " +
                "(SELECT rcd.document_id FROM r_contract_document rcd WHERE rcd.customer_id='" + customerId + "' AND rcd.document_c_id IN " +
                "(SELECT dc.id FROM document_category dc WHERE dc.document_c_name IN ('" + DatumIntegrConstant.HOUSEHOLDREGISTER_BORROWER + "','" + DatumIntegrConstant.HOUSEHOLDREGISTER_SPOUSE + "','" + DatumIntegrConstant.HOUSEHOLDREGISTER_OTHER + "') AND dc.is_deleted=0) " +
                "AND rcd.is_deleted=0) " +
                "AND d.is_flag<>1 AND d.is_deleted=0 LIMIT 1");
        return stringBuffer.toString();
    }


    public String proofOfEarnings(Long customerId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT(1) FROM document d WHERE d.id IN " +
                "(SELECT rcd.document_id FROM r_contract_document rcd WHERE rcd.customer_id='" + customerId + "' AND rcd.document_c_id IN " +
                "(SELECT dc.id FROM document_category dc WHERE dc.document_c_name IN ('" + DatumIntegrConstant.PROOFOFEARNINGS_BORROWER + "','" + DatumIntegrConstant.PROOFOFEARNINGS_SPOUSE + "') AND dc.is_deleted=0) " +
                "AND rcd.is_deleted=0) " +
                "AND d.is_flag<>1 AND d.is_deleted=0 LIMIT 1");
        return stringBuffer.toString();
    }


    public String proofOfMarriage(Long customerId){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT COUNT(1) FROM document d WHERE d.id IN " +
                "(SELECT rcd.document_id FROM r_contract_document rcd WHERE rcd.customer_id='" + customerId + "' AND rcd.document_c_id IN " +
                "(SELECT dc.id FROM document_category dc WHERE dc.document_c_name IN ('" + DatumIntegrConstant.PROOFOFMARRIAGE_BORROWER + "','" + DatumIntegrConstant.PROOFOFMARRIAGE_OTHER + "') AND dc.is_deleted=0) " +
                "AND rcd.is_deleted=0) " +
                "AND d.is_flag<>1 AND d.is_deleted=0 LIMIT 1");
        return stringBuffer.toString();
    }


    public String identityId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category" +
                " WHERE document_c_name IN ('" + DatumIntegrConstant.IDENTITY_BORROWER_FACE + "','" + DatumIntegrConstant.IDENTITY_BORROWER_BACK + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String householdRegisterId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.HOUSEHOLDREGISTER_BORROWER + "','" + DatumIntegrConstant.HOUSEHOLDREGISTER_SPOUSE + "'," +
                "'" + DatumIntegrConstant.HOUSEHOLDREGISTER_OTHER + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String houseRegistrationId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.HOUSEREGISTRATION + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String proofOfMarriageId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.PROOFOFMARRIAGE_BORROWER + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String voucherId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.VOUCHER + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String proofOfEarningsId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.PROOFOFEARNINGS_BORROWER + "','" + DatumIntegrConstant.PROOFOFEARNINGS_SPOUSE + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String bankStatementId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.BANK_STATEMENT_BORROWER + "','" + DatumIntegrConstant.BANK_STATEMENT_SPOUSE + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


    public String financialAssetsId(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT id,document_c_name FROM document_category WHERE " +
                "document_c_name IN ('" + DatumIntegrConstant.FINANCIAL_ASSETS + "','" + DatumIntegrConstant.SPARE_ROOM + "') AND is_deleted=0");
        return stringBuffer.toString();
    }


}
