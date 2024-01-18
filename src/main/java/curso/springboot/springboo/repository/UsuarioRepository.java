package curso.springboot.springboo.repository;

import curso.springboot.springboo.model.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @Query("select u from Usuario u where u.login = ?1")
        // ?1 siginfica primeiro paramentro que eu passar
    Usuario findUserByLogin(String login);
}
