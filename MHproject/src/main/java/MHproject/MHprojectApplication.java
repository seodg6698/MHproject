package MHproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"MHproject"})
@MapperScan("MHproject.mapper")
public class MHprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MHprojectApplication.class, args);
	}

}
