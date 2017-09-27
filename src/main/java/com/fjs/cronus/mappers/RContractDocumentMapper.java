package com.fjs.cronus.mappers;

import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.MyMapper;

import java.util.List;
import java.util.Map;


public interface RContractDocumentMapper{


    void addConDocument(RContractDocument rContractDocument);

    RContractDocument findByFeild(Map<String,Object> paramsMap);

    List<RContractDocument> ocrDocument(Map<String,Object> paramsMap);
}