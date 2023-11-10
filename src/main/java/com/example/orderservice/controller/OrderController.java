package com.example.orderservice.controller;

import com.example.orderservice.pojo.Order;
import com.example.orderservice.repository.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    /* ------------- Create ------------- */

    // Create order
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder){
        Order result = orderService.createOrder(newOrder);
        return ResponseEntity.ok(result);
    }

    /* ------------- Read ------------- */

    // Get orders
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Order>> getOrders(){
        ArrayList<Order> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }

    // Get order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.GET)
    public ResponseEntity<Order> getOrderByOrderID(@PathVariable("orderID") String orderID){
        return ResponseEntity.ok(orderService.getOrderByOrderID(orderID));
    }

    // Get orders (By user ID)
    @RequestMapping(value = "/orders/users/{customerID}", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Order>> getOrdersByCustomerID(@PathVariable("customerID") String customerID){
        return ResponseEntity.ok(orderService.getOrdersByCustomerID(customerID));
    }

    /* ------------- Update ------------- */

    // Update order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.PUT)
    public ResponseEntity<Order> updateOrder(@PathVariable("orderID") String orderID, @RequestBody Order toUpdatePayload){
        Order updateResult = orderService.updateOrder(orderID, toUpdatePayload);
        return ResponseEntity.ok(updateResult);
    }

    /* ------------- Delete ------------- */

    // Delete order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderID") String orderID){
        Boolean deletedResult = orderService.deleteOrder(orderID);
        return  ResponseEntity.ok(deletedResult);
    }

}
