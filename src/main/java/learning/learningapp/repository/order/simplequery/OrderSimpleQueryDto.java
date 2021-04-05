package learning.learningapp.repository.order.simplequery;

import learning.learningapp.domain.Address;
import learning.learningapp.domain.Order;
import learning.learningapp.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto{
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;  // xxxx Lazy 초기화 영속성 컨텍스트가 memberId 로 전체 영속성 컨텍스트를 찾아본다. 만약 없다면 DB에 쿼리를 보낸다.
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}