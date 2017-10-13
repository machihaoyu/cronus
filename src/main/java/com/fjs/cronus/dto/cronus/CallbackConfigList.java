package com.fjs.cronus.dto.cronus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by msi on 2017/10/13.
 */
public class CallbackConfigList implements Serializable {

    private List<CallbackConfigDTO> callbackConfigDTOS;


    public List<CallbackConfigDTO> getCallbackConfigDTOS() {
        return callbackConfigDTOS;
    }

    public void setCallbackConfigDTOS(List<CallbackConfigDTO> callbackConfigDTOS) {
        this.callbackConfigDTOS = callbackConfigDTOS;
    }
}
