package se.inera.intyg.intygsbestallning.web.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
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

        Optional<Bestallning> bestallningOptional =
                bestallningPersistenceService.getBestallningByIdAndHsaIdAndOrgId(Long.parseLong(id), null, null);

        if (!bestallningOptional.isPresent()) {
            LOG.error("Bestallning with id {} not found", id);
            return new RedirectView("/#/exit/" + IbErrorCodeEnum.NOT_FOUND.name());
            // Maybe throw exception and let RequestErrorController handle this?
            //throw new IbServiceException(IbErrorCodeEnum.NOT_FOUND, "Beställningen kunde inte hittas");
        }

        String redirectString;
        IntygsbestallningUser user = userService.getUser();

        Vardenhet vardenhetFromBestallning = bestallningOptional.get().getVardenhet();
        if (!user.changeValdVardenhet(vardenhetFromBestallning.getHsaId())) {
            LOG.error("User lacks MIU on the care unit on which the Bestallning was issued");
            redirectString = "/#/exit/" + IbErrorCodeEnum.LOGIN_FEL002.name();
            // Maybe throw exception and let RequestErrorController handle this?
            //throw new IbServiceException(IbErrorCodeEnum.LOGIN_FEL002, "Behörighet saknas");
        } else {
            LOG.debug("All good, redirecting to bestallning {}", id);
            redirectString = "/#/bestallning/" + id;
        }
        return new RedirectView(redirectString);
    }

}
