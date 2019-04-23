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
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.InvanarePersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.VardenhetPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.testdata.BootstrapBestallning;
import se.inera.intyg.intygsbestallning.web.service.util.EntityTxMapper;
import se.inera.intyg.schemas.contract.Personnummer;

import javax.ws.rs.core.Response;
import java.util.Optional;

@RestController
@RequestMapping(BestallningResource.API_TEST_BESTALLNINGAR)
@Profile({ "dev", "testability-api" })
public class BestallningResource {

    public static final String API_TEST_BESTALLNINGAR = "/api/test/bestallningar";

    private BestallningPersistenceService bestallningPersistenceService;
    private EntityTxMapper entityTxMapper;
    private InvanarePersistenceService invanarePersistenceService;
    private VardenhetPersistenceService vardenhetPersistenceService;

    public BestallningResource(BestallningPersistenceService bestallningPersistenceService, EntityTxMapper entityTxMapper,
                               InvanarePersistenceService invanarePersistenceService, VardenhetPersistenceService vardenhetPersistenceService) {
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.entityTxMapper = entityTxMapper;
        this.invanarePersistenceService = invanarePersistenceService;
        this.vardenhetPersistenceService = vardenhetPersistenceService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createBestallning(@RequestBody BootstrapBestallning bestallning) {
        return entityTxMapper.jsonResponse(() -> bestallningPersistenceService.saveNewBestallning(lookupExisting(bestallning)));
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

    Bestallning lookupExisting(BootstrapBestallning bestallning)
    {
        Optional<Invanare> existingInvanare = invanarePersistenceService.getInvanareByPersonnummer(bestallning.getInvanare().getPersonId());
        if (existingInvanare.isPresent())
        {
            Invanare invanare = existingInvanare.get();
            invanare.setBakgrundNulage(bestallning.getInvanare().getBakgrundNulage());
            bestallning.setInvanare(invanare);
        }

        Optional<Vardenhet> existingVardenhet = vardenhetPersistenceService.getVardenhetByHsaId(bestallning.getVardenhet().getHsaId());
        if (existingVardenhet.isPresent())
        {
            Vardenhet vardenhet = existingVardenhet.get();
            vardenhet.setVardgivareHsaId(bestallning.getVardenhet().getVardgivareHsaId());
            vardenhet.setNamn(bestallning.getVardenhet().getNamn());
            vardenhet.setEpost(bestallning.getVardenhet().getEpost());
            bestallning.setVardenhet(vardenhet);
        }

        return BootstrapBestallning.Factory.toDomain(bestallning);
    }
}
