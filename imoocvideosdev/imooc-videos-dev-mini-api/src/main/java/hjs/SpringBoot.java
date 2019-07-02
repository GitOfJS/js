package hjs;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


@SpringBootApplication
@MapperScan(value = "hjs.mapper")
@ComponentScan(value = {"org.n3r.idworker","hjs"})
public class SpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(SpringBoot.class,args);
    }
}
