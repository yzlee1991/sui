package com.yzlee.sui.server;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.yzlee.sui.common.abs.Filter;
import com.yzlee.sui.common.modle.ProtocolEntity;
import com.yzlee.sui.common.modle.push.HostEntity;
import com.yzlee.sui.common.modle.push.HostOnlineEvent;
import com.yzlee.sui.common.modle.push.HostOutlineEvent;
import com.yzlee.sui.common.utils.CommonUtils;
import com.yzlee.sui.common.utils.MillisecondClock;
import com.yzlee.sui.common.utils.RSAUtils;
import com.yzlee.sui.common.utils.SocketUtils;
import com.yzlee.sui.server.db.entity.Auth;
import com.yzlee.sui.server.db.mapper.AuthMapper;
import com.yzlee.sui.server.filter.*;
import com.yzlee.sui.server.push.PushServer;
import com.yzlee.sui.server.rmi.RmiServer;
import com.yzlee.sui.server.utils.Mybaits;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:59
 */
public class Server {

    Logger logger = LoggerFactory.getLogger(Server.class);

    private static volatile Server server;

    private Server() {
    }

    public static Server newInstance() {
        if (server == null) {
            synchronized (Server.class) {
                if (server == null) {
                    server = new Server();
                }
            }
        }
        return server;
    }

