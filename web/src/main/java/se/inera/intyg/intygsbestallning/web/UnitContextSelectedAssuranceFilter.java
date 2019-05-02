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

package se.inera.intyg.intygsbestallning.web;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

/**
 * Filter that terminates the filter chain and responds with a server error if a request is made to a url not included
 * in the configured ignoredUrlsList and the user don't have a unitcontext selected (yet).
 */
public class UnitContextSelectedAssuranceFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(UnitContextSelectedAssuranceFilter.class);

    private UserService userService;

    private List<String> ignoredUrlsList;

    public UnitContextSelectedAssuranceFilter(UserService userService, List<String> ignoredUrlsList) {
        this.userService = userService;
        this.ignoredUrlsList = ignoredUrlsList;
        if (ignoredUrlsList == null || ignoredUrlsList.isEmpty()) {
            LOG.warn("No ignored Urls are configured!!!");
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        LOG.error("User accessed api endpoint " + request.getRequestURI()
                + " - but has not selected a unit context yet - aborting filter chain!");
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        IntygsbestallningUser user = this.userService.getUser();
        boolean shouldNotFilter = user == null || user.getUnitContext() != null || isIgnoredUrl(request);
        LOG.debug("shouldNotFilter " + request.getRequestURI() + " = " + shouldNotFilter);
        return shouldNotFilter;
    }

    private boolean isIgnoredUrl(HttpServletRequest request) {
        String url = request.getRequestURI();
        boolean shouldIgnore = ignoredUrlsList.stream().anyMatch(s -> url.contains(s));
        LOG.debug("shouldIgnore url " + url + " = " + shouldIgnore);
        return shouldIgnore;
    }
}
