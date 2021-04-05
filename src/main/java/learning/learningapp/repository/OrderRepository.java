package learning.learningapp.repository;

import learning.learningapp.domain.Order;
import learning.learningapp.repository.order.simplequery.OrderSimpleQueryDto;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {

        return em.find(Order.class, id);
    }

    public List<Order> findAllbyString(OrderSearch orderSearch){ //... 검색 로직

        //language=JPQL
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;
    //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }


    //fetch
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member" +
                        " join fetch o.delivery d" ,Order.class
        ).getResultList();
    }


    //API 스펙에 의존적이어서 API스펙이 바뀌면 코드를 수정해야함 -> 재사용성이 떨어짐.
    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select new learning.learningapp.repository.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status , d.address) from Order o" +
                    " join o.member m" +
                    " join o.delivery d" , OrderSimpleQueryDto.class)
                    .getResultList();
            }


    public List<Order> findAllWithItem() {
        return em.createQuery(
                // 일대다를 페치 조인하면 페이징이 불가능하다.
                "select distinct o from Order o" +    //distinct는 뻥튀기 된 객체를 원복해줌  but 데이터베이스에서는 한줄이 완벽하게 같아야지만 중복을 제거한다.
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i" , Order.class)
                .getResultList();
    }

}