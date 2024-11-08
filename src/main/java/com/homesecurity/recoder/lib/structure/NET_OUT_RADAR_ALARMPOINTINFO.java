package com.homesecurity.recoder.lib.structure;

import com.homesecurity.recoder.lib.NetSDKLib;

/**
 * 订阅雷达的报警点信息出参(对应接口 CLIENT_AttachRadarAlarmPointInfo)
 *
 * @author ： 119178
 * @since ： Created in 2021/2/03 13:56
 */
public class NET_OUT_RADAR_ALARMPOINTINFO extends NetSDKLib.SdkStructure{
	/**
	 * 结构体大小
	 */
	public int                       dwSize;                  
	
	public NET_OUT_RADAR_ALARMPOINTINFO() {
        this.dwSize = this.size();
    }
}
