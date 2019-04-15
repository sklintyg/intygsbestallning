package se.inera.intyg.intygsbestallning.web.jobs;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Notifiering;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.dto.ListBestallningarBasedOnStatusQuery;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.inera.intyg.intygsbestallning.persistence.entity.QBestallningEntity;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;

@Component
@EnableScheduling
public class OlastBestallningPaminnelseJob {
    private static final Logger LOG = LoggerFactory.getLogger(OlastBestallningPaminnelseJob.class);
    private static final String JOB_NAME = "olastBestallningPaminnelseJob";

    private NotifieringSendService notifieringSendService;
    private BestallningPersistenceService bestallningPersistenceService;
    private BestallningTextService bestallningTextService;

    OlastBestallningPaminnelseJob(
            NotifieringSendService notifieringSendService,
            BestallningPersistenceService bestallningPersistenceService,
            BestallningTextService bestallningTextService) {
        this.notifieringSendService = notifieringSendService;
        this.bestallningPersistenceService = bestallningPersistenceService;
        this.bestallningTextService = bestallningTextService;

    }

    @Transactional
    @Scheduled(cron = "${bestallning.paminnelse.cron}")
    @SchedulerLock(name = JOB_NAME)
    public void checkBestallningar() {
        LOG.info("Performing batch notification of Bestallningar with status OLAST");
        var pb = new BooleanBuilder();
        var qe = QBestallningEntity.bestallningEntity;
        pb.and(qe.status.eq(BestallningStatus.OLAST));

        var bestallningList = bestallningPersistenceService.listBestallningarBasedOnStatus(
                new ListBestallningarBasedOnStatusQuery(Lists.newArrayList(BestallningStatus.OLAST)));

        var filteredFirstReminderList = bestallningList.stream()
                .filter(paminnelse1Filter())
                .filter(it -> DAYS.between(it.getAnkomstDatum(), LocalDate.now().atStartOfDay()) > getFirstReminderTime(it))
                .collect(Collectors.toList());

        var filteredSecondReminderList = bestallningList.stream()
                .filter(paminnelse2Filter())
                .filter(it -> DAYS.between(it.getAnkomstDatum(), LocalDate.now().atStartOfDay()) > getSecondReminderTime(it))
                .collect(Collectors.toList());

        // Do the sending
        sendNotificationAndUpdateBestallning(filteredFirstReminderList, NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1);
        sendNotificationAndUpdateBestallning(filteredSecondReminderList, NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2);

        LOG.info("Sent {} first notifications and {} second notifications", filteredFirstReminderList.size(),
                filteredSecondReminderList.size());
    }

    private void sendNotificationAndUpdateBestallning(List<Bestallning> filtered, NotifieringTyp notifieringTyp) {
        filtered.forEach(it -> {
            it.getNotifieringar().add(Notifiering.Factory.paminnelse(it.getVardenhet().getHsaId(), notifieringTyp));
            var saved = bestallningPersistenceService.updateBestallning(it);
            notifieringSendService.paminnelse(saved, notifieringTyp);
            bestallningPersistenceService.updateBestallning(saved);
        });
    }

    @NotNull
    private Predicate<Bestallning> paminnelse1Filter() {
        return it -> it.getNotifieringar() != null && it.getNotifieringar().stream().noneMatch(n ->
                n.getTyp().equals(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1));
    }

    @NotNull
    private Predicate<Bestallning> paminnelse2Filter() {
        return it -> it.getNotifieringar() != null &&
                it.getNotifieringar().stream()
                        .anyMatch(n -> n.getTyp().equals(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1)) &&
                it.getNotifieringar().stream()
                        .noneMatch(n -> n.getTyp().equals(NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2));
    }

    private int getFirstReminderTime(Bestallning bestallning) {
        BestallningTexter texter = bestallningTextService.getBestallningTexter(bestallning);
        return Integer.parseInt(texter.getPaminnelse1());
    }

    private int getSecondReminderTime(Bestallning bestallning) {
        BestallningTexter texter = bestallningTextService.getBestallningTexter(bestallning);
        return Integer.parseInt(texter.getPaminnelse2());
    }
}
