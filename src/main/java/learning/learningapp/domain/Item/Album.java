package learning.learningapp.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("A")
public class Album  extends Item {
    private String artist;
    private String etc;
}
