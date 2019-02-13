package com.yzlee.sui.common.inf;

import com.yzlee.sui.common.modle.TreeFileList;

import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:52
 */
public interface FileInf {

    public List<TreeFileList> getRootList();

    public List<TreeFileList> getFileList(String filePath);

    public List<TreeFileList> getFileList();

    public byte[] getFilePart(String filePath, int partSize, int partNum);

}
