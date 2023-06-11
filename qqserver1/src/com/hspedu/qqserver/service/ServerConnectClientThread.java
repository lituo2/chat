package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String useId;

    public ServerConnectClientThread(Socket socket, String useId) {
        this.socket = socket;
        this.useId = useId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("客户端与服务端" + useId + "保持通讯,读取数据...");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    System.out.println(message.getSender() + "用户列表");
                    String onlineUser = ManageClientThreads.getOnlineUser();

                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message2.setContent(onlineUser);
                    message2.setGetter(message.getSender());

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);//如果想实现离线留言，把消息保存到数据库即可
                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + "退出");
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    socket.close();
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)) {
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onLineUseId = iterator.next().toString();

                        if (!onLineUseId.equals(message.getSender())) {
                            ObjectOutputStream oos = new ObjectOutputStream(hm.get(onLineUseId).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }

                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getGetter()).getSocket().getOutputStream());
                    oos.writeObject(message);
                } else {
                    System.out.println("其他类型的message，暂时不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
