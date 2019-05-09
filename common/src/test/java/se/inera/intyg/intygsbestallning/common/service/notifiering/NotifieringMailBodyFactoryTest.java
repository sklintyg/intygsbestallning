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

import static org.assertj.core.api.Assertions.assertThat;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
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
class NotifieringMailBodyFactoryTest {

    private static final Long BESTALLNING_ID = 1L;
    private static final LocalDateTime ANKOMST_DATUM = LocalDateTime.now().minusDays(2L);

    @Mock
    private BestallningProperties bestallningProperties;

    @InjectMocks
    private NotifieringMailBodyFactory notifieringMailBodyFactory;

    @BeforeEach
    void setup() {
        Field hostField = ReflectionUtils.findField(BestallningProperties.class, "host");
        ReflectionUtils.makeAccessible(hostField);
        ReflectionUtils.setField(hostField, bestallningProperties, "host");
    }

    @Test
    void testBuildBody() {
        var notifieringTyp = NotifieringTyp.NY_BESTALLNING_PAMINNELSE_2;
        var bestallningTyp = "F1.0_AF00213";

        var mailTexter = buildMailTexter(bestallningTyp, notifieringTyp);
        var bestallning = buildBestallning(bestallningTyp);

        var html = notifieringMailBodyFactory.buildBody(bestallning, mailTexter, "url");

        assertThat(html).contains("<h1>" + mailTexter.getHalsning().getText() + "</h1>");
        assertThat(html).contains("<p>" + mailTexter.getBody().getText1() + "</p>");
        assertThat(html).contains("<a href=\"" + "url" +  "\">url</a>");
        assertThat(html).contains("src=\"" + "host/images/" + mailTexter.getLogo() +  "\"");
        assertThat(html).contains("<p>" + mailTexter.getFooter().getText() + "</p>");
    }

    private MailTexter buildMailTexter(String bestallningTyp, NotifieringTyp notifieringTyp) {
        return new MailTexter(
                "logo.png",
                bestallningTyp,
                notifieringTyp.name(),
                new ArendeRad("detta är ett ärende"),
                new Halsning("detta är en hälsning"),
                new Body("text1", "text2"),
                new Footer("detta är en footer")
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

}
