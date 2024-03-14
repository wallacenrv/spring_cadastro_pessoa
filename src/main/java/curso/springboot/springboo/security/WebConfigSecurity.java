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
@EnableWebSecurity // classe de configuracao de segurança
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService; // injecao de dependencia


    @Override // Configura as solicitações de acesso por Http
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable() // Desativa as configurações padrão de memória.
                .authorizeRequests() // Permitir restringir acessos
                .antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a pagina inicial
                .antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and().formLogin().permitAll()// permite qualquer usuário
                .loginPage("/login")
                .defaultSuccessUrl("/cadastropessoa")// ENTRA NO CASDASTRO DE PESSOAS
                .failureUrl("/login?error=true")// SE FALAHAR EU MANDO PARA ESSA AI
                .and()
                .logout().logoutSuccessUrl("/login")// Mapeia URL de Logout e invalida usuário autenticado
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
// cria um formulari ode login automaticamente
    }

    @Override // cria autenticacao do usuario com banco de dados ou em memória
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(implementacaoUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());

//parte 1
       // @Override // cria autenticacao do usuario com banco de dados ou em memória
      //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      /*  auth.inMemoryAuthentication().passwordEncoder( new BCryptPasswordEncoder())
        //auth.inMemoryAuthentication().passwordEncoder(NoOpPasswordEncoder.getInstance())// ta sem codificacao
                .withUser("wallacen")
                .password("$2a$10$0vNtbVEzSvTW5coYp26bBOLzGWtlvJ92SduKdWp/UcUk1nVnp.DCq")
                .roles("ADMIN");


       */

    }

    @Override // Ignora URL especificas
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/materialize/**"); // quero que ignore tudo quwe esta dentro da pasta /materialize
    }

}


//mapemanetode url
//        controle usuario
//                especificar algum bloqueio