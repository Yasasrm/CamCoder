package com.homesecurity.recoder.lib.structure;

import com.homesecurity.recoder.lib.NetSDKLib.SdkStructure;

public class NET_OUT_SET_PARK_CONTROL_INFO  extends SdkStructure{
	/**
	 * 结构体大小
	 */
	public int					dwSize;					 

	public NET_OUT_SET_PARK_CONTROL_INFO() {
        this.dwSize = this.size();
    }
}
