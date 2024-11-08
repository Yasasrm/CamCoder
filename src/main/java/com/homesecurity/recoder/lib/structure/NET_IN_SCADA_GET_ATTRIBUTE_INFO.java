package com.homesecurity.recoder.lib.structure;

import com.homesecurity.recoder.lib.NetSDKLib;

public class NET_IN_SCADA_GET_ATTRIBUTE_INFO extends NetSDKLib.SdkStructure {
    /**
     *  结构体大小
     */
    public int dwSize;

    /**
     *  获取条件
     */
    public NET_GET_CONDITION_INFO stuCondition;

    public NET_IN_SCADA_GET_ATTRIBUTE_INFO(){
        this.dwSize = this.size();
    }
}
