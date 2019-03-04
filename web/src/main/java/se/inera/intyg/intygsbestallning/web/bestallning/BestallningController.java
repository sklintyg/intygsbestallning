package se.inera.intyg.intygsbestallning.web.bestallning;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bestallningar")
public class BestallningController {

    @GetMapping
    public ResponseEntity getBestallningar() {
        return ResponseEntity.ok().build();
    }
}
