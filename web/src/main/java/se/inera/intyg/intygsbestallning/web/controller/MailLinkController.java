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

package se.inera.intyg.intygsbestallning.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.web.auth.IntygsbestallningUser;
import se.inera.intyg.intygsbestallning.web.service.user.UserService;

@RestController
@RequestMapping("/maillink")
public class MailLinkController {
    private static final Logger LOG = LoggerFactory.getLogger(MailLinkController.class);
    private UserService userService;
    private BestallningPersistenceService bestallningPersistenceService;

    public MailLinkController(UserService userService, BestallningPersistenceService bestallningPersistenceService) {
        this.userService = userService;
        this.bestallningPersistenceService = bestallningPersistenceService;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public RedirectView maillink(@PathVariable String id) throws IbServiceException {
        LOG.debug("Maillink-controller received request with bestallning-id {}", id);

        Bestallning bestallning =
                bestallningPersistenceService.getBestallningById(Long.parseLong(id))
                        .orElseThrow(() -> new IbServiceException(IbErrorCodeEnum.NOT_FOUND, "Bestallning with id " + id + " not found"));

        String redirectString;
        IntygsbestallningUser user = userService.getUser();

        String bestallningEnhetsId = bestallning.getVardenhet().getHsaId();
        if (!user.changeValdVardenhet(bestallningEnhetsId)) {
            throw new IbServiceException(
                    IbErrorCodeEnum.LOGIN_FEL002,
                    "User lacks MIU on the care unit on which the Bestallning was issued (" + bestallningEnhetsId + ")");
        } else {
            LOG.debug("All good, redirecting to bestallning {}", id);
            redirectString = "/#/bestallning/" + id;
        }
        return new RedirectView(redirectString);
    }

}
