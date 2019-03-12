package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.web.bestallning.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.service.bestallning.AccepteraBestallningService;

@RestController
@RequestMapping("/bestallningar")
public class BestallningController {

    private final AccepteraBestallningService accepteraBestallningService;

    public BestallningController(AccepteraBestallningService accepteraBestallningService) {
        this.accepteraBestallningService = accepteraBestallningService;
    }

    @GetMapping
    public ResponseEntity getBestallningar() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/acceptera")
    public ResponseEntity accepteraBestallning(@PathVariable String id, AccepteraBestallning accepteraBestallning) {
        accepteraBestallningService.accepteraBestallning(new AccepteraBestallningRequest(id, accepteraBestallning.getFritextForklaring()));
        return ResponseEntity.ok().build();
    }
}
