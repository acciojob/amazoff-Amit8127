package com.driver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private OrderRepository orderRepository = new OrderRepository();

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        List<String> order = orderRepository.getOrdersOfPartner(partnerId);
        order.add(orderId);
        DeliveryPartner deliveryPartner = orderRepository.getDeliveryPartner(partnerId);
        deliveryPartner.setNumberOfOrders(order.size());
        orderRepository.addDeliveryPartners(partnerId, deliveryPartner);
        orderRepository.addOrderPartnerPair(partnerId, order);
    }

    public Order getOrderById(String orderId) {
        Order order = orderRepository.getOrderById(orderId);
        return order;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        DeliveryPartner deliveryPartner = orderRepository.getPartnerById(partnerId);
        return deliveryPartner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        DeliveryPartner deliveryPartner = orderRepository.getDeliveryPartner(partnerId);
        return deliveryPartner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> ordersListOfPartner = orderRepository.getOrdersByPartnerId(partnerId);
        return ordersListOfPartner;
    }

    public List<String> getAllOrders() {
        List<String> allOrders = orderRepository.getAllOrders();
        return allOrders;
    }

    public Integer getCountOfUnassignedOrders() {
        List<String> partnerListOfOrders = orderRepository.getPartnersOfOrders();
        List<String> orders = orderRepository.getAllOrders();
        int count = 0;
        for(String partner : partnerListOfOrders) {
            List<String> orderList = orderRepository.getOrdersOfPartner(partner);
            for(String order : orderList) {
                if(orders.contains(order)) {
                    count++;
                }
            }
        }
        return orders.size() - count;
    }

    private int timeStringToInteger(String time) {
        String[] timeArr = time.split(":");
        return Integer.parseInt(timeArr[0])* 60 + Integer.parseInt(timeArr[1]);
    }

    private String timeIntegerToString(int time) {
        int min = time % 60;
        int hr = time / 60;
        return Integer.toString(hr) + ":" + Integer.toString(min);
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int countOfOrders = 0;
        List<String> orders = orderRepository.getOrdersOfPartner(partnerId);
        int endTime = timeStringToInteger(time);
        for(String order : orders) {
            Order order1 = getOrderById(order);
            if(order1.getDeliveryTime() > endTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders = orderRepository.getOrdersOfPartner(partnerId);
        int time = 0;
        for(String order : orders) {
            Order order1 = orderRepository.getOrderById(order);
            time = Math.max(time, order1.getDeliveryTime());
        }
        return timeIntegerToString(time);
    }

    public void deletePartnerById(String partnerId) {
        List<String> orders = orderRepository.getOrdersOfPartner(partnerId);
        for(String order : orders) {
            Order order1 = orderRepository.getOrderById(order);
            orderRepository.addOrder(order1);
        }
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        List<String> partnerListOfOrders = orderRepository.getPartnersOfOrders();
        for(String partner : partnerListOfOrders) {
            List<String> orders = orderRepository.getOrdersOfPartner(partner);
            int idx = -1;
            for(int i = 0; i < orders.size(); i++) {
                if(orders.get(i).equals(orderId)) {
                    idx = i;
                    break;
                }
            }
            if(idx != -1) {
                orders.remove(idx);
                orderRepository.addOrderPartnerPair(partner, orders);
                orderRepository.removeOrderById(orderId);
                return;
            }
        }
    }
}
