package com.fjs.cronus.mappers;

import com.fjs.cronus.model.RContractDocument;
import com.fjs.cronus.util.MyMapper;


public interface RContractDocumentMapper extends MyMapper<RContractDocument> {


    void addConDocument(RContractDocument rContractDocument);
}