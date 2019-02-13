package com.yzlee.sui.common.modle;

import java.util.List;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 10:50
 */
public class ProtocolEntity {

    public final static String TARGER_SERVER = "TARGER_SERVER";

    public enum Identity {
        USER, CORPSE
    }

    public enum Type {
        COMMONREQUEST, RESPONSE, HEARTBEAT, RMI, RMIREQUEST, PUSH, EXIT
    }

    public enum ReplyState {
        SUCCESE, ERROR
    }

    // public enum Mode {
    // INVOKE, COMMAND
    // }

    // 会话ID
    private String conversationId;
    // 身份 0：用户 2：僵尸
    private Identity identity;
    // 协议类型 0：普通请求 1：回复 2：心跳 3:rmi 4：rmi请求 5：推送 6：断开连接
    private Type type;
    // 全限定类名
    private String className;
    // 方法名称
    private String methodName;
    // 参数类型
    private List<String> paramsType;
    // 参数
    private List<String> params;
    // 回复
    private String reply;
    // 回复状态 0:成功 1：失败
    private ReplyState replyState;
    // 异常信息
    // private String errmsg;
    // 身份ID
    private String identityId;
    // 目标ID
    private String targetId;
    // 系统用户名
    private String sysUserName;
    // 模式 0：调用 1：命令
    // private Mode mode;
    // rmi服务名称
    private String rmiName;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<String> getParamsType() {
        return paramsType;
    }

    public void setParamsType(List<String> paramsType) {
        this.paramsType = paramsType;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public ReplyState getReplyState() {
        return replyState;
    }

    public void setReplyState(ReplyState replyState) {
        this.replyState = replyState;
    }

    // public String getErrmsg() {
    // return errmsg;
    // }
    //
    // public void setErrmsg(String errmsg) {
    // this.errmsg = errmsg;
    // }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    // public Mode getMode() {
    // return mode;
    // }
    //
    // public void setMode(Mode mode) {
    // this.mode = mode;
    // }

    public String getRmiName() {
        return rmiName;
    }

    public void setRmiName(String rmiName) {
        this.rmiName = rmiName;
    }

    @Override
    public String toString() {
        return "ProtocolEntity [conversationId=" + conversationId + ", identity=" + identity + ", type=" + type
                + ", className=" + className + ", methodName=" + methodName + ", paramsType=" + paramsType + ", params="
                + params + ", replyState=" + replyState + ", identityId=" + identityId + ", targetId=" + targetId
                + ", sysUserName=" + sysUserName + ", rmiName=" + rmiName + "]";
    }

}
