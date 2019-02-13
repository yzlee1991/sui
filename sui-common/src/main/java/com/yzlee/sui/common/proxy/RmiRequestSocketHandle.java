package com.yzlee.sui.common.proxy;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.common.modle.Conversation;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.utils.CommonUtils;
import com.yzlee.sui.common.utils.SocketUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.*;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:09
 */
public class RmiRequestSocketHandle implements InvocationHandler {

    // 基础类型
    @SuppressWarnings("serial")
    protected Set<Class<?>> baseType = new HashSet<Class<?>>() {
        {
            add(Integer.TYPE);
            add(Double.TYPE);
            add(Float.TYPE);
            add(Long.TYPE);
            add(Short.TYPE);
            add(Byte.TYPE);
            add(Boolean.TYPE);
            add(Character.TYPE);
            add(Void.TYPE);

        }
    };

    private Socket socket;

    private String rmiName;

    public RmiRequestSocketHandle(Socket socket, String rmiName) {
        super();
        this.socket = socket;
        this.rmiName = rmiName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
//		if (!baseType.contains(returnType) && !Serializable.class.isAssignableFrom(returnType)) {
//			// 返回值类型不可序列化，抛异常
//			System.out.println("返回值不可传输");
//			return null;
//		}

        List<String> paramsType = new ArrayList<String>();
        List<String> params = new ArrayList<String>();
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; i++) {

            if (!baseType.contains(types[i]) && !Serializable.class.isAssignableFrom(types[i])) {
                // 参数类型不可序列化，抛异常
                System.out.println("参数不可传输");
                return null;
            }
            byte[] bytes = CommonUtils.ObjectToByteArray(args[i]);
            String base64Param = Base64.encode(bytes);
            params.add(base64Param);
            paramsType.add(types[i].getTypeName());
        }

        ProtocolEntity entity = new ProtocolEntity();
        String conversationId = UUID.randomUUID().toString();
        entity.setConversationId(conversationId);
        entity.setType(ProtocolEntity.Type.RMIREQUEST);
        entity.setRmiName(rmiName);
        entity.setMethodName(method.getName());
        entity.setParamsType(paramsType);
        entity.setParams(params);
//		entity.setIdentityId("1");//之后这个要在服务端维护
//		entity.setTargetId(targetId);
//		entity.setMode(ProtocolEntity.Mode.INVOKE);//之后撤销
        SocketUtils.send(socket, entity);


        Conversation.Data data = new Conversation.Data();
        String lock = new String(conversationId);
        data.setLock(lock);
        Conversation.MAP.put(conversationId, data);
        synchronized (lock) {
            try {
                lock.wait(Conversation.REQUESTTIMEOUT);
                ProtocolEntity replyEntity = Conversation.MAP.get(conversationId).getEntity();
                Conversation.MAP.remove(conversationId);
                if (replyEntity == null) {
                    throw new RuntimeException("请求超时");
                }
                if (ProtocolEntity.ReplyState.SUCCESE == replyEntity.getReplyState()) {
                    String base64Reply = replyEntity.getReply();
                    byte[] bytes = Base64.decode(base64Reply);
                    Object reply = CommonUtils.byteArraytoObject(bytes);
                    return reply;
                } else if (ProtocolEntity.ReplyState.ERROR == replyEntity.getReplyState()) {
                    throw new RuntimeException(replyEntity.getReply());
                } else {
                    // 未定义的回复状态
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getRmiName() {
        return rmiName;
    }

    public void setRmiName(String rmiName) {
        this.rmiName = rmiName;
    }

}
