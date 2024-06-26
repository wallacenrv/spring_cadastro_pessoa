package curso.springboot.springboo.security;

import curso.springboot.springboo.model.Usuario;
import curso.springboot.springboo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUserByLogin(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não foi encontrado");
        }


        // apos criar as tabelas
        //aqui verifico login , senha e acessos e as autoridades dque configuiramos na tabela
        return new User(usuario.getLogin(), usuario.getPassword(),
                usuario.isEnabled(), true,
                true, true,
                usuario.getAuthorities());
    }
}
