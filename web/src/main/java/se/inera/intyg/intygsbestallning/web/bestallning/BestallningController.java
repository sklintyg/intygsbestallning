package se.inera.intyg.intygsbestallning.web.bestallning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.web.user.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.user.DtoObjectsKt;

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
        accepteraBestallningService.accepteraBestallning(DtoObjectsKt.fromDto(accepteraBestallning, id));
        return ResponseEntity.ok().build();
    }
}
