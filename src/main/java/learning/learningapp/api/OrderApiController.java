package learning.learningapp.api;

import learning.learningapp.domain.Address;
import learning.learningapp.domain.Order;
import learning.learningapp.domain.OrderItem;
import learning.learningapp.domain.OrderStatus;
import learning.learningapp.repository.OrderRepository;
import learning.learningapp.repository.OrderSearch;
import learning.learningapp.repository.order.query.OrderFlatDto;
import learning.learningapp.repository.order.query.OrderItemQueryDto;
import learning.learningapp.repository.order.query.OrderQueryDto;
import learning.learningapp.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
private final OrderQueryRepository orderQueryRepository;

    //엔티티를 그대로 노출
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllbyString(new OrderSearch());
        for (Order order : all) {
              order.getMember().getName();
              order.getDelivery().getAddress();
              List<OrderItem> orderItems = order.getOrderItems();
              for (OrderItem orderItem : orderItems) {
                  orderItem.getItem().getName();   // 프록시를 강제 초기화
              }
              // orderItems.stream().forEach(o -> o.getItem().getName()); 로도 표현 가능
        }
        return all;
    }


    //Dto 안에 엔티티가 있기에 적절하지 않음.
    // 엔티티의 의존을 끊어버려야함.
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2(){
        List<Order> orders = orderRepository.findAllbyString(new OrderSearch());

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3 () {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            System.out.println("order ref = "  + order +  " id = " + order.getId());   // 뻥 튀기된 order의 주소값이 같아진다.
        }

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset" , defaultValue = "0") int offset,
                                        @RequestParam(value = "limit" , defaultValue = "100") int limit) { //서버에서 요청

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return collect;
    }

    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){   // 컬렉션을 dto로 반환 N+1 문제 발
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDtoOptimization();
    }

//    @GetMapping("/api/v6/orders")
//    public List<OrderQueryDto> ordersV6(){
//        List<OrderFlatDto> flats = orderQueryRepository.findAllByDtoFlat();
//
////        return flats.stream()
////                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
////                                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
////                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
////                                o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
////                )).entrySet().stream()
////                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
////                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
////                        e.getKey().getAddress(), e.getValue()))
////                .collect(toList());
//    }




    @Getter   // no properties 에러가 발생할 시에는 getter or Data 애노테이션을 붙혀준다.
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;   //이와 같은 value 오브젝트 같은 경우는 그대로 노출해도 괜찮다.
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();

//            order.getOrderItems().stream().forEach(o -> o.getItem().getName());  // 프록시를 초기화하면 값이 나온다.
//
//            orderItems = order.getOrderItems();    // 엔티티이기에 값이 전달되어지지 않는다.

            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName;  //상품명
        private int orderPrice; //주문 가격
        private int count; // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
