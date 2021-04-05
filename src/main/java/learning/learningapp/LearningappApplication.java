package learning.learningapp;


import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LearningappApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningappApplication.class, args);
    }


    // 이런 방법이 있는데 할필요는 없는 듯..
    @Bean
    Hibernate5Module hibernate5Module(){
        Hibernate5Module hibernate5Module = new Hibernate5Module();
//        hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);  //엔티티를 그대로 노출시키는 방법 - 좋지 않은 방법


        return hibernate5Module;
    }
}
