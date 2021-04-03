package learning.learningapp.controller;

import learning.learningapp.domain.Item.Item;
import learning.learningapp.domain.Member;
import learning.learningapp.domain.Order;
import learning.learningapp.repository.OrderRepository;
import learning.learningapp.repository.OrderSearch;
import learning.learningapp.service.ItemService;
import learning.learningapp.service.MemberService;
import learning.learningapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final MemberService memberService;

    private final ItemService itemService;

    private final OrderRepository orderRepository;

    @GetMapping("/order")
    public String createForm(Model model){

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members" , members);
        model.addAttribute("items" , items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId ,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch , Model model) {
        List<Order> orders = orderRepository.findAllbyString(orderSearch);
        model.addAttribute("orders" ,orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancel(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
