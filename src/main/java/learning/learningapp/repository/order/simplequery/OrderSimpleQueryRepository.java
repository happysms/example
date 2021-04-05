package learning.learningapp.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

// 조회 전용으로 화면에만 맞춰 쓰는 용도의 레포지토리
@RequiredArgsConstructor
@Repository
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new learning.learningapp.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status , d.address) from Order o" +
                " join o.member m" +
                " join o.delivery d" , OrderSimpleQueryDto.class)
                .getResultList();
    }
}
