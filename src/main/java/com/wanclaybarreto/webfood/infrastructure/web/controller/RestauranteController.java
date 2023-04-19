package com.wanclaybarreto.webfood.infrastructure.web.controller;

import com.wanclaybarreto.webfood.application.service.RelatorioService;
import com.wanclaybarreto.webfood.application.service.RestauranteService;
import com.wanclaybarreto.webfood.application.service.ValidationException;
import com.wanclaybarreto.webfood.domain.pedido.*;
import com.wanclaybarreto.webfood.domain.restaurante.*;
import com.wanclaybarreto.webfood.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/restaurante")
public class RestauranteController {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CategoriaRestauranteRepository categoriaRestauranteRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping(path = "/home")
    public String home(Model model) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        List<Pedido> pedidos = pedidoRepository.findAllByRestaurante_IdOrderByDataHoraDesc(restauranteId);

        model.addAttribute("pedidos", pedidos);

        return "restaurante-home";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        //Atenção, o obketo que representa o usuário logado foi buscado no banco no momento do login,
        //mas, em termos de transação, não é o mesmo que está no banco de dados. A linha de código
        //"Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();"
        //garante que sempre estaremos lendo a última versão que está cadastrada no banco.
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();

        model.addAttribute("restaurante", restaurante);

        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);

        return "restaurante-cadastro";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("restaurante") @Valid Restaurante restaurante, Errors errors, Model model) {
        if (!errors.hasErrors()) {

            try {
                restauranteService.saveRestaurante(restaurante);
                model.addAttribute("msg", "Dados alterados com sucesso!");
            } catch (ValidationException e) {
                String[] fieldAndMessage = e.getMessage().split("--");
                errors.rejectValue(fieldAndMessage[0], null, fieldAndMessage[1]);
            }

        }

        System.out.println(errors);

        ControllerHelper.setEditMode(model, true);
        ControllerHelper.addCategoriasToRequest(categoriaRestauranteRepository, model);

        return "restaurante-cadastro";
    }

    @GetMapping(path = "/comidas")
    public String viewComidas(Model model) {
        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
        List<ItemCardapio> itensCardapio = itemCardapioRepository.findAllByRestaurante_IdOrderByNome(restauranteId);

        model.addAttribute("restaurante", restaurante);
        model.addAttribute("itensCardapio", itensCardapio);
        model.addAttribute("itemCardapio", new ItemCardapio());

        return "restaurante-comidas";
    }

    @PostMapping(path = "/comidas/cadastrar")
    public String cadastrarComida(@Valid @ModelAttribute("itemCardapio") ItemCardapio itemCardapio,
                                  Errors errors, Model model) {

        if(errors.hasErrors()) {
            Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
            Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow();
            List<ItemCardapio> itensCardapio = itemCardapioRepository.findAllByRestaurante_IdOrderByNome(restauranteId);

            model.addAttribute("restaurante", restaurante);
            model.addAttribute("itensCardapio", itensCardapio);

            return "restaurante-comidas";
        }

        restauranteService.saveItemCardapio(itemCardapio);

        return "redirect:/restaurante/comidas";
    }

    @GetMapping(path = "/comidas/remover")
    public String removerComida(@RequestParam("itemId") Integer itemId, Model model) {
        itemCardapioRepository.deleteById(itemId);
        return "redirect:/restaurante/comidas";
    }

    @GetMapping(path = "/pedido")
    public String viewPedido(Model model, @RequestParam("pedidoId") Integer pedidoId) {
        Pedido pedido =  pedidoRepository.findById(pedidoId).orElseThrow();
        model.addAttribute("pedido", pedido);

        return "restaurante-pedido";
    }

    @PostMapping(path = "/pedido/proximoStatus")
    public String avancarStatusPedido(Model model, @RequestParam("pedidoId") Integer pedidoId) {
        Pedido pedido =  pedidoRepository.findById(pedidoId).orElseThrow();
        pedido.definirProximoStatus();
        pedidoRepository.save(pedido);

        model.addAttribute("msg", "Status alterado com sucesso!");
        model.addAttribute("pedido", pedido);

        return "restaurante-pedido";
    }

    @GetMapping(path = "/relatorio/pedidos")
    public String relatorioPedidos(@ModelAttribute("relatorioPedidoFilter") RelatorioPedidoFilter filter, Model model) {
        if (filter.isDataFinalLessThanDataInicial()) {
            model.addAttribute("msg", "A data final não pode ser menor que a data inicial.");
            model.addAttribute("pedidos", new ArrayList<Pedido>(0));
            model.addAttribute("relatorioPedidoFilter", filter);
        }

        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        List<Pedido> pedidos = relatorioService.listarPedidos(restauranteId, filter);

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("relatorioPedidoFilter", filter);

        return "restaurante-relatorio-pedidos";
    }

    @GetMapping(path = "/relatorio/itens")
    public String relatorioItens(@ModelAttribute("relatorioItemFilter") RelatorioItemFilter filter, Model model) {
        if (filter.isDataFinalLessThanDataInicial()) {
            model.addAttribute("msg", "A data final não pode ser menor que a data inicial.");
            model.addAttribute("itensFaturamento", new ArrayList<RelatorioItemFaturamento>(0));
            model.addAttribute("relatorioItemFilter", filter);
        }

        Integer restauranteId = SecurityUtils.loggedRestaurante().getId();
        List<ItemCardapio> itensCardapio = itemCardapioRepository.findAllByRestaurante_IdOrderByNome(restauranteId);
        List<RelatorioItemFaturamento> itensFaturamento = relatorioService.calcularFaturamentoItens(restauranteId, filter);

        model.addAttribute("itensCardapio", itensCardapio);
        model.addAttribute("relatorioItemFilter", filter);
        model.addAttribute("itensFaturamento", itensFaturamento);

        return "restaurante-relatorio-itens";
    }

}
