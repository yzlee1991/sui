package com.yzlee.sui.common.service;

import com.yzlee.sui.common.inf.FileInf;
import com.yzlee.sui.common.modle.TreeFileList;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:05
 */
public class FileService implements FileInf {

    @Override
    public List<TreeFileList> getRootList() {
        List<TreeFileList> rootList = new ArrayList<TreeFileList>();
        File[] root = File.listRoots();
        for (File r : root) {
            TreeFileList treeFileList = new TreeFileList();
            treeFileList.setFileName(r.getAbsolutePath());
            rootList.add(treeFileList);
        }
        return rootList;
    }

    @Override
    public List<TreeFileList> getFileList(String filePath) {
        File file = new File(filePath);
        TreeFileList tfl = getFileList(file);
        return tfl.getList();
    }

    @Override
    public List<TreeFileList> getFileList() {
        List<TreeFileList> rootList = new ArrayList<TreeFileList>();
        File[] root = File.listRoots();
        for (File r : root) {
            rootList.add(getFileList(r));
        }
        return rootList;
    }

    private TreeFileList getFileList(File file) {
        TreeFileList treeFileList = new TreeFileList();
        treeFileList.setFileName(file.getName());
        treeFileList.setFilePath(file.getAbsolutePath());
        treeFileList.setModifyTime(file.lastModified());
        if (file.isDirectory()) {
            long fileSize = 0;
            List<TreeFileList> list = new ArrayList<TreeFileList>();
            File[] files = file.listFiles();
            if (files == null) {
                return null;
            }
            for (File f : files) {
                TreeFileList tfl = getFileList(f);
                if (tfl == null) {
                    continue;
                }
                fileSize += tfl.getFileSize();
                list.add(tfl);
            }
            treeFileList.setFileSize(fileSize);
            treeFileList.setDirectory(true);
            treeFileList.setList(list);
        } else {
            treeFileList.setDirectory(false);
            treeFileList.setFileSize(file.length());
        }
        return treeFileList;
    }

    @Override
    public byte[] getFilePart(String filePath, int partSize, int partNum) {
        byte[] bytes = new byte[partSize];
        try {
            File file = new File(filePath);
            int startIndex = (partNum - 1) * partSize;
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(startIndex);
            int num = raf.read(bytes);
            if (num != partSize) {
                byte[] copyBytes = new byte[num];
                System.arraycopy(bytes, 0, copyBytes, 0, num);
                bytes = copyBytes;
            }
            System.out.println("下载文件：" + filePath + " 第" + partNum + "部分完成");
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("下载文件异常，文件路径：" + filePath + " 大小：" + partSize + " 序号：" + partNum);
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
		/*FileService se = new FileService();
		String path = "H:\\公考资料\\第二阶段  专项\\广东\\判断推理\\翻译推理.mp4";
		File srcf = new File(path);
		File desf = new File("G:\\aa.mp4");
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(desf, true));
		long blockCount = srcf.length() / (1024 * 1024);
		blockCount = srcf.length() % (1024 * 1024) == 0 ? blockCount : blockCount + 1;
		for (int i = 1; i <= blockCount; i++) {
			byte[] bytes = se.getFilePart(path, 1024 * 1024, i);
			bos.write(bytes);
		}
		bos.flush();
		bos.close();*/

        new FileService().getFileList(new File("D:\\"));
    }

}
