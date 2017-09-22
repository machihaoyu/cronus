package com.fjs.cronus.mappers;

import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.MyMapper;

import java.util.Map;


public interface RContractDocumentMapper extends MyMapper<RContractDocument> {


    void addConDocument(RContractDocument rContractDocument);

    RContractDocument findByFeild(Map<String,Object> paramsMap);
}