package com.homesecurity.recoder.module;
import com.homesecurity.recoder.lib.NetSDKLib;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;
import com.sun.jna.Pointer;

public class PlayModule {

    private DataCallBackEx dataCallBackEx = new DataCallBackEx();

    public LLong startRealPlay(int channel, int stream) {
        return LoginModule.netsdk.CLIENT_RealPlayEx(LoginModule.loginHandle, channel, null, stream);
    }

    public void saveVideo(LLong playHandle, String output){
        LoginModule.netsdk.CLIENT_SetRealDataCallBackEx(playHandle, dataCallBackEx, null, 0x00000001);
        if(playHandle.longValue() == 0) {
            stopRealPlay(playHandle);
        } else {
            LoginModule.netsdk.CLIENT_SaveRealData(playHandle,output);
        }
    }

    public void stopSaveVideo(LLong playHandle){
        if (playHandle.longValue()==0){
            return;
        }
        LoginModule.netsdk.CLIENT_StopSaveRealData(playHandle);
    }

    public void stopRealPlay(LLong playHandle){
        if (playHandle.longValue()==0){
            return;
        }
        LoginModule.netsdk.CLIENT_StopSaveRealData(playHandle);
        boolean bRet = LoginModule.netsdk.CLIENT_StopRealPlayEx(playHandle);
        if (bRet){
            playHandle.setValue(0);
        }
    }

    class DataCallBackEx implements NetSDKLib.fRealDataCallBackEx{
        @Override
        public void invoke(LLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, int param, Pointer dwUser) {

        }
    }
}
