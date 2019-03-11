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
package se.inera.intyg.intygsbestallning.web.auth.service;

import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;
import se.inera.intyg.infra.integration.hsa.model.UserCredentials;
import se.inera.intyg.infra.integration.hsa.model.Vardenhet;
import se.inera.intyg.infra.integration.hsa.model.Vardgivare;
import se.inera.intyg.infra.security.common.exception.GenericAuthenticationException;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.siths.BaseSakerhetstjanstAssertion;
import se.inera.intyg.infra.security.siths.BaseUserDetailsService;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants;
import se.inera.intyg.intygsbestallning.web.auth.exceptions.MissingIBSystemRoleException;
import se.inera.intyg.intygsbestallning.web.auth.model.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.model.IbVardgivare;
import se.riv.infrastructure.directory.v1.PersonInformationType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
@Service
public class IntygsbestallningUserDetailsService extends BaseUserDetailsService implements SAMLUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(IntygsbestallningUserDetailsService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        if (credential == null) {
            throw new GenericAuthenticationException("SAMLCredential has not been set.");
        }
        LOG.info("ENTER - loadUserBySAML with relayState " + credential.getRelayState());
        return super.loadUserBySAML(credential);
    }

    @Override
    protected IntygsbestallningUser buildUserPrincipal(SAMLCredential credential) {
        // All IB customization is done in the overridden decorateXXX methods, so just return a new IbUSer
        IntygUser intygUser = super.buildUserPrincipal(credential);
        IntygsbestallningUser ibUser = new IntygsbestallningUser(intygUser);
        ibUser.setPossibleRoles(commonAuthoritiesResolver.getRoles());
        buildSystemAuthoritiesTree(ibUser);

        if (ibUser.getSystemAuthorities().size() == 0) {
            throw new MissingIBSystemRoleException(ibUser.getHsaId());
        }

        // If only a single possible entity to select as loggedInAt exists, do that...
        tryToSelectHsaEntity(ibUser);

        return ibUser;
    }


    protected void tryToSelectHsaEntity(IntygsbestallningUser ibUser) {
        if (ibUser.getTotaltAntalVardenheter() == 1) {
            ibUser.changeValdVardenhet(ibUser.getVardgivare().get(0).getVardenheter().get(0).getId());
        }
    }

    /**
     * Overridden for IB. We cannot use "fallback" roles here so we must postpone population of this.roles.
     *
     * @param intygUser
     * @param personInfo
     * @param userCredentials
     */
    @Override
    protected void decorateIntygUserWithRoleAndAuthorities(IntygUser intygUser, List<PersonInformationType> personInfo,
                                                           UserCredentials userCredentials) {
       // Do nothing
    }

    @Override
    protected void decorateIntygUserWithDefaultVardenhet(IntygUser intygUser) {
        // Only set a default enhet if there is only one (mottagningar doesnt count).
        // If no default vardenhet can be determined - let it be null and force user to select one.
        if (getTotaltAntalVardenheterExcludingMottagningar(intygUser) == 1) {
            super.decorateIntygUserWithDefaultVardenhet(intygUser);
        }
    }

    public void buildSystemAuthoritiesTree(IntygsbestallningUser user) {
        List<IbVardgivare> authSystemTree = new ArrayList<>();

        for (Vardgivare vg : user.getVardgivare()) {
            IbVardgivare ibVardgivare = new IbVardgivare(vg.getId(), vg.getNamn(), false);
            for (Vardenhet ve : vg.getVardenheter()) {
                ibVardgivare.getVardenheter().add(new IbVardenhet(ve.getId(), ve.getNamn(), ibVardgivare, ve.getVardgivareOrgnr()));
            }
            authSystemTree.add(ibVardgivare);
        }
        user.setSystemAuthorities(authSystemTree);
    }

    private int getTotaltAntalVardenheterExcludingMottagningar(IntygUser intygUser) {
        // count all vardenheter (not including mottagningar under vardenheter)
        return (int) intygUser.getVardgivare().stream().flatMap(vg -> vg.getVardenheter().stream()).count();
    }

    @Override
    protected void decorateIntygUserWithSystemRoles(IntygUser intygUser, UserCredentials userCredentials) {
        super.decorateIntygUserWithSystemRoles(intygUser, userCredentials);
    }

    @Override
    protected String getDefaultRole() {
        return AuthoritiesConstants.ROLE_BP_VARDADMIN;
    }

    @Override
    protected BaseSakerhetstjanstAssertion getAssertion(Assertion assertion) {
        return super.getAssertion(assertion);
    }

}
