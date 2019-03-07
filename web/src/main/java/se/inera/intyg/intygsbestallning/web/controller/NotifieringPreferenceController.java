package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.intygsbestallning.web.notification.NotificationPreferenceDto;
import se.inera.intyg.intygsbestallning.web.service.notifiering.NotifieringPreferenceService;

@RestController
@RequestMapping("/api/notifiering/preference")
public class NotifieringPreferenceController {

    private final NotifieringPreferenceService notifieringPreferenceService;

    //TODO: handle preference by checking current user session (i.e., use authenticated user) instead of parametrized hsaId.
    //private final  UserService userService;

    public NotifieringPreferenceController(NotifieringPreferenceService notifieringPreferenceService) {
        this.notifieringPreferenceService = notifieringPreferenceService;
    }

    @GetMapping(path = "/{hsaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificationPreferenceDto> getNotificationPreferences(@PathVariable("hsaId") String hsaId) {
        var notificationPreference = notifieringPreferenceService.getNotificationPreference(hsaId);
        if (notificationPreference.isPresent()) {
            return ResponseEntity.ok(NotificationPreferenceDto.Factory.fromDomain(notificationPreference.get()));
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<NotificationPreferenceDto> setNotificationPreferences(
            @RequestBody final NotificationPreferenceDto npd) {
        var newlySetValue = notifieringPreferenceService.setNotificationPreference(NotificationPreferenceDto.Factory.toDomain(npd));
        return ResponseEntity.ok(NotificationPreferenceDto.Factory.fromDomain(newlySetValue));
    }

}
