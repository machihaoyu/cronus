package com.fjs.cronus.mappers;

import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.MyMapper;
import org.omg.CORBA.INTERNAL;

import java.util.List;
import java.util.Map;


public interface RContractDocumentMapper{


    void addConDocument(RContractDocument rContractDocument);

    RContractDocument findByFeild(Map<String,Object> paramsMap);

    List<RContractDocument> ocrDocument(Map<String,Object> paramsMap);

    Integer checkCustomerIsUpload(Map<String,Object> paramsMap);

    List<Integer> findListByFeild(Map<String,Object> paramsMap);

    void update(RContractDocument rContractDocument);

    RContractDocument ocrDocumentToClient(Map<String,Object> paramsMap);
}