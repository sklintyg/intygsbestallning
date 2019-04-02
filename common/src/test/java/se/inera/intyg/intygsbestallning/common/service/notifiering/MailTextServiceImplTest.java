package se.inera.intyg.intygsbestallning.common.service.notifiering;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import se.inera.intyg.intygsbestallning.common.CommonConfig;
import se.inera.intyg.intygsbestallning.common.domain.NotifieringTyp;

@ActiveProfiles("test")
@TestPropertySource("classpath:test.properties")
@EnableAutoConfiguration
@SpringJUnitConfig(classes = {CommonConfig.class})
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
        assertThat(mailContent.getFooter().getText()).isNotNull();
    }

    @Test
    void getMailTextForUnsupported() {
        assertThatThrownBy(() -> mailTextService.getMailContent(SUPPORTED_TYP, UNSUPPORTED_INTYG_TYP))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("Mail Content for type: NY_BESTALLNING and intyg: UNSUPPORTED_INTYG does not exist");
    }
}
