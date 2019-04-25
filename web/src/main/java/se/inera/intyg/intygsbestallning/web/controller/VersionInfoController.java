package se.inera.intyg.intygsbestallning.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.ZoneId;
import se.inera.intyg.intygsbestallning.web.version.VersionInfo;

@RestController
public class VersionInfoController {

    private BuildProperties buildProperties;
    private Environment environment;

    public VersionInfoController(BuildProperties buildProperties, Environment environment) {
        this.buildProperties = buildProperties;
        this.environment = environment;
    }

    @GetMapping(path = "/public-api/version", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity getVersion() {
        var applicationName = buildProperties.getArtifact();
        var buildVersion = buildProperties.getVersion();
        var buildTimestamp = LocalDateTime.ofInstant(buildProperties.getTime(), ZoneId.systemDefault());
        var activeProfiles = StringUtils.join(environment.getActiveProfiles(), ", ");
        return ResponseEntity.ok(new VersionInfo(applicationName, buildVersion, buildTimestamp, activeProfiles));
    }
}
