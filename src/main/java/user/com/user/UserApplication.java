package user.com.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.context.ApplicationContext;


//import task3.com.task3.Model.User;
@SpringBootApplication
//@EnableSwagger2
 @EnableConfigurationProperties
public class UserApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext =  SpringApplication.run(UserApplication.class, args);
	}
}
