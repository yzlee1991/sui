package com.yzlee.sui.common.abs;

import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:48
 */
public abstract class AbstractSocketHandle {

    // public static ConcurrentMap<String, Object> conversationMap = new
    // ConcurrentHashMap<String, Object>();

//	protected Gson gson = new Gson();

    protected Socket socket;

    protected Object target;

    // protected String identityId;

    protected String targetId;

//	protected ProtocolEntity.Mode mode;

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

    protected AbstractSocketHandle(Socket socket, Object target, String targetId) {
//		if (ProtocolEntity.Mode.INVOKE != mode && ProtocolEntity.Mode.COMMAND != mode) {
//			throw new RuntimeException("非法的代理模式，mode：" + mode);
//		}
        this.socket = socket;
        this.target = target;
        // this.identityId = identityId;
        this.targetId = targetId;
//		this.mode = mode;
    }

}
