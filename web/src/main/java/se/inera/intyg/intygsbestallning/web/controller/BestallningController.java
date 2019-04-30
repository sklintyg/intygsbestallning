/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import se.inera.intyg.intygsbestallning.common.domain.BestallningSvar;
import se.inera.intyg.intygsbestallning.common.dto.AccepteraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.AvvisaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.KlarmarkeraBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningDirection;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningSortColumn;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarQuery;
import se.inera.intyg.intygsbestallning.common.dto.PdfBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.RaderaBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.VisaBestallningScope;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.bestallning.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.bestallning.AvvisaBestallning;
import se.inera.intyg.intygsbestallning.web.bestallning.BestallningStatusKategori;
import se.inera.intyg.intygsbestallning.web.bestallning.RaderaBestallning;
import se.inera.intyg.intygsbestallning.web.service.bestallning.AccepteraBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.AvvisaBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.KlarmarkeraBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.ListBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.RaderaBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.VidarebefordraBestallningService;
import se.inera.intyg.intygsbestallning.web.service.bestallning.VisaBestallningService;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

import javax.ws.rs.Produces;

@RestController
@RequestMapping("/api/bestallningar")
public class BestallningController {

    private AccepteraBestallningService accepteraBestallningService;
    private ListBestallningService listBestallningService;
    private VisaBestallningService visaBestallningService;
    private AvvisaBestallningService avvisaBestallningService;
    private RaderaBestallningService raderaBestallningService;
    private KlarmarkeraBestallningService klarmarkeraBestallningService;
    private PdfBestallningService pdfBestallningService;
    private UserService userService;

    public BestallningController(
            AccepteraBestallningService accepteraBestallningService,
            ListBestallningService listBestallningService,
            VisaBestallningService visaBestallningService,
            AvvisaBestallningService avvisaBestallningService,
            RaderaBestallningService raderaBestallningService,
            KlarmarkeraBestallningService klarmarkeraBestallningService,
            PdfBestallningService pdfBestallningService,
            UserService userService) {
        this.accepteraBestallningService = accepteraBestallningService;
        this.listBestallningService = listBestallningService;
        this.visaBestallningService = visaBestallningService;
        this.avvisaBestallningService = avvisaBestallningService;
        this.raderaBestallningService = raderaBestallningService;
        this.klarmarkeraBestallningService = klarmarkeraBestallningService;
        this.pdfBestallningService = pdfBestallningService;
        this.userService = userService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getBestallningar(
            @RequestParam(value = "category", required = false) BestallningStatusKategori kategori,
            @RequestParam(value = "textSearch", required = false) String textSearch,
            @RequestParam(value = "pageIndex", required = false) Integer pageIndex,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sortColumn", required = false) ListBestallningSortColumn sortColumn,
            @RequestParam(value = "sortDirection", required = false) ListBestallningDirection sortDirection) {

        var statusar = (kategori != null) ? kategori.getList() : Collections.EMPTY_LIST;

        if (pageIndex == null) {
            pageIndex = 0;
        }

        if (limit == null) {
            limit = 50;
        }

        if (sortColumn == null) {
            sortColumn = ListBestallningSortColumn.ID;
        }

        if (sortDirection == null) {
            sortDirection = ListBestallningDirection.ASC;
        }

        var user = userService.getUser();

        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();

        var result = listBestallningService.listByQuery(
                new ListBestallningarQuery(
                        statusar,
                        hsaId,
                        orgNrVardgivare,
                        textSearch,
                        pageIndex,
                        limit,
                        sortColumn,
                        sortDirection));

        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity visaBestallning(@PathVariable String id) {

        Long idLong;
        try {
            idLong = Long.valueOf(id);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Id could not be converted to Long value");
        }

        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();

        var aktuellBestallning = visaBestallningService.getBestallningByIdAndHsaIdAndOrgId(idLong, hsaId, orgNrVardgivare);

        if (aktuellBestallning.isPresent()) {
            return ResponseEntity.ok(aktuellBestallning);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/acceptera")
    public ResponseEntity accepteraBestallning(@PathVariable String id, AccepteraBestallning accepteraBestallning) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        accepteraBestallningService.accepteraBestallning(new AccepteraBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare,
                BestallningSvar.ACCEPTERAT,
                accepteraBestallning.getFritextForklaring()));

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/avvisa")
    public ResponseEntity avvisaBestallning(@PathVariable String id, AvvisaBestallning avvisaBestallning) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        avvisaBestallningService.avvisaBestallning(new AvvisaBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare,
                BestallningSvar.AVVISAT,
                avvisaBestallning.getFritextForklaring()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/radera")
    public ResponseEntity raderaBestallning(@PathVariable String id, RaderaBestallning raderaBestallning) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        raderaBestallningService.raderaBestallning(new RaderaBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare,
                BestallningSvar.RADERAT,
                raderaBestallning.getFritextForklaring()));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/klarmarkera")
    public ResponseEntity klarmarkeraBestallning(@PathVariable String id) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        klarmarkeraBestallningService.klarmarkeraBestallning(new KlarmarkeraBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare));
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/pdf/forfragan", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity pdfForfragan(@PathVariable String id) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        byte[] pdf = pdfBestallningService.pdf(new PdfBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare,
                VisaBestallningScope.FORFRAGAN));
        return ResponseEntity.ok(pdf);
    }

    @GetMapping(value = "/{id}/pdf/faktureringsunderlag", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity pdfFaktureringsunderlag(@PathVariable String id) {
        var user = userService.getUser();
        var hsaId = user.getUnitContext().getId();
        var orgNrVardgivare = ((IbVardenhet) user.getUnitContext()).getOrgNrVardgivare();
        byte[] pdf = pdfBestallningService.pdf(new PdfBestallningRequest(
                id,
                hsaId,
                orgNrVardgivare,
                VisaBestallningScope.FAKTURERINGSUNDERLAG));
        return ResponseEntity.ok(pdf);
    }
}
