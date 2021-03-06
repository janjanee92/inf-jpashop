package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        //  given
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10, "hello", "123");

        int orderCount = 2;

        //  when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //  then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 제고가 줄어야 함.");
    }

    @Test
    void 주문취소() {
        //  given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10, "hello", "123");

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //  when
        orderService.cancelOrder(orderId);

        //  then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문 상태는 CANCEL이 되어야 함.");
        assertEquals(10, book.getStockQuantity(), "재고가 복구되어야 함.");
    }
    
    @Test
    void 상품주문_재고수량초과() {
        //  given
        Member member = createMember();
        Item item = createBook("JPA", 10000, 10, "hello", "123");

        //  when
        int orderCount = 11;

        //  then
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));
    }

    private Book createBook(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = Book.createBook(name, price, stockQuantity, author, isbn);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}