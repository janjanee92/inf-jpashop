package jpabook.jpashop.domain.item;

import jpabook.jpashop.dto.UpdateItemDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends Item {
    private String author;
    private String isbn;

    //==생성 메소드==//

    public static Book createBook(String name, int price, int stockQuantity, String author, String isbn) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);

        return book;
    }

    //==비즈니스 로직==//

    @Override
    public void change(UpdateItemDTO dto) {
        super.change(dto);
        this.author = dto.getAuthor();
        this.isbn = dto.getIsbn();
    }

}
