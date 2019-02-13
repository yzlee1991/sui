package com.yzlee.sui.server.rmi.service;

import com.yzlee.sui.common.inf.HostInf;
import com.yzlee.sui.common.modle.push.HostEntity;
import com.yzlee.sui.server.Server;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 12:05
 */
public class HostService implements HostInf {

    @Override
    public List<HostEntity> getOnlineHostEntity() {
        HostEntity hostEntity=Server.newInstance().getOwnHostEntity();
        String identityId=hostEntity.getIdentityId();
        List<HostEntity> list=new ArrayList<HostEntity>();
        for(Thread key:Server.newInstance().hostMap.keySet()){
            HostEntity he=Server.newInstance().hostMap.get(key);
            if(he.getIdentityId().equals(identityId)){
                continue;
            }
            list.add(he);
        }
        return list;
    }

}
