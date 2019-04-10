package se.inera.intyg.intygsbestallning.web.jobs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class OlastBestallningPaminnelseJobTest {

    @Mock
    private NotifieringSendService notifieringSendService;

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private BestallningTextService bestallningTextService;

    @InjectMocks
    private OlastBestallningPaminnelseJob job;

    @Test
    void testOlastBestallingPaminnelseJobOk() {
        job.checkBestallningar();

    }
}
