package se.inera.intyg.intygsbestallning.web.controller.testability;

import com.querydsl.core.BooleanBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.persistence.entity.BestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.InvanareEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.entity.VardenhetEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.BestallningRepository;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.service.util.EntityTxMapper;

import javax.ws.rs.core.Response;

@RestController
@RequestMapping(BestallningResource.API_TEST_BESTALLNINGAR)
@Profile({ "dev", "testability-api" })
public class BestallningResource {

    public static final String API_TEST_BESTALLNINGAR = "/api/test/bestallningar";

    private BestallningRepository bestallningRepository;
    private EntityTxMapper entityTxMapper;

    public BestallningResource(BestallningRepository bestallningRepository, EntityTxMapper entityTxMapper) {
        this.bestallningRepository = bestallningRepository;
        this.entityTxMapper = entityTxMapper;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createBestallning(@RequestBody Bestallning bestallning) {

        var invanareEntity = InvanareEntity.Factory.toEntity(bestallning.getInvanare());
        var vardenhetEntity = VardenhetEntity.Factory.toEntity(bestallning.getVardenhet());
        var bestallningEntity = BestallningEntity.Factory.toEntity(bestallning, invanareEntity, vardenhetEntity);

        return entityTxMapper.jsonResponse(() -> bestallningRepository.save(bestallningEntity));
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteBestallning(@PathVariable("id") Long id) {
        return entityTxMapper.jsonResponse(() -> {

            var pb = new BooleanBuilder();
            var qe = QBestallningEntity.bestallningEntity;
            pb.and(qe.id.eq(id));

            var bestallning = bestallningRepository.findOne(pb).orElseThrow(
                    () -> new IllegalArgumentException("Bestallning with id '" + id + "' does not exist."));

            bestallningRepository.delete(bestallning);

            return EntityTxMapper.OK;
        });
    }
}