    // 之后添加factory，要能在主线程捕获其他线程中的异常
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        return thread;
    });

    final MillisecondClock clock = new MillisecondClock(cachedThreadPool);

    private Filter headFilter = null;

    private long timeout = 30000;

    private int delayTime = 100;

    private long lastTime = clock.now();

    private RmiServer rmiServer = RmiServer.newInstance();

    // 缓存心跳检测时间
    // public Map<Socket, Long> heartBeatMap = new HashMap<Socket, Long>();

    // 缓存连接服务器的所有socket
    public Map<String, Socket> socketMap = new ConcurrentHashMap<String, Socket>();// key=identityId

    // 用于关联上下2个map
    public Map<String, Thread> threadMap = new ConcurrentHashMap<String, Thread>();// key=identityId

    // 缓存socket对应的identityId
    // public Map<Socket, String> identityIdMap = new HashMap<Socket, String>();

    // 缓存用户信息
    public Map<Thread, HostEntity> hostMap = new ConcurrentHashMap<Thread, HostEntity>();// key=currentThread

    public void start() {
        init();
        logger.info("启动服务...");
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket socket = serverSocket.accept();
                cachedThreadPool.execute(() -> {
                    try {
                        // 1.登陆
                        login(socket);
                        // if (!flag) {
                        // br.close();
                        // bw.close();
                        // socket.close();
                        // return;
                        // }
                        // heartBeatMap.put(socket, lastTime);
                        // 2.登陆成功启动监听
                        Thread currentThread = Thread.currentThread();
                        while (!currentThread.isInterrupted()) {
                            ProtocolEntity entity = SocketUtils.receive(socket);
                            // 刷新心跳时间
                            HostEntity hostEntity = hostMap.get(Thread.currentThread());
                            hostEntity.setFlushTime(clock.now());
                            // heartBeatMap.put(socket, clock.now());
                            // observer.notifyListener(entity);
                            entity.setIdentityId(hostEntity.getIdentityId());
                            headFilter.handle(entity);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        // 关闭连接
                        try {
                            socket.close();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        // 退出线程
                    }

                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 登陆，成功则返回身份id
    private void login(Socket socket) throws Exception {
        ProtocolEntity entity = SocketUtils.receive(socket);
        ProtocolEntity.Identity identity = entity.getIdentity();

        String identityId = new String();

        // 推送实体
        HostEntity hostEntity = new HostEntity();
        hostEntity.setIdentity(entity.getIdentity());
        // hostEntity.setIdentityId(identityId);以后看看怎么优化
        hostEntity.setName(entity.getSysUserName());

        // 1.登陆
        if (identity.equals(ProtocolEntity.Identity.USER)) {
            // RSA账号密码登陆 1.生成并发送公钥
            KeyPair keyPair = RSAUtils.genKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            byte[] bytes = CommonUtils.ObjectToByteArray(publicKey);
            String base64PublicKey = Base64.encode(bytes);
            entity = new ProtocolEntity();
            entity.setReply(base64PublicKey);
            SocketUtils.send(socket, entity);
            // 2.校验用户名密码
            entity = SocketUtils.receive(socket);
            // 数据库操作（之后结合spirng等修改）
            String userName = RSAUtils.decrypt(entity.getParams().get(0), privateKey);
            String passWord = RSAUtils.decrypt(entity.getParams().get(1), privateKey);
            SqlSession sqlSession = Mybaits.sessionFactory.openSession();
            AuthMapper authMapper = sqlSession.getMapper(AuthMapper.class);
            Auth auth = authMapper.get(userName, passWord);
            if (auth != null) {
                identityId = auth.getId();
                if (socketMap.containsKey(identityId)) {
                    entity = new ProtocolEntity();
                    entity.setReplyState(ProtocolEntity.ReplyState.ERROR);
                    entity.setReply("该用户已登陆，不能重复登陆");
                    SocketUtils.send(socket, entity);
                    throw new RuntimeException("该用户已登陆，不能重复登陆");
                }
                hostEntity.setIdentityId(identityId);
                socketMap.put(identityId, socket);
                threadMap.put(identityId, Thread.currentThread());
                // identityIdMap.put(socket, identityId);
                // identityMap.put(socket.hashCode(), identityId);
                entity = new ProtocolEntity();
                entity.setReplyState(ProtocolEntity.ReplyState.SUCCESE);
                entity.setReply("登陆成功");
                SocketUtils.send(socket, entity);
                logger.info("登陆成功");
            } else {
                entity = new ProtocolEntity();
                entity.setReplyState(ProtocolEntity.ReplyState.ERROR);
                entity.setReply("登陆失败，用户名或密码错误");
                SocketUtils.send(socket, entity);
                logger.info("登陆失败");
                throw new RuntimeException("登陆失败，用户名或密码错误");
            }

        } else if (identity.equals(ProtocolEntity.Identity.CORPSE)) {
            // 1.查找数据库，是否被拉黑，被黑则发送指令退出程序,之后实现

            identityId = entity.getIdentityId();
            if (socketMap.containsKey(identityId)) {
                entity = new ProtocolEntity();
                entity.setReplyState(ProtocolEntity.ReplyState.ERROR);
                entity.setReply("该用户已登陆，不能重复登陆");
                SocketUtils.send(socket, entity);
                throw new RuntimeException("该用户已登陆，不能重复登陆");
            }
            hostEntity.setIdentityId(identityId);
            socketMap.put(identityId, socket);
            threadMap.put(identityId, Thread.currentThread());
            entity = new ProtocolEntity();
            entity.setReplyState(ProtocolEntity.ReplyState.SUCCESE);
            entity.setReply("登陆成功");
            SocketUtils.send(socket, entity);
            logger.info("登陆成功");
        } else {
            // 未知类型，抛异常
        }

        // 缓存用户信息
        hostEntity.setFlushTime(clock.now());
        hostMap.put(Thread.currentThread(), hostEntity);

        // 上线推送
        cachedThreadPool.execute(() -> {
            try {
                HostOnlineEvent hostEvent = new HostOnlineEvent();
                hostEvent.setJson(CommonUtils.gson.toJson(hostEntity));
                PushServer.newInstance().push(hostEvent, hostEntity.getIdentityId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // return identityId;
    }

    // 初始化
    public void init() {
        logger.info("初始化配置...");
        // 1.注册filter，之后改成反射可以扫描注册
        register();
        // 2.rmi服务注册
        rmiServer.autoRegister();
        // 3.开启心跳超时检测，之后看情况还quarz第三方类
        heartBeatCheck();
    }

    private void register() {
        try {
            String str = this.getClass().getResource("").toURI().toString();
            if (str.startsWith("file")) {
                registerByFile();
            } else if (str.startsWith("jar")) {
                registerByJar();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("注册业务异常" + e.getMessage());
        }
    }

    private void registerByFile() {
        logger.info("处理器自动注册...");
        try {
            String scanPath = this.getClass().getResource("").toURI().getPath() + "filter";
            Filter filter = null;
            String packageName = this.getClass().getPackage().getName() + ".filter.";
            File file = new File(scanPath);
            for (File f : file.listFiles()) {
                String fileName = f.getName();
                String packageClassName = packageName + fileName.substring(0, fileName.indexOf("."));
                Filter newFilter = (Filter) Class.forName(packageClassName).newInstance();
                if (headFilter == null) {
                    filter = newFilter;
                    headFilter = filter;
                } else {
                    filter.register(newFilter);
                    filter = newFilter;
                }
                logger.info("注册处理器：" + newFilter.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("注册业务异常" + e.getMessage());
        }

    }

    //可运行jar包环境（暂时没找到jar包的遍历，先手动注册，之后再弄）
    private void registerByJar() {
        if (headFilter != null) {
            return;
        }
        Filter filter=new CommonRequestFilter();
        headFilter=filter;
        logger.info("注册处理器："+filter.getClass().getName());
        filter.register(new ExitFilter());
        filter=filter.filter;
        logger.info("注册处理器："+filter.getClass().getName());
        filter.register(new HeartbeatFilter());
        filter=filter.filter;
        logger.info("注册处理器："+filter.getClass().getName());
        filter.register(new ResponseFilter());
        filter=filter.filter;
        logger.info("注册处理器："+filter.getClass().getName());
        filter.register(new RmiFilter());
        filter=filter.filter;
        logger.info("注册处理器："+filter.getClass().getName());
        filter.register(new RmiRequestFilter());
        filter=filter.filter;
        logger.info("注册处理器："+filter.getClass().getName());
    }

    private void heartBeatCheck() {
        logger.info("开启心跳超时检测");
        cachedThreadPool.execute(() -> {
            try {
                while (true) {
                    long currentTime = clock.now();
                    if ((currentTime - lastTime) < timeout) {
                        Thread.sleep(delayTime);
                        continue;
                    }
                    // Set<Entry<Socket, Long>> set = heartBeatMap.entrySet();
                    // Iterator<Entry<Socket, Long>> it = set.iterator();
                    // List<String> expiredSocketList = new ArrayList<String>();
                    // while (it.hasNext()) {
                    // Entry<Socket, Long> entry = it.next();
                    // long lastHeartBeatTime = entry.getValue();
                    // if ((currentTime - lastHeartBeatTime) > timeout) {
                    // String identityId = identityIdMap.get(entry.getKey());
                    // expiredSocketList.add(identityId);
                    // }
                    // }
                    // outLine(expiredSocketList);
                    // lastTime = currentTime;
                    Set<Map.Entry<Thread, HostEntity>> set = hostMap.entrySet();
                    Iterator<Map.Entry<Thread, HostEntity>> it = set.iterator();
                    List<String> expiredSocketList = new ArrayList<String>();
                    while (it.hasNext()) {
                        Map.Entry<Thread, HostEntity> entry = it.next();
                        HostEntity hostEntity = entry.getValue();
                        long lastHeartBeatTime = hostEntity.getFlushTime();
                        if ((currentTime - lastHeartBeatTime) > timeout) {
                            String identityId = hostEntity.getIdentityId();
                            expiredSocketList.add(identityId);
                        }
                    }
                    outLine(expiredSocketList);
                    lastTime = currentTime;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void outLine(List<String> list) throws Exception {
        for (String identityId : list) {
            outLine(identityId);
        }
    }

    public void outLine(String identityId) throws Exception {
        // 1.关闭socket
        socketMap.get(identityId).close();
        // 2.清理socket相关的集合数据
        clearSocketData(identityId);
        // 3.下线推送
        cachedThreadPool.execute(() -> {
            try {
                HostEntity HostEntity = new HostEntity();
                HostEntity.setIdentityId(identityId);
                String json = CommonUtils.gson.toJson(HostEntity);
                HostOutlineEvent hostOutlineEvent = new HostOutlineEvent();
                hostOutlineEvent.setJson(json);
                PushServer.newInstance().push(hostOutlineEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clearSocketData(String identityId) throws Exception {
        Thread thread = threadMap.get(identityId);
        hostMap.remove(thread);
        threadMap.remove(identityId);
        socketMap.remove(identityId);
        // Socket socket = socketMap.get(identityId);
        // heartBeatMap.remove(socket);
        // identityIdMap.remove(socket);
        // socketMap.remove(identityId);
        logger.info("清理Socket,  identityId=" + identityId);
    }

    public HostEntity getOwnHostEntity() {
        Thread currentThread = Thread.currentThread();
        return hostMap.get(currentThread);
    }

}
