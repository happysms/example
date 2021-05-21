package learning.learningapp.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {

    private Long orderId;  //꼭 없어도 됨.
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
