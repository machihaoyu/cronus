package com.fjs.cronus.service.Echo;

import com.fjs.cronus.controller.AllocateController;
import com.fjs.cronus.dto.Echo.EchoDTO;
import com.fjs.cronus.dto.Echo.MsgTmplDTO;
import com.fjs.cronus.dto.Echo.StationMsgReqDTO;
import com.fjs.cronus.service.client.EchoClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by msi on 2018/1/17.
 */
@Service
public class EchoService {
    @Autowired
    EchoClientService echoClientService;

    private static final Logger logger = LoggerFactory.getLogger(EchoService.class);



    public MsgTmplDTO queryMsgTmpl(String token,String tmplType){

        MsgTmplDTO msgTmplDTO = new MsgTmplDTO();
        EchoDTO<MsgTmplDTO> echoDTO = echoClientService.queryMsgTmpl(token,tmplType);
        if (echoDTO.getData() != null) {
            msgTmplDTO = echoDTO.getData();
            return  msgTmplDTO;
        }
        return null;

    }

    //异步发送采用自定义线程
    @Async("mineAsyncPool")
    public void addStationMsg(String token, StationMsgReqDTO stationMsgReqDTO){

            final Thread currentThread = Thread.currentThread();
            final String oldName = currentThread.getName();
            currentThread.setName(String.format(currentThread.getName() + "发送短信线程"));
            long step2Time = System.currentTimeMillis();
            long start = System.currentTimeMillis();
            try {
            logger.debug("开始发送短信");
            echoClientService.addStationMsg(token,stationMsgReqDTO);
        }catch (Exception e){
            logger.error("charge error ", e);
        }finally {
              logger.warn("通信完成,总耗时: " + (System.currentTimeMillis() - start));
                        currentThread.setName(oldName);
        }
    }





}
