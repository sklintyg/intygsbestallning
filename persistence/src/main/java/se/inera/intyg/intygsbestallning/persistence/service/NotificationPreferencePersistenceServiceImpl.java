package se.inera.intyg.intygsbestallning.persistence.service;

import org.springframework.stereotype.Service;
import se.inera.intyg.intygsbestallning.common.notification.NotifieringPreference;
import se.inera.intyg.intygsbestallning.persistence.NotifieringPreferenceEntity;
import se.inera.intyg.intygsbestallning.persistence.repository.NotifieringPreferenceRepository;

import java.util.Optional;

@Service
public class NotificationPreferencePersistenceServiceImpl implements NotificationPreferencePersistenceService {

    private final NotifieringPreferenceRepository notifieringPreferenceRepository;

    public NotificationPreferencePersistenceServiceImpl(NotifieringPreferenceRepository notifieringPreferenceRepository) {
        this.notifieringPreferenceRepository = notifieringPreferenceRepository;
    }

    @Override
    public NotifieringPreference saveNewNotificationPreference(NotifieringPreference notifieringPreference) {
        var npe = NotifieringPreferenceEntity.Factory.toEntity(notifieringPreference);
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
