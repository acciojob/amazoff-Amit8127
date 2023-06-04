package com.driver;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {

    private Map<String, Order> orders = new HashMap<>();
    private Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();
    private Map<String, List<String>> ordersOfPartner = new HashMap<>();
    private Set<String> assingedOrders = new HashSet<>();
    public void addOrder(Order order) {
        orders.put(order.getId(), order);
        assingedOrders.add(order.getId());
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

    public void deletePartnerById(String partnerId) {
        if(!ordersOfPartner.isEmpty()) {
            assingedOrders.addAll(ordersOfPartner.get(partnerId));
        }
        deliveryPartners.remove(partnerId);
        ordersOfPartner.remove(partnerId);
    }

    public void addPartners(DeliveryPartner partner) {
        deliveryPartners.put(partner.getId(), partner);
    }

    public void unAssignedOrder(String orderId) {
        assingedOrders.remove(orderId);
    }

    public void deleteOrderById(String orderId) {
        if(orders.containsKey(orderId)){
            if(assingedOrders.contains(orderId)){
                assingedOrders.remove(orderId);
            }
            else{
                for(String str : ordersOfPartner.keySet()){
                    List<String> list = ordersOfPartner.get(str);
                    if(list.contains(orderId)){
                        list.remove(orderId);
                    }
                }
            }
        }
    }

    public Integer getCountOfUnassignedOrders() {
        return assingedOrders.size();
    }
}
