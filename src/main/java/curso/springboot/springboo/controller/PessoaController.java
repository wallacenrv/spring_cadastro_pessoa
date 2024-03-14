package curso.springboot.springboo.controller;
import curso.springboot.springboo.model.Pessoa;
import curso.springboot.springboo.model.Telefone;
import curso.springboot.springboo.repository.PessoaRepository;
import curso.springboot.springboo.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private TelefoneRepository telefoneRepository;


    /* ---------------------  Metodo de Entrada--------------------  */
    @RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
    public ModelAndView inicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", new Pessoa()); // quando estiver na tela de cadastro... crio um objeto vazio
        Iterable<Pessoa> pessoasIt = pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))); // paginando = carregar do 0 ate o 5 e ordenando por nome
        modelAndView.addObject("pessoas", pessoasIt);
        return modelAndView;
    }


    //    METODO DE PAGINACAO - back end // ele vai intercepetar a paginacao
    @GetMapping("/pessoaspag")
    public ModelAndView carregaPessoaPorPaginacao(@PageableDefault(size = 5) Pageable pageable,
                                                  ModelAndView model, @RequestParam("nomepesquisa") String nomepesquisa) {
        Page<Pessoa> pagePessoa = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
        model.addObject("pessoas", pagePessoa);
        model.addObject("pessoaobj", new Pessoa());
        model.addObject("nomepesquisa", nomepesquisa);
        model.setViewName("cadastro/cadastropessoa");
        return model;

    }


    /* ----------------- METODO SALVAR / CADASTRAR --------------------      */

    @RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

        pessoa.setTelefones(telefoneRepository.getTelefones(pessoa.getId())); // tem que carregar os telefones, porque esta em cascata. senao dar erro na hora e ] de editar o cadastro, o mapemaneto esta em casacata

// validacao para salvar
        if (bindingResult.hasErrors()) { // se tiver erros
            ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

            modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
            modelAndView.addObject("pessoaobj", pessoa);

            // Pega as mensagens de erro das anotaçoes e joga na tela
            List<String> msg = new ArrayList<String>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                msg.add(objectError.getDefaultMessage()); // vem das anotações @NotEmpty e outras
            }
            modelAndView.addObject("msg", msg);
            return modelAndView;
        }

        pessoaRepository.save(pessoa); // realizou o cadastro

        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
        andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
        andView.addObject("pessoaobj", new Pessoa()); // cria um objeto vazio para o formlario trabalhar corretamente

        return andView;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
    public ModelAndView pessoas() {
        ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");

        andView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
        andView.addObject("pessoaobj", new Pessoa());
        return andView;
    }


    @GetMapping("/editarpessoa/{idpessoa}")
    public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa); // carregar o objeto pessoa atraves do uid

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoaobj", pessoa.get());
        return modelAndView;

    }


    @GetMapping("/removerpessoa/{idpessoa}")
    public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

        pessoaRepository.deleteById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoaRepository.findAll(PageRequest.of(0, 5, Sort.by("nome"))));
        modelAndView.addObject("pessoaobj", new Pessoa());
        return modelAndView;

    }

    @PostMapping("**/pesquisarpessoa")
    public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa,
                                  @RequestParam("pesqsexo") String pesqsexo,
                                  @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {


        Page<Pessoa> pessoas = null;
        if (pesqsexo != null && !pesqsexo.isEmpty()) {
            pessoas = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
        } else {
            pessoas = pessoaRepository.findPessoaByNamePage(nomepesquisa, pageable);
        }

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("pessoas", pessoas);
        modelAndView.addObject("pessoaobj", new Pessoa());
        modelAndView.addObject("nomepesqusa", nomepesquisa);
        return modelAndView;
    }


    //
    @GetMapping("/telefones/{idpessoa}")
    public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

        Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        modelAndView.addObject("pessoaobj", pessoa.get());
        modelAndView.addObject("msg", new ArrayList<String>());
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
        return modelAndView;

    }

    @PostMapping("**/addfonePessoa/{pessoaid}")
    public ModelAndView addFonePessoa(Telefone telefone,
                                      @PathVariable("pessoaid") Long pessoaid) {

        Pessoa pessoa = pessoaRepository.findById(pessoaid).get();

        if (telefone != null && telefone.getNumero().isEmpty()
                || telefone.getTipo().isEmpty()) {

            ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
            modelAndView.addObject("pessoaobj", pessoa);
            modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));

            List<String> msg = new ArrayList<String>();
            if (telefone.getNumero().isEmpty()) {
                msg.add("Numero deve ser informado");
            }

            if (telefone.getTipo().isEmpty()) {
                msg.add("Tipo deve ser informado");
            }
            modelAndView.addObject("msg", msg);

            return modelAndView;

        }

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        telefone.setPessoa(pessoa);

        telefoneRepository.save(telefone);

        modelAndView.addObject("pessoaobj", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid)); // carrega os telefones das pessoas
        return modelAndView;
    }

    @GetMapping("/removertelefone/{idtelefone}")
    public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

        // eu consigo trazer a pessoa, porque tenho a pessoa amarrada ao telefone
        Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();

        telefoneRepository.deleteById(idtelefone);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
        modelAndView.addObject("pessoaobj", pessoa);
        modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
        return modelAndView;

    }

}


