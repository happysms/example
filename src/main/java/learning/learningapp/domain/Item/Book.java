package learning.learningapp.domain.Item;

import learning.learningapp.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@DiscriminatorValue("B")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

public class Book extends Item {
    private String author;
    private String isbn;
}
