package com.yzlee.sui.common.inf;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:57
 */
public interface UpdateInf extends Rmiable {

    public Long getCorpseLastestSize();

    public byte[] getCorpseUpdatePart(int partSize, int partNum);

}
