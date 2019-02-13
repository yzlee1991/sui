package com.yzlee.sui.server.push;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.modle.push.PushEvent;
import com.yzlee.sui.common.utils.CommonUtils;
import com.yzlee.sui.common.utils.SocketUtils;
import com.yzlee.sui.server.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 12:03
 */
public class PushServer {

    private static volatile PushServer pushServer;

    private PushServer() {
    }

    public static PushServer newInstance() {
        if (pushServer == null) {
            synchronized (PushServer.class) {
                if (pushServer == null) {
                    pushServer = new PushServer();
                }
            }
        }
        return pushServer;
    }

    public void push(PushEvent event, String... filterIdentityIds) throws IOException {
        Set<String> filterSet=new HashSet<String>(Arrays.asList(filterIdentityIds));
        ProtocolEntity entity=new ProtocolEntity();
        entity.setType(ProtocolEntity.Type.PUSH);
        byte[] bytes=CommonUtils.ObjectToByteArray(event);
        String reply=Base64.encode(bytes);
        entity.setReply(reply);

        for(String key:Server.newInstance().socketMap.keySet()){
            if(filterSet.contains(key)){
                continue;
            }
            Socket socket=Server.newInstance().socketMap.get(key);
            SocketUtils.send(socket, entity);
        }
    }

}
