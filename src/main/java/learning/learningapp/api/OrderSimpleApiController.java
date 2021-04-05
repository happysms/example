package learning.learningapp.api;

import learning.learningapp.domain.Address;
import learning.learningapp.domain.Order;
import learning.learningapp.domain.OrderStatus;
import learning.learningapp.repository.OrderRepository;
import learning.learningapp.repository.OrderSearch;
import learning.learningapp.repository.order.simplequery.OrderSimpleQueryDto;
import learning.learningapp.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
/**
 * XToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //orderV1, V2가 가진 문제점은 lazy loading으로 너무 많은 쿼리가 실행됨.

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllbyString(new OrderSearch()); //new OrderSearch()를 row하게 넘기면 검색조건이 없기에 모두다 나옴.

        for (Order order : all){
            order.getMember().getName(); // Lazy 강제 초기화  //order.getMember()까지는 프록시 객체 getName까지하면 쿼리가 실행됨.
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }

        return all;
    }

    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // ORDER 2개
        // N + 1 -> 회원 N + 배송 N

        List<Order> orders = orderRepository.findAllbyString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();  // Lazy 초기화 영속성 컨텍스트가 memberId 로 전체 영속성 컨텍스트를 찾아본다. 만약 없다면 DB에 쿼리를 보낸다.
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

    @GetMapping("api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3(){  //v4에 비해 성능은 부족하지만 재사용성이 좋음.
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")  //재사용에 어려움이 있음.
    public List<OrderSimpleQueryDto> orderV4(){
        return orderRepository.findOrderDtos();
    }
}

/**
 * -쿼리 방식 선택 권장 순서-
 * 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다. -> 유지보수 측면에서 좋음.
 * 2. 필요하면 페치 조인으로 성능을 최적화한다. -> 대부분의 성능 이슈가 해결된다.
 * 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
 * 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL 이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
 */