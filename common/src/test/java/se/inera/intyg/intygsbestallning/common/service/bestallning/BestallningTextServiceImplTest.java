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
package se.inera.intyg.intygsbestallning.common.service.bestallning;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.schemas.contract.Personnummer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@EnableAutoConfiguration
@SpringJUnitConfig(classes = {CommonConfig.class})
@TestPropertySource("classpath:test.properties")
class BestallningTextServiceImplTest {

    private static final String SUPPORTED_BESTALLNING_TYP = "TEST";

    private static final String UNSUPPORTED_BESTALLNING_TYP = "TEST_UNSUPPORTED";

    @Autowired
    private BestallningTextServiceImpl textService;

    @Test
    void getBestallningTextWithSupportedTyp() {
        var bestallning = buildBestallning(SUPPORTED_BESTALLNING_TYP);
        var texter = textService.getBestallningTexter(bestallning);

        assertThat(texter).isNotNull();

        assertThat(texter.getTyp()).isEqualTo(SUPPORTED_BESTALLNING_TYP);

        assertThat(texter.getTyp()).isNotNull();
        assertThat(texter.getGiltigFrom()).isNotNull();
        assertThat(texter.getBild()).isNotNull();

        assertThat(texter.getTexter()).hasSize(2);

        assertThat(texter.getTexter().get(0).getId()).isEqualTo("TEST_1");
        assertThat(texter.getTexter().get(0).getValue()).isEqualTo("Text tillhörande id TEST_1");

        assertThat(texter.getTexter().get(1).getId()).isEqualTo("TEST_2");
        assertThat(texter.getTexter().get(1).getValue()).isEqualTo("Text tillhörande id TEST_2");
    }

    @Test
    void getBestallningTextWithUnsupportedTypOrVersion() {
        var bestallning = buildBestallning(UNSUPPORTED_BESTALLNING_TYP);
        assertThatThrownBy(() -> textService.getBestallningTexter(bestallning))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Bestallning text resources for bestallning is not supported for Type: " + UNSUPPORTED_BESTALLNING_TYP);
    }

    private Bestallning buildBestallning(String typ) {
        return Bestallning.Factory.newBestallning(
                Invanare.Factory.newInvanare(Personnummer.createPersonnummer("19121212-1212").get(), null),
                "syfte",
                "insats",
                Handlaggare.Factory.newHandlaggare(
                        "namn",
                        "073-000",
                        "e@mail-se",
                        "myndigheten",
                        "kontor",
                        "kostställe",
                        "adressen",
                        "postnr",
                        "stad"
                ),
                typ,
                "AF00213",
                "detta är beskrivningen",
                Vardenhet.Factory.newVardenhet(
                        "hsa",
                        "vardGivareHsa",
                        "orgId",
                        "namn",
                        "e@mail.se"
                ),
                "referens"
        );
    }
}
