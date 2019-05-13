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
package se.inera.intyg.intygsbestallning.common.service.notifiering;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import javax.mail.MessagingException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.BestallningStatus;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Notifiering;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.property.BestallningProperties;
import se.inera.intyg.intygsbestallning.common.text.mail.ArendeRad;
import se.inera.intyg.intygsbestallning.common.text.mail.Body;
import se.inera.intyg.intygsbestallning.common.text.mail.Footer;
import se.inera.intyg.intygsbestallning.common.text.mail.Halsning;
import se.inera.intyg.intygsbestallning.common.text.mail.MailTexter;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class NotifieringSendServiceImplTest {

    private static final Long BESTALLNING_ID = 1L;
    private static final LocalDateTime ANKOMST_DATUM = LocalDateTime.now().minusDays(2L);

    @Mock
    private MailTextService mailTextService;

    @Mock
    private BestallningProperties bestallningProperties;

    @Mock
    private MailBodyFactory mailBodyFactory;

    @Mock
    private MailService mailService;

    @InjectMocks
    private NotifieringSendServiceImpl notifieringSendService;

    @BeforeEach
    void setup() {
        Field hostField = ReflectionUtils.findField(BestallningProperties.class, "host");
        ReflectionUtils.makeAccessible(hostField);
        ReflectionUtils.setField(hostField, bestallningProperties, "host");
    }

    @Test
    void testNyBestallning() throws MessagingException {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING;
        var bestallningTyp = "F1.0_AF00213";

        var mailTexter = buildMailTexter(bestallningTyp, notifieringTyp);
        var mockedMailBody = "<p>detta är en mailbody</p>";
        var bestallning = buildBestallning(bestallningTyp);

        when(mailTextService.getMailContent(eq(notifieringTyp), eq(bestallningTyp))).thenReturn(mailTexter);
        when(mailBodyFactory.buildBody(eq(bestallning), eq(mailTexter), anyString())).thenReturn(mockedMailBody);

        notifieringSendService.nyBestallning(bestallning);

        verify(mailTextService, times(1)).getMailContent(eq(notifieringTyp), eq(bestallningTyp));
        verify(mailBodyFactory, times(1)).buildBody(eq(bestallning), eq(mailTexter), anyString());
        verify(mailService, times(1)).sendNotifieringToVardenhet(
                eq(bestallning.getVardenhet().getEpost()),
                eq(mailTexter.getArendeRad().getArende()),
                eq(mockedMailBody)
        );
    }

    @Test
    void testNyBestallningWithNoNotifieringar() {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING;
        var bestallningTyp = "F1.0_AF00213";

        var bestallning = buildBestallning(bestallningTyp);
        bestallning.setNotifieringar(List.of());

        assertThatThrownBy(() -> notifieringSendService.paminnelse(bestallning, notifieringTyp))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("notifieringList may not be null or empty");

        verifyZeroInteractions(mailTextService);
        verifyZeroInteractions(mailBodyFactory);
        verifyZeroInteractions(mailService);
    }

    @Test
    void testVidareBefordrad() {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING;
        var bestallningTyp = "F1.0_AF00213";

        var mailTexter = buildMailTexter(bestallningTyp, notifieringTyp);
        var mockedMailBody = "<p>detta är en mailbody</p>";
        var bestallning = buildBestallning(bestallningTyp);

        when(mailTextService.getMailContent(eq(notifieringTyp), eq(bestallningTyp))).thenReturn(mailTexter);
        when(mailBodyFactory.buildBodyRawText(eq(bestallning), eq(mailTexter), anyString())).thenReturn(mockedMailBody);

        notifieringSendService.vidarebefordrad(bestallning);

        verify(mailTextService, times(1)).getMailContent(eq(notifieringTyp), eq(bestallningTyp));
        verify(mailBodyFactory, times(1)).buildBodyRawText(eq(bestallning), eq(mailTexter), anyString());
        verifyZeroInteractions(mailService);

    }

    @Test
    void testPaminnelse1() throws MessagingException {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING_PAMINNELSE_1;
        var bestallningTyp = "F1.0_AF00213";

        var mailTexter = buildMailTexter(bestallningTyp, notifieringTyp);
        var mockedMailBody = "<p>detta är en mailbody</p>";
        var bestallning = buildBestallning(bestallningTyp);

        when(mailTextService.getMailContent(eq(notifieringTyp), eq(bestallningTyp))).thenReturn(mailTexter);
        when(mailBodyFactory.buildBody(eq(bestallning), eq(mailTexter), anyString())).thenReturn(mockedMailBody);

        notifieringSendService.paminnelse(bestallning, notifieringTyp);

        verify(mailTextService, times(1)).getMailContent(eq(notifieringTyp), eq(bestallningTyp));
        verify(mailBodyFactory, times(1)).buildBody(eq(bestallning), eq(mailTexter), anyString());
        verify(mailService, times(1)).sendNotifieringToVardenhet(
                eq(bestallning.getVardenhet().getEpost()),
                eq(mailTexter.getArendeRad().getArende()),
                eq(mockedMailBody)
        );
    }

    @Test
    void testPaminnelse2() throws MessagingException {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2;
        var bestallningTyp = "F1.0_AF00213";

        var mailTexter = buildMailTexter(bestallningTyp, notifieringTyp);
        var mockedMailBody = "<p>detta är en mailbody</p>";
        var bestallning = buildBestallning(bestallningTyp);

        when(mailTextService.getMailContent(eq(notifieringTyp), eq(bestallningTyp))).thenReturn(mailTexter);
        when(mailBodyFactory.buildBody(eq(bestallning), eq(mailTexter), anyString())).thenReturn(mockedMailBody);

        notifieringSendService.paminnelse(bestallning, notifieringTyp);

        verify(mailTextService, times(1)).getMailContent(eq(notifieringTyp), eq(bestallningTyp));
        verify(mailBodyFactory, times(1)).buildBody(eq(bestallning), eq(mailTexter), anyString());
        verify(mailService, times(1)).sendNotifieringToVardenhet(
                eq(bestallning.getVardenhet().getEpost()),
                eq(mailTexter.getArendeRad().getArende()),
                eq(mockedMailBody)
        );
    }

    private Invanare buildInvanare() {
        Personnummer personnummer = createPersonnummer("191212121212").get();
        return new Invanare(personnummer, "");
    }

    private Bestallning buildBestallning(String bestallingTyp) {
        return new Bestallning(
                BESTALLNING_ID,
                bestallingTyp,
                "AF00213",
                "detta är beskrivningen",
                ANKOMST_DATUM,
                null,
                "",
                "",
                BestallningStatus.LAST,
                buildInvanare(),
                buildHandlaggare(),
                buildVardenhet(),
                "referens",
                Lists.newArrayList(),
                List.of(Notifiering.Factory.nyBestallning("hsaId")));
    }

    private Vardenhet buildVardenhet() {
        return new Vardenhet("hsa-id", "", "", "", "");
    }

    private Handlaggare buildHandlaggare() {
        return new Handlaggare(1L, "", "", "email", "", "", "", "", "", "");
    }

    private MailTexter buildMailTexter(String bestallningTyp, NotifieringTyp notifieringTyp) {
        return new MailTexter(
                "logo.png",
                bestallningTyp,
                notifieringTyp.name(),
                new ArendeRad("detta är ett ärende"),
                new Halsning("detta är en hälsning"),
                new Body("text1", "text2"),
                new Footer("detta är en footer", "detta är en footer")
        );
    }
}
