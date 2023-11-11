package com.example.orderservice.controller;

import com.example.orderservice.pojo.ErrorResponse;
import com.example.orderservice.pojo.Order;
import com.example.orderservice.pojo.Product;
import com.example.orderservice.repository.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    /* ------------- Create ------------- */

    // Create order
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public ResponseEntity<?> createOrder(@RequestBody Order newOrder){
        try {
            this.validateOrderData(newOrder);
            Order result = orderService.createOrder(newOrder);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot create new order", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot create new order", e.getMessage()));
        }
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
    public ResponseEntity<?> updateOrder(@PathVariable("orderID") String orderID, @RequestBody Order toUpdatePayload){
        try {
            this.validateOrderData(toUpdatePayload);
            Order updateResult = orderService.updateOrder(orderID, toUpdatePayload);
            if (updateResult == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Cannot update order", "orderID not found"));
            }
            return ResponseEntity.ok(updateResult);
        } catch (IllegalArgumentException ie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Cannot update order", ie.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Cannot update order", e.getMessage()));
        }
    }

    /* ------------- Delete ------------- */

    // Delete order
    @RequestMapping(value = "/orders/{orderID}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteOrder(@PathVariable("orderID") String orderID){
        Boolean deletedResult = orderService.deleteOrder(orderID);
        return  ResponseEntity.ok(deletedResult);
    }

    public void validateOrderData(Order order) throws Exception{
        if (order.getTotal_cost() < 0) {
            throw new IllegalArgumentException("Total price must be greater than Zero or equal to Zero");
        }
        if (order.getProducts().size() == 0) {
            throw new IllegalArgumentException("Products must have at least 1 product");
        }
        for (Product product : order.getProducts()) {
            if (product.getPrice() < 0) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Price must be greater than or equal to Zero");
            }
            if (product.getName() == null || product.getName().isBlank()) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Name is Required");
            }
            if (product.getDescription() == null || product.getDescription().isBlank()) {
                throw new IllegalArgumentException("Invalid Product \uD83D\uDD34, Description is Required");
            }
        }
    }
}
