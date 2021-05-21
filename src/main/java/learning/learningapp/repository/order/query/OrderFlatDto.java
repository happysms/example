package learning.learningapp.repository.order.query;

import learning.learningapp.domain.Address;
import learning.learningapp.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 페이징이 불가능
@Data
public class OrderFlatDto {  // order 와 orderItem 을 조인한 것
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
