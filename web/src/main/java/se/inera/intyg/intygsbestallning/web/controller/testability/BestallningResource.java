package se.inera.intyg.intygsbestallning.web.controller.testability;

import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.EntityTxMapper;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping(BestallningResource.API_TEST_BESTALLNINGAR)
@Profile({ "dev", "testability-api" })
public class BestallningResource {

    public static final String API_TEST_BESTALLNINGAR = "/api/test/bestallningar";

    private BestallningPersistenceService bestallningPersistenceService;
    private EntityTxMapper entityTxMapper;

    public BestallningResource(BestallningPersistenceService bestallningPersistenceService, EntityTxMapper entityTxMapper) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.entityTxMapper = entityTxMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createBestallning(@RequestBody Bestallning bestallning) {
        return entityTxMapper.jsonResponse(() -> bestallningPersistenceService.saveNewBestallning(bestallning));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteBestallning(@PathVariable("id") Long id) {
        return entityTxMapper.jsonResponse(() -> {
            var bestallning = bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(id, null, null).orElseThrow(
                    () -> new IllegalArgumentException("Bestallning with id '" + id + "' does not exist."));
            bestallningPersistenceService.deleteBestallning(bestallning);
            return EntityTxMapper.OK;
        });
    }
}
