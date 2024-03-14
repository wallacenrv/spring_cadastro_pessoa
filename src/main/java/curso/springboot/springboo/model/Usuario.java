package curso.springboot.springboo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.management.relation.Role;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String login;
    private String senha;


    @OneToMany(fetch = FetchType.EAGER) // para sempre carregar
    @JoinTable(name = "usuario_role",//cria tabela de acesso do usuario
            joinColumns = @JoinColumn(name = "usuario_id",// nome da coluna
                    referencedColumnName = "id",
                    table = "usuario"),// referenciando o id da tabela usuario

            inverseJoinColumns = @JoinColumn(name = "role_id", // é da roles
                    referencedColumnName = "id",
                    table = "role"))
    private List<Roles> roles;
// um usuario tem muitos acessos

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    //aqui
    @Override
    public String getPassword() {
        return senha;
    }

    //aqui
    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
