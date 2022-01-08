package com.homesecurity.recoder.module;

import com.homesecurity.recoder.lib.NetSDKLib;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;
import com.sun.jna.ptr.IntByReference;

public class RecordModule {

    public static LLong downloadHandle = new LLong(0);
    public static boolean queryRecordFile(int channelId, NetSDKLib.NET_TIME startTime, NetSDKLib.NET_TIME endTime, NetSDKLib.NET_RECORDFILE_INFO[] fileInfo, IntByReference count){
        int recordFileType = 0;
        boolean bRet = LoginModule.netsdk.CLIENT_QueryRecordFile(LoginModule.loginHandle, channelId, recordFileType, startTime, endTime, null, fileInfo, fileInfo.length*fileInfo[0].size,count,5000, false);
        if (bRet){
            System.out.println("Query Success!! ("+count.getValue()+")");
            for (int i = 0; i < count.getValue(); i++){
                System.out.println(fileInfo[i].nRecordFileType);
                System.out.println(fileInfo[i].starttime.toStringTime());
                System.out.println(fileInfo[i].endtime.toStringTime());
            }
        }else{
            System.err.println("Failed to start real-time monitoring, error code: " + LoginModule.netsdk.CLIENT_GetLastError());
            return false;
        }
        return true;
    }

    public static LLong downloadRecords(int channelId, int recordFileType, NetSDKLib.NET_TIME startTime, NetSDKLib.NET_TIME endTime, String fileName, NetSDKLib.fTimeDownLoadPosCallBack downloadTime){
        downloadHandle = LoginModule.netsdk.CLIENT_DownloadByTimeEx(LoginModule.loginHandle,channelId,recordFileType,startTime,endTime,fileName,downloadTime,null,null,null,null);
        if (downloadHandle.longValue()!=0){
            System.out.println("Success!");
        }else{
            System.err.println("Failed to download, error code: " + LoginModule.netsdk.CLIENT_GetLastError());
        }
        return downloadHandle;
    }
}
