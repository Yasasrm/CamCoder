package com.homesecurity.recoder.lib.structure;

import com.homesecurity.recoder.lib.NetSDKLib;

import static com.homesecurity.recoder.lib.NetSDKLib.MAX_PATH;

/**
 * @author 47081
 * @version 1.0
 * @description
 * @date 2021/2/22
 */
public class ObjectUrl extends NetSDKLib.SdkStructure {
    public byte[] objectUrl=new byte[MAX_PATH];
}
