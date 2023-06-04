package com.driver;

import java.util.*;

public class OrderRepository {

    private Map<String, Order> orders = new HashMap<>();
    private Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    private Map<String, List<String>> ordersOfPartner = new HashMap<>();
    public void addOrder(Order order) {
        orders.put(order.getId(), order);
    }

    public void addPartner(String partnerId) {
        deliveryPartners.put(partnerId, new DeliveryPartner(partnerId));
    }

    public List<String> getOrdersOfPartner(String partnerId) {
        return ordersOfPartner.getOrDefault(partnerId, new ArrayList<>());
    }

    public Optional<DeliveryPartner> getDeliveryPartner(String partnerId) {
        if(deliveryPartners.containsKey(partnerId)) {
            return Optional.of(deliveryPartners.get(partnerId));
        }
        return Optional.empty();
    }
    public void addOrderPartnerPair(String partnerId, List<String> order) {
        ordersOfPartner.put(partnerId, order);
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orders.containsKey(orderId)) {
            return Optional.of(orders.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(deliveryPartners.containsKey(partnerId)) {
            return Optional.of(deliveryPartners.get(partnerId));
        }
        return Optional.empty();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return ordersOfPartner.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orders.keySet());
    }

    public Optional<String> getPartnersOfOrders(String orderId) {
        if(ordersOfPartner.containsKey(orderId)){
            Optional.of(ordersOfPartner.get(orderId));
        }
        return Optional.empty();
    }

    public void deletePartnerById(String partnerId) {
        deliveryPartners.remove(partnerId);
        ordersOfPartner.remove(partnerId);
    }

    public void addPartners(DeliveryPartner partner) {
        deliveryPartners.put(partner.getId(), partner);
    }

    public void deleteOrder(String orderId) {
        orders.remove(orderId);
    }

    public List<String> getPartnersListOrders() {
        return new ArrayList<>(ordersOfPartner.keySet());
    }
}
