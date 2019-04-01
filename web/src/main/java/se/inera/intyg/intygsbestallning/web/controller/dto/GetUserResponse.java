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
package se.inera.intyg.intygsbestallning.web.controller.dto;

import se.inera.intyg.infra.security.common.model.AuthenticationMethod;
import se.inera.intyg.infra.security.common.model.Feature;
import se.inera.intyg.infra.security.common.model.Role;
import se.inera.intyg.intygsbestallning.web.auth.IbSelectableHsaEntity;
import se.inera.intyg.intygsbestallning.web.auth.IbVardgivare;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;

import java.util.List;
import java.util.Map;

import static org.springframework.security.saml.SAMLLogoutProcessingFilter.FILTER_URL;
import static se.inera.intyg.intygsbestallning.web.auth.SecurityConfig.FAKE_LOGOUT_URL;

/**
 * Reponse dto for the getUser api.
 */
public class GetUserResponse {


    private String hsaId;
    private String namn;
    private String authenticationScheme;
    private String logoutUrl;

    private Map<String, Feature> features;

    private List<IbVardgivare> authoritiesTree;
    private Role currentRole;
    private IbSelectableHsaEntity unitContext;
    private int totaltAntalVardenheter;

    public GetUserResponse(IntygsbestallningUser user) {
        this.hsaId = user.getHsaId();
        this.namn = user.getNamn();

        this.authenticationScheme = user.getAuthenticationScheme();
        this.logoutUrl = user.getAuthenticationMethod().equals(AuthenticationMethod.FAKE) ? FAKE_LOGOUT_URL : FILTER_URL;
        this.features = user.getFeatures();

        this.authoritiesTree = user.getSystemAuthorities();
        this.currentRole = user.getCurrentRole();
        this.unitContext = user.getUnitContext();
        this.totaltAntalVardenheter = user.getTotaltAntalVardenheter();

    }

    public String getHsaId() {
        return hsaId;
    }

    public String getNamn() {
        return namn;
    }

    public String getAuthenticationScheme() {
        return authenticationScheme;
    }

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public List<IbVardgivare> getAuthoritiesTree() {
        return authoritiesTree;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public IbSelectableHsaEntity getUnitContext() {
        return unitContext;
    }

    public int getTotaltAntalVardenheter() {
        return totaltAntalVardenheter;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
