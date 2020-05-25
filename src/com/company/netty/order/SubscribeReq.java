package com.company.netty.order;

import java.io.Serializable;

/**
 * Author: Liuchong
 * Description: 订购请求类
 * date: 2020/5/25 0025 下午 8:51
 */
public class SubscribeReq implements Serializable {
    private static final long serialVersionUID = 1L;
    private int subReqID;
    private String userName;
    private String productName;
    private String phoneName;
    private String address;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(int subReqID) {
        this.subReqID = subReqID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"subReqID\":")
                .append(subReqID);
        sb.append(",\"userName\":\"")
                .append(userName).append('\"');
        sb.append(",\"productName\":\"")
                .append(productName).append('\"');
        sb.append(",\"phoneName\":\"")
                .append(phoneName).append('\"');
        sb.append(",\"address\":\"")
                .append(address).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
