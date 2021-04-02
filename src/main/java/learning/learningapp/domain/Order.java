package learning.learningapp.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //주문 회원  외래키 , 값을 변경하면 다른 멤버로 변경이 된다.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  // Cascade로 order만 persist하면 arderitem도 모두 persist한다.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //배송정보

    private LocalDateTime orderDate; //주문시간 @Enumerated(Enum Type.STRING)

    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // == 생성 메서드 == // 밖애서 order에 포함된 객체를 set하는 것이 아니라 생성할 때부터 내부 객체의 값을 set한다.
    public static Order createOrder(Member member, Delivery delivery , OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문 취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품을 취소가 불가능 합니다. ");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // == 조회 로직 == //

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        int total = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return total;
    }

}