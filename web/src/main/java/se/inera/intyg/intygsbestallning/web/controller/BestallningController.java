package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.ws.rs.QueryParam;
import java.util.List;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.web.bestallning.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.service.bestallning.AccepteraBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.ListBestallningService;

@RestController
@RequestMapping("/bestallningar")
public class BestallningController {

    private AccepteraBestallningService accepteraBestallningService;
    private ListBestallningService listBestallningService;

    public BestallningController(AccepteraBestallningService accepteraBestallningService, ListBestallningService listBestallningService) {
        this.accepteraBestallningService = accepteraBestallningService;
        this.listBestallningService = listBestallningService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBestallningar(
            @RequestParam(value = "status", required = false) List<BestallningStatus> statusar,
            @RequestParam(value = "textSearch", required = false) String textSearch,
            @RequestParam(value = "index", required = false) Integer index,
            @RequestParam(value = "limit", required = false) Integer limit) {

        if (statusar == null) {
            statusar = List.of(BestallningStatus.values());
        }

        if (index == null) {
            index = 0;
        }

        if (limit == null) {
            limit = 50;
        }

        var result = listBestallningService.listByQuery(new ListBestallningarQuery(statusar, textSearch, index, limit));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/acceptera")
    public ResponseEntity accepteraBestallning(@PathVariable String id, AccepteraBestallning accepteraBestallning) {
        accepteraBestallningService.accepteraBestallning(new AccepteraBestallningRequest(id, accepteraBestallning.getFritextForklaring()));
        return ResponseEntity.ok().build();
    }
}
