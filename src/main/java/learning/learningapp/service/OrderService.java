package learning.learningapp.service;

import learning.learningapp.domain.Delivery;
import learning.learningapp.domain.Item.Item;
import learning.learningapp.domain.Member;
import learning.learningapp.domain.Order;
import learning.learningapp.domain.OrderItem;
import learning.learningapp.repository.ItemRepository;
import learning.learningapp.repository.MemberRepository;
import learning.learningapp.repository.OrderRepository;
import learning.learningapp.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final MemberRepository memberRepository;

    private final ItemRepository itemRepository;
    /***
     * 주문
     */
    @Transactional
    public Long order(Long memberId , Long itemId , int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long  orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 조회
        order.cancel();
    }


//    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }

}
