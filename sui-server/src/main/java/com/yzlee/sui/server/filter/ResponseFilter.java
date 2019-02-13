package com.yzlee.sui.server.filter;

import com.yzlee.sui.common.abs.Filter;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.utils.SocketUtils;
import com.yzlee.sui.server.Server;

import java.net.Socket;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:58
 */
public class ResponseFilter extends Filter {

    @Override
    public void handle(ProtocolEntity entity) {
        try{
            if(ProtocolEntity.Type.RESPONSE.equals(entity.getType())){
                System.out.println("ResponseFilter  handling "+entity);
                //转发，之后添加权限控制
                Socket targetSocket=Server.newInstance().socketMap.get(entity.getTargetId());
                SocketUtils.sendByNoBlock(targetSocket, entity);
            }else{
                if(this.filter!=null){
                    this.filter.handle(entity);
                }else{
                    System.out.println("未知类型："+entity.getType());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
