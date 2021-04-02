package learning.learningapp.repository;

import learning.learningapp.domain.Order;
import learning.learningapp.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSearch {

    private String memberName;

    private OrderStatus orderStatus; // 주문 상태(Order, Cancel)



}
