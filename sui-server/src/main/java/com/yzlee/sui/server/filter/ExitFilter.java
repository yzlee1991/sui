package com.yzlee.sui.server.filter;

import com.google.gson.Gson;
import com.yzlee.sui.common.abs.Filter;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.server.Server;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:57
 */
public class ExitFilter extends Filter {

    private Gson gson = new Gson();

    @Override
    public void handle(ProtocolEntity entity) {
        try {
            if (ProtocolEntity.Type.EXIT.equals(entity.getType())) {
                System.out.println("ExitFilter  handling  " + entity);
                Thread.currentThread().interrupt();
                Server.newInstance().outLine(entity.getIdentityId());
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
