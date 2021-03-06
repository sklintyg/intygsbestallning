/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.intygsbestallning.web.pdl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import se.inera.intyg.infra.logmessages.ActivityPurpose;
import se.inera.intyg.infra.logmessages.ActivityType;
import se.inera.intyg.infra.logmessages.ResourceType;
import se.inera.intyg.infra.security.common.model.Feature;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.RequestOrigin;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.infra.security.common.model.UserOriginType;
import se.inera.intyg.intygsbestallning.common.property.PdlLoggingProperties;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PdlLoggingMessageFactoryTest {

    private static final String USER_ID = "SE2321000198-016965";
    private static final String USER_NAME = "";
    private static final String USER_TITLE = "Vårdadministratör";
    private static final String VG_ID = "vardgivare-1";
    private static final String VG_NAME = "vardgivare-namn";
    private static final String VG_ORGNR = "vardgivare-orgnr";
    private static final String VE_ID = "vardenhet-1";
    private static final String VE_NAME = "vardenhet-nanm";
    private static final String PATIENT_ID = "19121212-1212";

    private static final String ACTIVITY_LEVEL = UUID.randomUUID().toString();

    private static final String PDL_LOGGING_SYSTEMID = "devtest-id";
    private static final String PDL_LOGGING_SYSTEMNAME = "devtest-name";
    private static final String PDL_LOGGING_QUEUENAME = "ib.pdllogging.queue";

    PdlLoggingMessageFactoryImpl testee;

    @BeforeEach
    public void setup() {
      testee = new PdlLoggingMessageFactoryImpl(getPdlLoggingProperties());
    }

    @Test
    public void buildPdlLogMessage() {
        var pdlLogMessage = testee.buildLogMessage(buildLogMessage(), createIbUser());

        assertEquals(PDL_LOGGING_SYSTEMID, pdlLogMessage.getSystemId());
        assertEquals(PDL_LOGGING_SYSTEMNAME, pdlLogMessage.getSystemName());

        // verify activity
        assertEquals(ActivityType.READ, pdlLogMessage.getActivityType());
        assertEquals(LogEvent.BESTALLNING_OPPNAS_OCH_LASES.getActivityArgs(), pdlLogMessage.getActivityArgs());
        assertEquals(ACTIVITY_LEVEL, pdlLogMessage.getActivityLevel());
        assertEquals(ActivityPurpose.CARE_TREATMENT, pdlLogMessage.getPurpose());

        // verify resource
        assertEquals(1, pdlLogMessage.getPdlResourceList().size());

        var logResource = pdlLogMessage.getPdlResourceList().get(0);
        assertEquals(ResourceType.RESOURCE_TYPE_BESTALLNING.getResourceTypeName(), logResource.getResourceType());
        assertEquals(VE_ID, logResource.getResourceOwner().getEnhetsId());
        assertEquals(VE_NAME, logResource.getResourceOwner().getEnhetsNamn());
        assertEquals(VG_ID, logResource.getResourceOwner().getVardgivareId());
        assertEquals(VG_NAME, logResource.getResourceOwner().getVardgivareNamn());
        assertEquals(PATIENT_ID.replace("-", ""), logResource.getPatient().getPatientId());
        assertEquals("", logResource.getPatient().getPatientNamn());

        // verify user
        assertNull(pdlLogMessage.getUserAssignment());
        assertEquals(USER_ID, pdlLogMessage.getUserId());
        assertEquals(USER_NAME, pdlLogMessage.getUserName());
        assertEquals(USER_TITLE, pdlLogMessage.getUserTitle());
        assertEquals(VE_ID, pdlLogMessage.getUserCareUnit().getEnhetsId());
        assertEquals(VE_NAME, pdlLogMessage.getUserCareUnit().getEnhetsNamn());
        assertEquals(VG_ID, pdlLogMessage.getUserCareUnit().getVardgivareId());
        assertEquals(VG_NAME, pdlLogMessage.getUserCareUnit().getVardgivareNamn());
    }

    private LogMessage buildLogMessage() {
        LogActivity logActivity = buildLogActivity();
        List<LogResource> resources = buildLogResources();

        return new LogMessage(logActivity, resources);
    }

    private LogActivity buildLogActivity() {
        return new LogActivity(ACTIVITY_LEVEL, LogEvent.BESTALLNING_OPPNAS_OCH_LASES);
    }

    private List<LogResource> buildLogResources() {
        var logResource = new LogResource(PATIENT_ID, VE_ID, VG_ID);
        logResource.setEnhetsNamn(VE_NAME);
        logResource.setVardgivareNamn(VG_NAME);

        return Collections.singletonList(logResource);
    }

    private IntygUser createDefaultUser() {
        return createUser(AuthoritiesConstants.ROLE_VARDADMIN,
                createPrivilege(AuthoritiesConstants.PRIVILEGE_LISTA_BESTALLNINGAR,
                        Collections.EMPTY_LIST,
                        Collections.EMPTY_LIST),
                Collections.EMPTY_MAP,
                UserOriginType.NORMAL.name());
    }

    private Privilege createPrivilege(String name, List<String> intygsTyper, List<RequestOrigin> requestOrigins) {
        Privilege p = new Privilege();
        p.setName(name);
        p.setIntygstyper(intygsTyper);
        p.setRequestOrigins(requestOrigins);
        return p;
    }

    private IntygUser createUser(String roleName, Privilege p, Map<String, Feature> features, String origin) {
        IntygUser user = new IntygUser(USER_ID);
        user.setTitel(USER_TITLE);

        HashMap<String, Privilege> pMap = new HashMap<>();
        pMap.put(p.getName(), p);
        user.setAuthorities(pMap);

        user.setOrigin(origin);
        user.setFeatures(features);

        HashMap<String, Role> rMap = new HashMap<>();
        Role role = new Role();
        role.setName(roleName);
        rMap.put(roleName, role);

        user.setRoles(rMap);
        return user;
    }

    private IntygsbestallningUser createIbUser() {
        var ibUser = new IntygsbestallningUser(createDefaultUser());
        ibUser.setHsaId(USER_ID);
        ibUser.setNamn(USER_NAME);
        ibUser.setUnitContext(createIbVardenhet());
        return ibUser;
    }

    private IbVardgivare createIbVardgivare() {
        return new IbVardgivare(VG_ID, VG_NAME);
    }

    private IbVardenhet createIbVardenhet() {
        final IbVardgivare ibVardgivare = createIbVardgivare();
        return new IbVardenhet(VE_ID, VE_NAME, ibVardgivare.getId(), ibVardgivare.getName(), VG_ORGNR);
    }

    private PdlLoggingProperties getPdlLoggingProperties() {
        return new PdlLoggingProperties() {{
            setSystemId(PDL_LOGGING_SYSTEMID);
            setSystemName(PDL_LOGGING_SYSTEMNAME);
            setQueueName(PDL_LOGGING_QUEUENAME);
        }};
    }

}