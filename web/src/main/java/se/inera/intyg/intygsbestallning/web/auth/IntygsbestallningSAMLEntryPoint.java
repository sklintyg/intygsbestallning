/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.intygsbestallning.web.auth;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import se.inera.intyg.infra.security.common.model.AuthConstants;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Customized SAMLEntryPoint for IB that provides two customizations:
 *
 * 1. Tries to parse the "targetApplication" query parameter of the SAML login request. Allowed values are FMU or BP.
 * If null or empty string, defaults to FMU. If an unknown value, throws exception.
 *
 * 2. Specifies the {@link AuthConstants#URN_OASIS_NAMES_TC_SAML_2_0_AC_CLASSES_TLSCLIENT} authContext for SITHS.
 *
 * @author eriklupander
 */
public class IntygsbestallningSAMLEntryPoint extends SAMLEntryPoint {

    private static final String TARGET_APP = "targetApplication";
    private static final Logger LOG = LoggerFactory.getLogger(IntygsbestallningSAMLEntryPoint.class);

    /**
     * Override from superclass, see class comment for details.
     *
     * @param context
     *            containing local entity
     * @param exception
     *            exception causing invocation of this entry point (can be null)
     * @return populated webSSOprofile
     * @throws MetadataProviderException
     *             in case metadata loading fails
     */
    @Override
    protected WebSSOProfileOptions getProfileOptions(SAMLMessageContext context, AuthenticationException exception)
            throws MetadataProviderException {

        WebSSOProfileOptions ssoProfileOptions = new WebSSOProfileOptions();
        ssoProfileOptions.setAuthnContexts(buildTlsClientAuthContexts());
        return ssoProfileOptions;
    }

    private Collection<String> buildTlsClientAuthContexts() {
        Set<String> set = new HashSet<>();
        set.add(AuthConstants.URN_OASIS_NAMES_TC_SAML_2_0_AC_CLASSES_TLSCLIENT);
        return set;
    }
}
