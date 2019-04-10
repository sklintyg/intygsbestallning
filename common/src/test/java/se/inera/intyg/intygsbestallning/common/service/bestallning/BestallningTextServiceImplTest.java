package se.inera.intyg.intygsbestallning.common.service.bestallning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

@ActiveProfiles("test")
@TestPropertySource("classpath:test.properties")
@EnableAutoConfiguration
@SpringJUnitConfig(classes = {CommonConfig.class})
class BestallningTextServiceImplTest {

    private static final String SUPPORTED_BESTALLNING_TYP = "TEST";
    private static final Double SUPPORTED_BESTALLNING_TYP_VERSION = 1.0;

    private static final String UNSUPPORTED_BESTALLNING_TYP = "TEST_UNSUPPORTED";
    private static final Double UNSUPPORTED_BESTALLNING_TYP_VERSION = 1.1;

    @Autowired
    private BestallningTextServiceImpl textService;

    @Test
    void getBestallningTextWithSupportedTyp() {
        var bestallning = buildBestallning(SUPPORTED_BESTALLNING_TYP, SUPPORTED_BESTALLNING_TYP_VERSION);
        var texter = textService.getBestallningTexter(bestallning);

        assertThat(texter).isNotNull();

        assertThat(texter.getTyp()).isEqualTo(SUPPORTED_BESTALLNING_TYP);
        assertThat(Double.valueOf(texter.getVersion())).isEqualTo(SUPPORTED_BESTALLNING_TYP_VERSION);

        assertThat(texter.getTyp()).isNotNull();
        assertThat(texter.getVersion()).isNotNull();
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
        var bestallning = buildBestallning(UNSUPPORTED_BESTALLNING_TYP, UNSUPPORTED_BESTALLNING_TYP_VERSION);
        assertThatThrownBy(() -> textService.getBestallningTexter(bestallning))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage(
                        "Bestallning text resources for bestallning is not supported for Type: " + UNSUPPORTED_BESTALLNING_TYP +
                                " and Version: " + UNSUPPORTED_BESTALLNING_TYP_VERSION);
    }

    private Bestallning buildBestallning(String typ, Double version) {
        return Bestallning.Factory.newBestallning(
                Invanare.Factory.newInvanare(Personnummer.createPersonnummer("19121212-1212").get(), null),
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
                version,
                Vardenhet.Factory.newVardenhet(
                        "hsa",
                        "vardGivareHsa",
                        "orgId",
                        "namn",
                        "e@mail.se",
                        "svar"
                ),
                "referens"
        );
    }
}
