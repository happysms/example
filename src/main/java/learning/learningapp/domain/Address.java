package learning.learningapp.domain;

import lombok.Getter;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;

    private String street;

    private String zipcode;

    protected Address() { } //리플렉션이나 프록시 같은 기술을 쓰기위해서는 기본생성자가 필요함. but public 으로 선언하면 외부에서 쉽게 접근할 수 있으니 protected로 선

    public Address(String city, String street, String zipcode) {  // 생성시에만 초기화 가능 Setter 대신 선
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
