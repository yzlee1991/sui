package com.yzlee.sui.client.filter;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.client.listener.Listener;
import com.yzlee.sui.common.abs.Filter;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.modle.push.PushEvent;
import com.yzlee.sui.common.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:18
 */
public class PushFilter extends Filter {

    private List<Listener> list = new ArrayList<Listener>();

    @Override
    public void handle(ProtocolEntity entity) {
        if (ProtocolEntity.Type.PUSH.equals(entity.getType())) {
            System.out.println("PushFilter  handling " + entity);
            try {
                byte[] bytes = Base64.decode(entity.getReply());
                PushEvent event = (PushEvent) CommonUtils.byteArraytoObject(bytes);
                notifyListener(event);
            } catch (Base64DecodingException e) {
                e.printStackTrace();
            }
        } else {
            if (this.filter != null) {
                this.filter.handle(entity);
            } else {
                System.out.println("未知类型：" + entity.getType());
            }
        }
    }

    public void register(Listener listener) {
        list.add(listener);
    }

    public void remove(Listener listener) {
        list.remove(listener);
    }

    public void notifyListener(PushEvent event) {
        for (Listener listener : list) {
            listener.action(event);
        }
    }

}
