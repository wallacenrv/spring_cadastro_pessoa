package curso.springboot.springboo.repository;

import curso.springboot.springboo.model.Pessoa;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional // o Spring controla todas as transacoes do Spring
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    // JPA REPOSITORY EXTEND DA pAGIN AND SORT PARA PAGINACAO
//    Selecione da tabela Pessoa do campo nome , onde contenha %?1%
    @Query("select p from Pessoa p where p.nome like %?1%")
    // jpql
    List<Pessoa> findPessoaByname(String nome);

    @Query("select p from Pessoa p where p.sexopessoa= ?1 ")
        // jpql
    List<Pessoa> findPessoaBySexo(String sexopsssoa);


    @Query("select p from Pessoa p where p.nome like %?1% and p.sexopessoa= ?2 ")
        // jpql
    List<Pessoa> findPessoaBynameSexo(String nome, String sexopessoa);


    default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);

//    Estamos confirando a pesquisa para consultar por partes do nome no banco de dados, igual a like com SQL
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        // Une o objeto com o valor e a configracao para consultar
        Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
        Page<Pessoa> pessoas = findAll(example, pageable);
        return pessoas;

    }

    default Page<Pessoa> findPessoaByNameSexoPage(String nome, String sexo, Pageable pageable) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSexopessoa(sexo);

//    Estamos confirando a pesquisa para consultar por partes do nome no banco de dados, igual a like com SQL
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("sexopessoa", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        // Une o objeto com o valor e a configracao para consultar
        Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
        Page<Pessoa> pessoas = findAll(example, pageable);
        return pessoas;

    }


}
