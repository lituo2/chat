package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

public class SendNewsToAllService implements Runnable {


    @Override
    public void run() {
        while (true) {
            System.out.println("请输入服务器要推送的新闻(输入exit退出推送服务)");
            String news = Utility.readString(100);
            if ("exit".equals(news)) {
                break;
            }
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_TOALL_MES);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(new java.util.Date().toString());
            System.out.println("服务器推送消息给所有人说" + news);

            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                ServerConnectClientThread serverConnectClientThread = hm.get(onLineUserId);
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(serverConnectClientThread.getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
