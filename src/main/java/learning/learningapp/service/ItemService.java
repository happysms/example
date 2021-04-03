package learning.learningapp.service;

import learning.learningapp.domain.Item.Book;
import learning.learningapp.domain.Item.Item;
import learning.learningapp.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional //변경감지 기능 사용
    public Item updateItem(Long itemId , String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

        // 영속성 컨텍스트 위에 존재하기 때문에 트랜젝션에 의해 커밋이 자동으로 됨 .
        // 변경된 사항을 찾아 flush 를 날림.

        return findItem;
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
