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

package se.inera.intyg.intygsbestallning.web.service.pdl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import se.inera.intyg.infra.logmessages.ActivityType;
import se.inera.intyg.infra.logmessages.PdlLogMessage;
import se.inera.intyg.infra.security.common.model.Feature;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.RequestOrigin;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.infra.security.common.model.UserOriginType;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants;
import se.inera.intyg.intygsbestallning.web.pdl.LogEvent;
import se.inera.intyg.intygsbestallning.web.pdl.LogMessage;
import se.inera.intyg.intygsbestallning.web.pdl.PdlLoggingMessageFactory;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;
import se.inera.intyg.schemas.contract.Personnummer;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Magnus Ekstrand on 2019-05-07.
 */
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class LogServiceImplTest {

    private static final String USER_ID = "SE2321000198-016965";
    private static final String USER_NAME = "";
    private static final String USER_TITLE = "Vårdadministratör";
    private static final String VG_ID = "vardgivare-1";
    private static final String VG_NAME = "vardgivare-namn";
    private static final String VG_ORGNR = "vardgivare-orgnr";
    private static final String VE_ID = "vardenhet-1";
    private static final String VE_NAME = "vardenhet-nanm";
    private static final String PATIENT_ID = "19121212-1212";

    @Mock
    private PdlLoggingMessageFactory pdlLoggingMessageFactory;

    @Mock
    private UserService userService;

    @InjectMocks
    private LogService logService = new LogServiceImplStub();

    @BeforeEach
    void setup() {
        when(userService.getUser()).thenReturn(buildIbUser());
    }

    @Test
    void whenLoggingSingleBestallning_thenOnlyOnePdlLogMessage() {
        ArgumentCaptor<LogMessage> arg = ArgumentCaptor.forClass(LogMessage.class);

        logService.log("100", PATIENT_ID, VE_ID, VG_ID, LogEvent.BESTALLNING_OPPNAS_OCH_LASES);

        verify(pdlLoggingMessageFactory, times(1)).buildLogMessage(arg.capture(), any(IntygsbestallningUser.class));

        assertEquals("100", arg.getValue().getActivity().getActivityLevel());
        assertEquals(ActivityType.READ, arg.getValue().getActivity().getEvent().getActivityType());
        assertEquals("Beställning läst", arg.getValue().getActivity().getEvent().getActivityArgs());
        assertEquals(1, arg.getValue().getResources().size());
        assertEquals(PATIENT_ID, arg.getValue().getResources().get(0).getPatientId());
        assertEquals(VE_ID, arg.getValue().getResources().get(0).getEnhetsId());
        assertEquals(VG_ID, arg.getValue().getResources().get(0).getVardgivareId());
    }

    @Test
    void whenLoggingMultipleBestallningar_thenOnlyOnePdlLogMessagesPerPatient() {
        ArgumentCaptor<LogMessage> arg = ArgumentCaptor.forClass(LogMessage.class);

        logService.logList(createBestallningar(
                List.of(BestallningStatus.OLAST, BestallningStatus.LAST, BestallningStatus.ACCEPTERAD)),
                LogEvent.PERSONINFORMATION_VISAS_I_LISTA);

        verify(pdlLoggingMessageFactory, times(2)).buildLogMessage(arg.capture(), any(IntygsbestallningUser.class));
        assertNotEquals(arg.getAllValues().get(0).getResources().get(0).getPatientId(), arg.getAllValues().get(1).getResources().get(0).getPatientId());
    }

    @Test
    void whenLogEventIsPersoninformationVisasILista_thenAssertActivityLevel() {
        ArgumentCaptor<LogMessage> arg = ArgumentCaptor.forClass(LogMessage.class);

        logService.log("100", PATIENT_ID, VE_ID, VG_ID, LogEvent.PERSONINFORMATION_VISAS_I_LISTA);

        verify(pdlLoggingMessageFactory).buildLogMessage(arg.capture(), any(IntygsbestallningUser.class));
        assertEquals("Översikt", arg.getValue().getActivity().getActivityLevel());
    }

    private Privilege createPrivilege(String name, List<String> intygsTyper, List<RequestOrigin> requestOrigins) {
        Privilege p = new Privilege();
        p.setName(name);
        p.setIntygstyper(intygsTyper);
        p.setRequestOrigins(requestOrigins);
        return p;
    }

    private IntygUser buildDefaultUser() {
        return buildUser(AuthoritiesConstants.ROLE_VARDADMIN,
                createPrivilege(AuthoritiesConstants.PRIVILEGE_LISTA_BESTALLNINGAR,
                        Collections.EMPTY_LIST,
                        Collections.EMPTY_LIST),
                Collections.EMPTY_MAP,
                UserOriginType.NORMAL.name());
    }

    private IntygUser buildUser(String roleName, Privilege p, Map<String, Feature> features, String origin) {
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

    private IntygsbestallningUser buildIbUser() {
        var ibUser = new IntygsbestallningUser(buildDefaultUser());
        ibUser.setHsaId(USER_ID);
        ibUser.setNamn(USER_NAME);
        ibUser.setUnitContext(buildIbVardenhet());
        return ibUser;
    }

    private IbVardgivare buildIbVardgivare() {
        return new IbVardgivare(VG_ID, VG_NAME);
    }

    private IbVardenhet buildIbVardenhet() {
        final IbVardgivare ibVardgivare = buildIbVardgivare();
        return new IbVardenhet(VE_ID, VE_NAME, ibVardgivare.getId(), ibVardgivare.getName(), VG_ORGNR);
    }

    private Vardenhet buildVardenhet() {
        return Vardenhet.Factory.newVardenhet(VE_ID, VG_ID, VG_ORGNR, VG_NAME, "epost@mail.se");
    }

    private Handlaggare buildHandlaggare() {
        return Handlaggare.Factory.newHandlaggare(
                "Handlaggaren", "073-123", "myndigheten", "kontor", "kostnadsställe", "adress", "12345", "Staden");
    }

    private Invanare buildInvanare(String personnummer) {
        return Invanare.Factory.newInvanare(
                Personnummer.createPersonnummer(personnummer).get(), "Läge");
    }

    private List<Bestallning> createBestallningar(List<BestallningStatus> statusar) {
        var bestallningar = List.of(
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("20121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref"),
                Bestallning.Factory.newBestallning(buildInvanare("19121212-1212"), "syfte", "insatser", buildHandlaggare(), "F1.0_AF00213", "AF00213", "detta är beskrivningen", buildVardenhet(), "ref")
        );

        var randomIndex = new Random().nextInt(statusar.size());
        var longId = new AtomicLong();

        for (var bestallning : bestallningar) {
            Field idField = ReflectionUtils.findField(Bestallning.class, "id");
            ReflectionUtils.makeAccessible(idField);
            ReflectionUtils.setField(idField, bestallning, longId.getAndIncrement());
            bestallning.setStatus(statusar.get(randomIndex));
        }

        return bestallningar;
    }

    class LogServiceImplStub extends LogServiceImpl {
        @Override
        void send(PdlLogMessage pdlLogMessage) {
            //doNothing
        }
    }
}
