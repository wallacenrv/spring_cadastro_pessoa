package curso.springboot.springboo.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity // TABELA DE PAPEIS
public class Roles implements GrantedAuthority {


    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // nome do acesso
    private String nomeRole;

    // ROLE_ADMIN, ROLE_GERENTE, ROLE_SECREIARIO = padrao do spring security
    @Override
    public String getAuthority() {
        return this.nomeRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeRole() {
        return nomeRole;
    }

    public void setNomeRole(String nomeRole) {
        this.nomeRole = nomeRole;
    }
}
