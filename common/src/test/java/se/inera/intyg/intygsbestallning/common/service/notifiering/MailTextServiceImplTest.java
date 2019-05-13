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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@EnableAutoConfiguration
@SpringJUnitConfig(classes = {CommonConfig.class})
@TestPropertySource("classpath:test.properties")
class MailTextServiceImplTest {

    private static final NotifieringTyp SUPPORTED_TYP = NotifieringTyp.NY_BESTALLNING;

    private static final String SUPPORTED_INTYG_TYP = "TEST_INTYG";
    private static final String UNSUPPORTED_INTYG_TYP = "UNSUPPORTED_INTYG";

    @Autowired
    private MailTextServiceImpl mailTextService;

    @Test
    void getMailTextForSupportedIntygAndTyp() {
        var mailContent = mailTextService.getMailContent(SUPPORTED_TYP, SUPPORTED_INTYG_TYP);

        assertThat(mailContent).isNotNull();

        assertThat(mailContent.getTyp()).isEqualTo(SUPPORTED_TYP.name());
        assertThat(mailContent.getIntyg()).isEqualTo(SUPPORTED_INTYG_TYP);

        assertThat(mailContent.getArendeRad()).isNotNull();
        assertThat(mailContent.getArendeRad().getArende()).isNotNull();

        assertThat(mailContent.getBody()).isNotNull();
        assertThat(mailContent.getBody().getText1()).isNotNull();
        assertThat(mailContent.getBody().getText2()).isNotNull();

        assertThat(mailContent.getHalsning()).isNotNull();
        assertThat(mailContent.getHalsning().getText()).isNotNull();

        assertThat(mailContent.getFooter()).isNotNull();
        assertThat(mailContent.getFooter().getText1()).isNotNull();
        assertThat(mailContent.getFooter().getText2()).isNotNull();
    }

    @Test
    void getMailTextForUnsupported() {
        assertThatThrownBy(() -> mailTextService.getMailContent(SUPPORTED_TYP, UNSUPPORTED_INTYG_TYP))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mail Content for type: NY_BESTALLNING and intyg: UNSUPPORTED_INTYG does not exist");
    }
}
