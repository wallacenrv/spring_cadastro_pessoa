package curso.springboot.springboo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration // classe de configuracao do spring
@EnableWebSecurity // classe de congiruacao de segurança
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService; // injecao de dependencia


    @Override // Configura as solicitações de acesso por Http
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable() // Desativa as configurações padrão de memória.
                .authorizeRequests() // Pertimi restringir acessos
                .antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
                .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().permitAll()// permite qualquer usuário
                .loginPage("/login")
                .defaultSuccessUrl("/cadastropessoa")
                .failureUrl("/login?error=true")
                .and()
                .logout().logoutSuccessUrl("/login")
                // Mapeia URL de Logout e invalida usuário autenticado
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

    }

    @Override // cria autenticacao do usuario com bano de dados ou memoria
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(implementacaoUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());



      /*  auth.inMemoryAuthentication().passwordEncoder( new BCryptPasswordEncoder())
        //auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())// ta sem codificacao
                .withUser("wallacen")
                .password("$2a$10$0vNtbVEzSvTW5coYp26bBOLzGWtlvJ92SduKdWp/UcUk1nVnp.DCq")
                .roles("ADMIN");


       */

    }

    @Override // Ignora URL especificas
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/materialize/**");
    }

}


//mapemanetode url
//        controle usuario
//                especificar algum bloqueio