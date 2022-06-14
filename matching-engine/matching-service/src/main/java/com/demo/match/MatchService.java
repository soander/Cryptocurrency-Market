package com.demo.match;

import com.demo.model.Order;
import com.demo.model.OrderBooks;

public interface MatchService {

    // Matching service
    void match(OrderBooks orderBooks, Order order);
}
