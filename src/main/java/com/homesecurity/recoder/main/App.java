package com.homesecurity.recoder.main;

import com.homesecurity.recoder.lib.NetSDKLib;
import com.homesecurity.recoder.lib.NetSDKLib.LLong;
import com.homesecurity.recoder.module.LoginModule;
import com.homesecurity.recoder.module.PlayModule;
import com.sun.jna.Pointer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Recorder
 *
 */
public class App 
{
    public static void main( String[] args ) {

        while (true) {
            LocalDateTime ldt = LocalDateTime.now();
            String time = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH.mm.ss").format(ldt);
            LLong playHandle = null;
            String camIp = "192.168.1.6";
            int camPort = 37777;
            String camUser = "admin";
            String camPassword = "L28243FF";
            System.out.println(LoginModule.init(DisConnectCallBack.getInstance(), HaveReConnectCallBack.getInstance()) ? "Initialization Success!" : "Initialization Failed!");
            LoginModule.login(camIp, camPort, camUser, camPassword);
            for (int i = 0; i < 5000; i++) {
                playHandle = PlayModule.startRealPlay(0, 0, i, time);
                if (playHandle.longValue() == 0) {
                    LoginModule.logout();
                    LoginModule.cleanup();
                    File file = new File("C:\\Users\\yasas\\Desktop\\New folder\\video_"+time+"_dat");
                    try {
                        boolean result = Files.deleteIfExists(file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private static class DisConnectCallBack implements NetSDKLib.fDisConnect {

        private DisConnectCallBack() {
        }

        private static class CallBackHolder {
            private static DisConnectCallBack instance = new DisConnectCallBack();
        }

        public static DisConnectCallBack getInstance() {
            return CallBackHolder.instance;
        }

        public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("Device[%s] Port[%d] DisConnect!\n", pchDVRIP, nDVRPort);
        }
    }


    private static class HaveReConnectCallBack implements NetSDKLib.fHaveReConnect {
        private HaveReConnectCallBack() {
        }

        private static class CallBackHolder {
            private static HaveReConnectCallBack instance = new HaveReConnectCallBack();
        }

        public static HaveReConnectCallBack getInstance() {
            return CallBackHolder.instance;
        }

        public void invoke(NetSDKLib.LLong m_hLoginHandle, String pchDVRIP, int nDVRPort, Pointer dwUser) {
            System.out.printf("ReConnect Device[%s] Port[%d]\n", pchDVRIP, nDVRPort);
        }
    }
}
