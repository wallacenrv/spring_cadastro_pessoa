package curso.springboot.springboo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages ={"curso.springboot.springboo.model"} )
@SpringBootApplication
@ComponentScan(basePackages = {"curso.*"})// forca mapear todos os pacotes
@EntityScan(basePackages = "curso.springboot.springboo.model")// Faz o scaneamento das entidades
@EnableWebMvc // para criar tela de login , habilitar todos os RECURSOS de mvc
public class SpringbooApplication implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbooApplication.class, args);


    }

    // rESPONSAVEL PELA TELA DE LOGIN
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("/login");//vai interceptar o login e mandar para a telade login
        registry.setOrder(Ordered.LOWEST_PRECEDENCE);


        //parte 1
       // public static void main(String[] args) {
     //       SpringApplication.run(SpringbooApplication.class, args);
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//			String result = encoder.encode("123"); // QUERO CODIFICAR E DEPOIS EXECUTO ESSE METODO
//			System.out.println(result);

    //    }


    }
}



