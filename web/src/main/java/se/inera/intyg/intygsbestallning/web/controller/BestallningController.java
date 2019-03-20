package se.inera.intyg.intygsbestallning.web.controller;

import com.google.common.collect.Lists;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.web.bestallning.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.bestallning.BestallningStatusKategori;
import se.inera.intyg.intygsbestallning.web.service.bestallning.AccepteraBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.GetBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.ListBestallningService;

@RestController
@RequestMapping("/api/bestallningar")
public class BestallningController {

    private AccepteraBestallningService accepteraBestallningService;
    private ListBestallningService listBestallningService;
    private GetBestallningService getBestallningService;

    public BestallningController(
            AccepteraBestallningService accepteraBestallningService,
            ListBestallningService listBestallningService,
            GetBestallningService getBestallningService) {
        this.accepteraBestallningService = accepteraBestallningService;
        this.listBestallningService = listBestallningService;
        this.getBestallningService = getBestallningService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBestallningar(
            @RequestParam(value = "category", required = false) BestallningStatusKategori kategori,
            @RequestParam(value = "textSearch", required = false) String textSearch,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "limit", required = false) Integer limit) {

        var statusar = Lists.<BestallningStatus>newArrayList();

        if (kategori == null) {
            statusar = Lists.newArrayList(BestallningStatus.values());
        } else {
            statusar = Lists.newArrayList(kategori.getList());
        }

        if (pageIndex == null) {
            pageIndex = 0;
        }

        if (limit == null) {
            limit = 50;
        }

        var result = listBestallningService.listByQuery(new ListBestallningarQuery(statusar, textSearch, pageIndex, limit));
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBestallning(@PathVariable String id) {

        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Id could not be converted to Long value");
        }

        var aktuellBestallning = getBestallningService.getBestallningById(idLong);

        if (aktuellBestallning.isPresent()) {
            return ResponseEntity.ok(aktuellBestallning);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/acceptera")
    public ResponseEntity accepteraBestallning(@PathVariable String id, AccepteraBestallning accepteraBestallning) {
        accepteraBestallningService.accepteraBestallning(new AccepteraBestallningRequest(id, accepteraBestallning.getFritextForklaring()));
        return ResponseEntity.ok().build();
    }
}
