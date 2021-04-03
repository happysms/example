package learning.learningapp.controller;

import learning.learningapp.domain.Item.Book;
import learning.learningapp.domain.Item.Item;
import learning.learningapp.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form" , new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm form, BindingResult result){

        if (result.hasErrors()) {
            return "items/createItemForm";
        }

        Book book = new Book();

        // setter를 모두 제거하고 createBook 이라는 함수를 만들어서 설정하는 것이 더 best

        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());
        book.setStockQuantity(form.getStockQuantity());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items = itemService.findItems();
        model.addAttribute("items" , items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());


        model.addAttribute("form" , form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId , @ModelAttribute("form") BookForm form) {

//        Book book = new Book();             //일반적으로 JPA가 관리하는 객체가 아님.
//        book.setIsbn(form.getIsbn());
//        book.setName(form.getName());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setPrice(form.getPrice());
//        book.setId(form.getId());

        itemService.updateItem(itemId, form.getName() , form.getPrice() , form.getStockQuantity()); // 필요한 데이터만 받는다.


        // 변경데이터가 많다면 DTO를 만든다.

        return "redirect:/items";
    }
}







