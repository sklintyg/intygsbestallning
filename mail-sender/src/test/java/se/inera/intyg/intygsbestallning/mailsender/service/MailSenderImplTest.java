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
package se.inera.intyg.intygsbestallning.mailsender.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.intygsbestallning.common.mail.NotificationEmail;
import se.inera.intyg.intygsbestallning.mailsender.exception.PermanentException;
import se.inera.intyg.intygsbestallning.mailsender.exception.TemporaryException;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class MailSenderImplTest {

    @InjectMocks
    private MailSenderImpl testee;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(testee, "fromAddress", "somefrom@inera.se");
    }

    @Test
    public void testSendMail() throws Exception {
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));

        String json = buildNotificationMailAsJson();
        testee.process(json);
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendMailThrowsTemporaryExceptionWhenProblemWithSend() throws Exception {
        when(javaMailSender.createMimeMessage()).thenReturn(mock(MimeMessage.class));
        doThrow(new MailAuthenticationException("Faked problem")).when(javaMailSender).send(any(MimeMessage.class));

        String json = buildNotificationMailAsJson();

        assertThrows(TemporaryException.class, () -> {
            testee.process(json);
        });

        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void testSendMailThrowsPermanentExceptionOnUnparsableJSON() throws Exception {
        String json = "this is not json}";

        assertThrows(PermanentException.class, () -> {
            testee.process(json);
        });

        verify(javaMailSender, times(0)).send(any(MimeMessage.class));
    }

    private String buildNotificationMailAsJson() throws JsonProcessingException {
        NotificationEmail email = new NotificationEmail("some.mail@inera.se","The subject","Feel it");
        return new ObjectMapper().writeValueAsString(email);

    }

}
