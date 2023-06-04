package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        orderRepository.addPartner(partnerId);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) throws OrderNotFound, DeliveryPartnerNotFound {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> deliveryPartnerOpt = orderRepository.getDeliveryPartner(partnerId);
        if(orderOpt.isEmpty()) {
            throw new OrderNotFound(orderId);
        }
        if(deliveryPartnerOpt.isEmpty()) {
            throw new DeliveryPartnerNotFound(partnerId);
        }
        List<String> order = orderRepository.getOrdersOfPartner(partnerId);
        order.add(orderId);
        DeliveryPartner partner = deliveryPartnerOpt.get();
        partner.setNumberOfOrders(order.size());
        orderRepository.addPartners(partner);
        orderRepository.unAssignedOrder(orderId);
        orderRepository.addOrderPartnerPair(partnerId, order);
    }

    public Order getOrderById(String orderId) {
        Optional<Order> orderOpt = orderRepository.getOrderById(orderId);
        if(orderOpt.isPresent()) {
            return orderOpt.get();
        }
        throw new OrderNotFound(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        Optional<DeliveryPartner> deliveryPartnerOpt = orderRepository.getPartnerById(partnerId);
        if(deliveryPartnerOpt.isPresent()) {
            return deliveryPartnerOpt.get();
        }
        throw new DeliveryPartnerNotFound(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Optional<DeliveryPartner> deliveryPartnerOpt = orderRepository.getDeliveryPartner(partnerId);
        if(deliveryPartnerOpt.isPresent()) {
            return deliveryPartnerOpt.get().getNumberOfOrders();
        }
        return 0;
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
        return orderRepository.getCountOfUnassignedOrders();
    }

    private int timeStringToInteger(String time) {
        String[] timeArr = time.split(":");
        return Integer.parseInt(timeArr[0])* 60 + Integer.parseInt(timeArr[1]);
    }

    private String timeIntegerToString(int time) {
        int min = time % 60;
        int hr = time / 60;

        String hours;
        if(hr < 10) {
            hours = "0"+ Integer.toString(hr);
        } else {
            hours = Integer.toString(hr);
        }

        String minutes;
        if(min < 10) {
            minutes = "0"+ Integer.toString(min);
        } else {
            minutes = Integer.toString(min);
        }
        return hours + ":" + minutes;
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {

        int countOfOrders = 0;
        List<String> orders = orderRepository.getOrdersOfPartner(partnerId);
        if(orders.isEmpty()) return 0;
        int endTime = timeStringToInteger(time);
        for(String order : orders) {
            Order order1 = orderRepository.getOrderById(order).get();
            if(order1.getDeliveryTime() > endTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orders = orderRepository.getOrdersOfPartner(partnerId);
        int time = Integer.MIN_VALUE;
        String ansTime = "00:00";
        if(orders.isEmpty()) return ansTime;
        for(String order : orders) {
            Order order1 = orderRepository.getOrderById(order).get();
            time = Math.max(time, order1.getDeliveryTime());
        }
        ansTime = timeIntegerToString(time);
        return ansTime;
    }

    public void deletePartnerById(String partnerId) {
        orderRepository.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) {
        orderRepository.deleteOrderById(orderId);
    }
}
