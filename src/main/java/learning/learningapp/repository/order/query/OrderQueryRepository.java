package learning.learningapp.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//쿼리쪽 레퍼지토리는 화면 혹은 API에 의존관계가 있는 것을 띄워내기 위함.
@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new learning.learningapp.repository.order.query.OrderItemQueryDto(oi.order.id , i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i"
                + " where oi.order.id = :orderId" , OrderItemQueryDto.class)
                .setParameter("orderId" , orderId)
                .getResultList();

    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new learning.learningapp.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status , d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" , OrderQueryDto.class)
                .getResultList();
    }


    // 쿼리가 총 2번 실행됨.
    public List<OrderQueryDto> findAllByDtoOptimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = result.stream().
                map(o -> o.getOrderId())
                .collect(Collectors.toList());

        //쿼리를 한번 보냄.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

                // 값 세팅.
                result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new learning.learningapp.repository.order.query.OrderItemQueryDto(oi.order.id , i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i"
                        + " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // 메모리에서 모두 매칭을 함.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    public List<OrderFlatDto> findAllByDtoFlat() {
        return em.createQuery(
                "select new " +
                        " learning.learningapp.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status , d.address , i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
