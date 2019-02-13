package com.yzlee.sui.common.proxy;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.common.abs.AbstractSocketHandle;
import com.yzlee.sui.common.modle.Conversation;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.utils.CommonUtils;
import com.yzlee.sui.common.utils.SocketUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:59
 */
public class CommonRequestSocketHandle extends AbstractSocketHandle implements InvocationHandler {

    // protected long requestTimeout = 10000;

    public CommonRequestSocketHandle(Socket socket, Object target, String targetId) {
        super(socket, target, targetId);
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
        entity.setType(ProtocolEntity.Type.COMMONREQUEST);
        entity.setClassName(target.getClass().getName());
        entity.setMethodName(method.getName());
        entity.setParamsType(paramsType);
        entity.setParams(params);
        // entity.setIdentityId(identityId);
        entity.setTargetId(targetId);
        // entity.setMode(mode);

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

}
