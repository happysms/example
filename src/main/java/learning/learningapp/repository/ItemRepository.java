package learning.learningapp.repository;

import learning.learningapp.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if (item.getId() == null){ //ITEM 은 JPA에 저장하기 전까지 id 값이 없다. 그렇기에 신규 등록
            em.persist(item);
        }else{
            em.merge(item);  // 업데이트와 비슷한 맥락 하지만 입력이 안된 필드가 있다면 null 로 업데이트 될 가능성이 있다. 대부분 변경감지가 더 낫다.
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i" , Item.class)
                .getResultList();
    }
}
