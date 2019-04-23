package se.inera.intyg.intygsbestallning.web.controller;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.ZoneId;
import se.inera.intyg.intygsbestallning.web.version.Versions;

@Profile("!prod")
@RestController
public class VersionsController {

    private BuildProperties buildProperties;

    public VersionsController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/api/versions")
    ResponseEntity getVersions() {
        var applicationName = buildProperties.getArtifact();
        var buildVersion = buildProperties.getVersion();
        var buildTimestamp = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());

        return ResponseEntity.ok(
                new Versions(
                        applicationName,
                        buildVersion,
                        buildTimestamp,
                        System.getProperty("infraVersion")
                )
        );
    }
}
