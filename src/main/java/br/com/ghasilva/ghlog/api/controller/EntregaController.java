package br.com.ghasilva.ghlog.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.ghasilva.ghlog.api.assembler.EntregaAssembler;
import br.com.ghasilva.ghlog.api.model.Entrega;
import br.com.ghasilva.ghlog.api.model.EntregaModel;
import br.com.ghasilva.ghlog.api.model.input.EntregaInput;
import br.com.ghasilva.ghlog.api.service.SolicitacaoEntregaService;
import br.com.ghasilva.ghlog.repository.EntregaRepository;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

	@Autowired
	private SolicitacaoEntregaService solicitacaoEntregaService;

	@Autowired
	private EntregaRepository entregaRepository;

	@Autowired
	private EntregaAssembler entregaAssembler;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EntregaModel solicitar(@Valid @RequestBody EntregaInput entregaInput) {
		Entrega novaEntrega  = entregaAssembler.toEntity(entregaInput);
		Entrega entregaSolicitada = solicitacaoEntregaService.solicitar(novaEntrega);
		return entregaAssembler.toModel(entregaSolicitada);
	}

	@GetMapping
	public List<EntregaModel> listar() {
		return entregaAssembler.toCollectionModel(entregaRepository.findAll());
		

	}

	@GetMapping("/{entregaId}")
	public ResponseEntity<EntregaModel> buscar(@PathVariable Long entregaId) {

		return entregaRepository.findById(entregaId)
		.map(entrega -> ResponseEntity.ok(entregaAssembler.toModel(entrega)))
		.orElse(ResponseEntity.notFound().build());
	}
}


