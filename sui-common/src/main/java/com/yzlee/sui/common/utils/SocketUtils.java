package com.yzlee.sui.common.utils;

import com.yzlee.sui.common.modle.ProtocolEntity;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:04
 */
public class SocketUtils {

    final static String ENCODING="UTF-8";
    private static Map<Socket, BufferedReader> brMap = new HashMap<Socket, BufferedReader>();
    private static Map<Socket, BufferedWriter> bwMap = new HashMap<Socket, BufferedWriter>();

    // 非阻塞发送，目前只适用于服务端这种一个线程对应一个客户端不存在并发的情况
    public static void sendByNoBlock(Socket socket, Object obj) throws IOException {
        if (obj == null || socket == null || socket.isClosed()) {
            return;
        }
        String json = CommonUtils.gson.toJson(obj);
        BufferedWriter bw = getBufferWriter(socket);
        bw.write(json);
        bw.newLine();
        bw.flush();
    }

    public static void send(Socket socket, Object obj) throws IOException {
        if (obj == null || socket == null || socket.isClosed()) {
            return;
        }
        String json = CommonUtils.gson.toJson(obj);
        BufferedWriter bw = getBufferWriter(socket);
        synchronized (socket) {
            bw.write(json);
            bw.newLine();
            bw.flush();
        }
    }

    public static ProtocolEntity receive(Socket socket) throws IOException {
        BufferedReader br = getBufferedReader(socket);
        String json = br.readLine();
        if(json==null){
            //linux 下按crt+c关闭的时候socket正常关闭，readLine返回null，待验证
            throw new RuntimeException("socket已关闭");
        }
        ProtocolEntity entity = CommonUtils.gson.fromJson(json, ProtocolEntity.class);
        return entity;
    }

    private static BufferedReader getBufferedReader(Socket socket) throws IOException {
        BufferedReader br = brMap.get(socket);
        if (br == null) {
            synchronized (socket) {
                br = brMap.get(socket);
                if (br != null) {
                    return br;
                }
                br = new BufferedReader(new InputStreamReader(socket.getInputStream(),ENCODING));
                brMap.put(socket, br);
            }
        }
        return br;
    }

    private static BufferedWriter getBufferWriter(Socket socket) throws IOException {
        BufferedWriter bw = bwMap.get(socket);
        if (bw == null) {
            synchronized (socket) {
                bw = bwMap.get(socket);
                if (bw != null) {
                    return bw;
                }
                bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),ENCODING));
                bwMap.put(socket, bw);
            }
        }
        return bw;
    }

}
