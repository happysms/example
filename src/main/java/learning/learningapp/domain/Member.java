package learning.learningapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")  // DB 컬럼 명 설정
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    //@JsonIgnore - 엔티티에 프레젠테이션을 위한 것이 있으면 안된다. - 수정의 어려움 존재

    @OneToMany(mappedBy = "member")
    @JsonIgnore // 양방향 연관관계에서는 둘 중 하나를 JsonIgnore 를 해주어야한다.
    private List<Order> orders = new ArrayList<>(); // 컬렉션은 필드에서 바로 초기화하는 것이 안전하다. (null 문제로부터)
}











