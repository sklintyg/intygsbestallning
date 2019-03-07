package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringPreference;
import se.inera.intyg.intygsbestallning.persistence.entity.NotifieringPreferenceEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.NotificationPreferenceRepository;

import java.util.Optional;

@Service
public class NotificationPreferencePersistenceServiceImpl implements NotificationPreferencePersistenceService {

    private final NotificationPreferenceRepository notifieringPreferenceRepository;

    public NotificationPreferencePersistenceServiceImpl(NotificationPreferenceRepository notifieringPreferenceRepository) {
        this.notifieringPreferenceRepository = notifieringPreferenceRepository;
    }

    @Override
    public NotifieringPreference saveNewNotificationPreference(NotifieringPreference notifieringPreference) {
        var npe = NotifieringPreferenceEntity.Factory.fromDomain(notifieringPreference);
        return NotifieringPreferenceEntity.Factory.toDomain(notifieringPreferenceRepository.save(npe));
    }

    @Override
    public NotifieringPreference updateExistingNotificationPreference(NotifieringPreference notifieringPreference) {
        throw new UnsupportedOperationException("TODO: implement updateExistingNotificationPreference()");
    }

    @Override
    public Optional<NotifieringPreference> getNotificationPreference(String hsaId) {
        return notifieringPreferenceRepository.findByHsaId(hsaId)
                .map(NotifieringPreferenceEntity.Factory::toDomain);
    }
}
