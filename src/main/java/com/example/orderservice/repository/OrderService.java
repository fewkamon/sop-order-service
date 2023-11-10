package com.example.orderservice.repository;

import com.example.orderservice.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    public OrderService(OrderRepository repository){
        this.repository = repository;
    }

    public Order createOrder(Order newOrder){
        try{
            return repository.save(newOrder);
        }catch (Exception error){
            System.out.println(error);
            return null;
        }
    }

    public ArrayList<Order> getOrders(){
        return (ArrayList<Order>) repository.findAll();
    }

    public Order getOrderByOrderID(String orderID){
        Optional<Order> result = repository.findById(orderID);

        if(result.isPresent()) return result.get();

        return null;
    }

    public ArrayList<Order> getOrdersByCustomerID(String customerID){
        Optional<ArrayList<Order>> result = repository.findByCustomerID(customerID);

        if(result.isPresent()) return result.get();

        return null;
    }

    public Order updateOrder(String orderID, Order toUpdateOrder){
        Optional<Order> foundOrder = repository.findById(orderID);

        if(foundOrder.isPresent()){
            foundOrder.get().setCustomer_id(toUpdateOrder.getCustomer_id());
            foundOrder.get().setTotal_cost(toUpdateOrder.getTotal_cost());

            if(!(toUpdateOrder.getProducts() == null)){
                System.out.println("User want to make change on product too");
                foundOrder.get().setProducts(toUpdateOrder.getProducts());
            }

            repository.save(foundOrder.get());
            return foundOrder.get();
        }

        return null;
    }

    public Boolean deleteOrder(String toDeleteOrderID){
        try{
            long before = repository.count();
            repository.deleteById(toDeleteOrderID);
            long after = repository.count();
            if(before - after == 1) return true;

            return false;

        }catch (Exception error){
            System.out.println(error);
            return false;
        }

    }
}