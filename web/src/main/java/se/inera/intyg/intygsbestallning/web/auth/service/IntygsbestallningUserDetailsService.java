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
package se.inera.intyg.intygsbestallning.web.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.opensaml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.stereotype.Service;

import se.inera.intyg.infra.integration.hsa.model.UserCredentials;
import se.inera.intyg.infra.integration.hsa.model.Vardenhet;
import se.inera.intyg.infra.integration.hsa.model.Vardgivare;
import se.inera.intyg.infra.security.authorities.AuthoritiesResolverUtil;
import se.inera.intyg.infra.security.common.exception.GenericAuthenticationException;
import se.inera.intyg.infra.security.common.model.IntygUser;
import se.inera.intyg.infra.security.common.model.Privilege;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.infra.security.exception.MissingMedarbetaruppdragException;
import se.inera.intyg.infra.security.siths.BaseSakerhetstjanstAssertion;
import se.inera.intyg.infra.security.siths.BaseUserDetailsService;
import se.inera.intyg.intygsbestallning.web.auth.IbVardenhet;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.auth.authorities.AuthoritiesConstants;
import se.riv.infrastructure.directory.v1.PersonInformationType;

@Service
public class IntygsbestallningUserDetailsService extends BaseUserDetailsService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(IntygsbestallningUserDetailsService.class);

    @Override
    public Object loadUserBySAML(SAMLCredential credential) {
        if (credential == null) {
            throw new GenericAuthenticationException("SAMLCredential has not been set.");
        }
        LOG.info("ENTER - loadUserBySAML");
        return super.loadUserBySAML(credential);
    }

    @Override
    protected IntygsbestallningUser buildUserPrincipal(SAMLCredential credential) {
        // All IB customization is done in overridden decorateXXX methods, so just return a new IbUSer
        IntygUser intygUser = super.buildUserPrincipal(credential);
        IntygsbestallningUser ibUser = new IntygsbestallningUser(intygUser);
        ibUser.setPossibleRoles(commonAuthoritiesResolver.getRoles());

        buildSystemAuthoritiesTree(ibUser);

        if (ibUser.getSystemAuthorities().size() == 0) {
            throw new MissingMedarbetaruppdragException(ibUser.getHsaId());
        }

        // If only a single possible entity to select as loggedInAt exists, do that...
        tryToSelectHsaEntity(ibUser);

        return ibUser;
    }

    protected void buildSystemAuthoritiesTree(IntygsbestallningUser user) {
        List<IbVardgivare> authSystemTree = new ArrayList<>();

        for (Vardgivare vg : user.getVardgivare()) {
            IbVardgivare ibVardgivare = new IbVardgivare(vg.getId(), vg.getNamn());
            for (Vardenhet ve : vg.getVardenheter()) {
                ibVardgivare.getVardenheter().add(new IbVardenhet(ve.getId(), ve.getNamn(), ibVardgivare.getId(), ibVardgivare.getName(), ve.getVardgivareOrgnr()));
            }
            authSystemTree.add(ibVardgivare);
        }
        user.setSystemAuthorities(authSystemTree);
    }

    protected void tryToSelectHsaEntity(IntygsbestallningUser ibUser) {
        if (ibUser.getTotaltAntalVardenheter() == 1) {
            ibUser.changeValdVardenhet(ibUser.getVardgivare().get(0).getVardenheter().get(0).getId());
        }
    }

    /**
     * Overridden for IB. Application has only one role.
     *
     * @param intygUser
     * @param personInfo
     * @param userCredentials
     */
    @Override
    protected void decorateIntygUserWithRoleAndAuthorities(IntygUser intygUser,
                                                           List<PersonInformationType> personInfo,
                                                           UserCredentials userCredentials) {

        Role role = commonAuthoritiesResolver.getRole(AuthoritiesConstants.ROLE_VARDADMIN);
        LOG.debug("User role is set to {}", role);
        intygUser.setRoles(AuthoritiesResolverUtil.toMap(role));
        intygUser.setAuthorities(AuthoritiesResolverUtil.toMap(role.getPrivileges(), Privilege::getName));
    }

    @Override
    protected void decorateIntygUserWithDefaultVardenhet(IntygUser intygUser) {
        // Only set a default enhet if there is only one (mottagningar doesnt count).
        // If no default vardenhet can be determined - let it be null and force user to select one.
        if (getTotaltAntalVardenheterExcludingMottagningar(intygUser) == 1) {
            super.decorateIntygUserWithDefaultVardenhet(intygUser);
        }
    }

    /**
     * Overridden for IB. Application do not utilize system roles.
     *
     * @param intygUser
     * @param userCredentials
     */
    @Override
    protected void decorateIntygUserWithSystemRoles(IntygUser intygUser, UserCredentials userCredentials) {
        // Do nothing;
    }

    @Override
    protected String getDefaultRole() {
        return AuthoritiesConstants.ROLE_VARDADMIN;
    }

    @Override
    protected BaseSakerhetstjanstAssertion getAssertion(Assertion assertion) {
        return super.getAssertion(assertion);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private int getTotaltAntalVardenheterExcludingMottagningar(IntygUser intygUser) {
        // count all vardenheter (not including mottagningar under vardenheter)
        return (int) intygUser.getVardgivare().stream().flatMap(vg -> vg.getVardenheter().stream()).count();
    }

}
