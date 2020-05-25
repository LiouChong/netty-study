package com.company.netty.order.server;

import java.io.Serializable;

/**
 * Author: Liuchong
 * Description:
 * date: 2020/5/25 0025 下午 8:53
 */
public class SubscribeResp implements Serializable {
    private static final long serialVersionUID = 1L;
    private int subReqID;
    private int respCode;
    private String desc;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"subReqID\":")
                .append(subReqID);
        sb.append(",\"respCode\":")
                .append(respCode);
        sb.append(",\"desc\":\"")
                .append(desc).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
