package com.yzlee.sui.common.infImpl;

import com.yzlee.sui.common.inf.Listener;
import com.yzlee.sui.common.inf.Observerable;
import com.yzlee.sui.common.modle.ProtocolEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:58
 */
public class Observer implements Observerable {

    private List<Listener> list=new ArrayList<Listener>();

    @Override
    public void register(Listener listener) {
        list.add(listener);
    }

    @Override
    public void remove(Listener listener) {
        list.remove(listener);
    }

    @Override
    public void notifyListener(ProtocolEntity entity) {
        for(Listener listener:list){
            listener.action(entity);
        }
    }

}
