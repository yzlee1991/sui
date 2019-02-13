package com.yzlee.sui.corpse.filter;

import com.yzlee.sui.common.abs.Filter;
import com.yzlee.sui.common.modle.Conversation;
import com.yzlee.sui.common.modle.ProtocolEntity;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:47
 */
public class ResponseFilter extends Filter {

    @Override
    public void handle(ProtocolEntity entity) {
        try {
            if (ProtocolEntity.Type.RESPONSE.equals(entity.getType())) {
                System.out.println("ResponseFilter  handling " + entity);
                Conversation.Data data=Conversation.MAP.get(entity.getConversationId());
                if(data==null){
                    System.out.println("收到过期的回复"+entity);
                    return;
                }
                data.setEntity(entity);
                String lock=data.getLock();
                synchronized (lock) {
                    lock.notify();
                }
            } else {
                if (this.filter != null) {
                    this.filter.handle(entity);
                } else {
                    System.out.println("未知类型：" + entity.getType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
