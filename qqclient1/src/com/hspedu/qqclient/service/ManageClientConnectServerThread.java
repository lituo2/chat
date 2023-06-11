package com.hspedu.qqclient.service;

import java.util.HashMap;

public class ManageClientConnectServerThread {
    private static HashMap<String, ClinentConnectServerThread> hm = new HashMap<>();

    public static void addClientConnectServerThread(String useId, ClinentConnectServerThread clinentConnectServerThread) {
        hm.put(useId, clinentConnectServerThread);
    }

    public static ClinentConnectServerThread getClientConnectServerThread(String userId) {
        return hm.get(userId);
    }
}
