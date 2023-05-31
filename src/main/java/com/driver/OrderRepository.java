package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {

    private Map<String, Order> orders = new HashMap<>();
    private Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    private Map<String, List<String>> ordersOfPartner = new HashMap<>();
    public void addOrder(Order order) {
        if(!orders.containsKey(order.getId())) {
            orders.put(order.getId(), order);
        }
    }

    public void addPartner(String partnerId) {
        deliveryPartners.put(partnerId, new DeliveryPartner(partnerId));
    }

    public List<String> getOrdersOfPartner(String partnerId) {
        return ordersOfPartner.getOrDefault(partnerId, new ArrayList<>());
    }

    public DeliveryPartner getDeliveryPartner(String partnerId) {
        return deliveryPartners.get(partnerId);
    }

    public void addDeliveryPartners(String partnerId, DeliveryPartner deliveryPartner) {
        deliveryPartners.put(partnerId, deliveryPartner);
    }

    public void addOrderPartnerPair(String partnerId, List<String> order) {
        ordersOfPartner.put(partnerId, order);
    }

    public Order getOrderById(String orderId) {
        return orders.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartners.get(partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return ordersOfPartner.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    public List<String> getPartnersOfOrders() {
        return new ArrayList<>(ordersOfPartner.keySet());
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartners.remove(partnerId);
        ordersOfPartner.remove(partnerId);
    }

    public void removeOrderById(String orderId) {
        orders.remove(orderId);
    }
}
