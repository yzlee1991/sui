package com.yzlee.sui.common.rmi;

import com.yzlee.sui.common.modle.Conversation;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.proxy.RmiRequestSocketHandle;
import com.yzlee.sui.common.utils.SocketUtils;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.UUID;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:07
 */
//目前rmi远程调用对于返回值为long基础数据类型的调用会报错，这个之后需要解决
public class RmiClient {

    public static Object lookup(Socket socket, String rmiName) throws IOException {
        ProtocolEntity entity = new ProtocolEntity();
        String conversationId = UUID.randomUUID().toString();
        entity.setType(ProtocolEntity.Type.RMI);
        entity.setConversationId(conversationId);
        entity.setRmiName(rmiName);

        //先put再send，否则多线程会有问题
        Conversation.Data data = new Conversation.Data();
        String lock = new String(conversationId);
        data.setLock(lock);
        Conversation.MAP.put(conversationId, data);

        SocketUtils.send(socket, entity);

        synchronized (lock) {
            try {
                lock.wait(Conversation.REQUESTTIMEOUT);
                ProtocolEntity replyEntity = Conversation.MAP.get(conversationId).getEntity();
                Conversation.MAP.remove(conversationId);
                if (replyEntity == null) {
                    throw new RuntimeException("rmi服务请求超时");
                }
                if (ProtocolEntity.ReplyState.SUCCESE == replyEntity.getReplyState()) {
                    String infName = replyEntity.getReply();
                    Class inf = Class.forName(infName);
                    RmiRequestSocketHandle rsh = new RmiRequestSocketHandle(socket, rmiName);
                    return Proxy.newProxyInstance(inf.getClassLoader(), new Class[] { inf }, rsh);//重点，如何只通过接口获得代理类

                } else if (ProtocolEntity.ReplyState.ERROR == replyEntity.getReplyState()) {
                    throw new RuntimeException(replyEntity.getReply());
                } else {
                    // 未定义的回复状态
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
