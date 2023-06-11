package com.hspedu.qqserver.service;

import com.hspedu.qqcommon.Message;
import com.hspedu.qqcommon.MessageType;
import com.hspedu.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class QQServer {
    private ServerSocket ss;
    private static HashMap<String, User> validUsers = new HashMap<>();
    private static ConcurrentHashMap<String, ArrayList<Message>> ofLineDb = new ConcurrentHashMap<>();

    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));
    }

    public boolean checkUser(String userId, String passwd) {
        User user = validUsers.get(userId);
        if (user == null) {
            return false;
        }
        if (!user.getUserwd().equals(passwd)) {
            return false;
        }
        return true;
    }

    public QQServer() {
        try {
            System.out.println("服务端在9999端口进行监听");
            new Thread(new SendNewsToAllService()).start();
            ss = new ServerSocket(9999);
            while (true) {
                Socket socket = ss.accept();
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User u = (User) ois.readObject();
                Message message = new Message();


                if (checkUser(u.getUserId(), u.getUserwd())) {
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    oos.writeObject(message);

                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    serverConnectClientThread.start();

                    ManageClientThreads.addClientThread(u.getUserId(), serverConnectClientThread);
                } else {
                    System.out.println("验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    oos.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
