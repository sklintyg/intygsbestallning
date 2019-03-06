package se.inera.intyg.intygsbestallning.persistence.service;

import se.inera.intyg.intygsbestallning.common.notification.NotifieringPreference;

import java.util.Optional;

public interface NotificationPreferencePersistenceService {

    public NotifieringPreference saveNewNotificationPreference(NotifieringPreference notificationPreference);

    public NotifieringPreference updateExistingNotificationPreference(NotifieringPreference notifieringPreference);

    public Optional<NotifieringPreference> getNotificationPreference(String hsaId);

}
