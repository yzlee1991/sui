package com.yzlee.sui.common.proxy;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.common.abs.AbstractSocketHandle;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.utils.CommonUtils;
import com.yzlee.sui.common.utils.SocketUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:08
 */
public class ResponseSocketHandle extends AbstractSocketHandle implements InvocationHandler {

    private String conversationId;

    public ResponseSocketHandle(Socket socket, Object target, String targetId) {
        super(socket, target, targetId);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ProtocolEntity entity = new ProtocolEntity();
        Object returnValue = null;
        try {
            returnValue = method.invoke(target, args);
            byte[] bytes = CommonUtils.ObjectToByteArray(returnValue);
            String base64Reply = Base64.encode(bytes);

            entity.setConversationId(conversationId);
            entity.setType(ProtocolEntity.Type.RESPONSE);
            entity.setReplyState(ProtocolEntity.ReplyState.SUCCESE);
            entity.setReply(base64Reply);
            // entity.setIdentityId(identityId);
            entity.setTargetId(targetId);

        } catch (Exception e) {
            System.out.println("捕获到调用异常");
            e.printStackTrace();
            entity.setConversationId(conversationId);
            entity.setType(ProtocolEntity.Type.RESPONSE);
            entity.setReplyState(ProtocolEntity.ReplyState.ERROR);
            entity.setReply(e.getMessage());// 先简单处理，之后输出整个异常栈信息
            // entity.setIdentityId(identityId);
            entity.setTargetId(targetId);
        }
        SocketUtils.send(socket, entity);

        return returnValue;

    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

}
