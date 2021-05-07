package com.example.sanjeevaniadmin.Models;

import java.util.Date;
import java.util.List;

public class OrderModel {
    private String orderId;
    private String orderStatus;
    private Date orderedDate;
    private Date packedDate;
    private Date shippeddate;
    private Date deliveredDate;
    private Date cancelledDate;
    private String paymentStatus;
    private String paymentMethod;
    private long totalItem;
    private long totalAmount;
    private String address;
    private String fullName;
    private String pincode;
    private List<MyOrderItemModel> myOrderItemModelList;

    public OrderModel(String orderId, String orderStatus, Date orderedDate, Date packedDate, Date shippeddate, Date deliveredDate, Date cancelledDate, String paymentStatus, String paymentMethod, long totalItem, long totalAmount, String address, String fullName, String pincode, List<MyOrderItemModel> myOrderItemModelList) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippeddate = shippeddate;
        this.deliveredDate = deliveredDate;
        this.cancelledDate = cancelledDate;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.totalItem = totalItem;
        this.totalAmount = totalAmount;
        this.address = address;
        this.fullName = fullName;
        this.pincode = pincode;
        this.myOrderItemModelList = myOrderItemModelList;
    }

    public List<MyOrderItemModel> getMyOrderItemModelList() {
        return myOrderItemModelList;
    }

    public void setMyOrderItemModelList(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippeddate() {
        return shippeddate;
    }

    public void setShippeddate(Date shippeddate) {
        this.shippeddate = shippeddate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Date getCancelledDate() {
        return cancelledDate;
    }

    public void setCancelledDate(Date cancelledDate) {
        this.cancelledDate = cancelledDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public long getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(long totalItem) {
        this.totalItem = totalItem;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
