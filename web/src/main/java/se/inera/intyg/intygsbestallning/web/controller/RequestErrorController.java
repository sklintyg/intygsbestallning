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
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import se.inera.intyg.infra.security.authorities.AuthoritiesException;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.web.controller.dto.ApiErrorResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * Custom ErrorController that overrides Spring boots default "whitepage" error handling.
 */
@RestController
public class RequestErrorController implements ErrorController {

    private static final String IB_CLIENT_ROOTPATH = "/#/";
    public static final String IB_CLIENT_EXIT_BOOT_PATH = IB_CLIENT_ROOTPATH + "exit/";

    private static final String ERROR_PATH = "/error";
    private static final Logger LOG = LoggerFactory.getLogger(RequestErrorController.class);
    private ErrorAttributes errorAttributes = new DefaultErrorAttributes(true);

    /**
     * For normal "browser navigation" initiated requests, we handle all error with a redirect to a specific
     * startup view to present an error.
     *
     * @param request
     * @return
     */
    @RequestMapping(path = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView handleErrorAsRedirect(WebRequest webRequest, HttpServletRequest request) {
        final String errorContext = getErrorContext(webRequest);

        ApiErrorResponse apiErrorResponse = fromHttpStatus(getDispatcherErrorStatusCode(request));
        String redirectView = "redirect:" + IB_CLIENT_EXIT_BOOT_PATH + apiErrorResponse.getErrorCode();

        LOG.error(String.format("(Page) Request error intercepted: %s - responding with: %s", errorContext, redirectView));
        return new ModelAndView(redirectView);
    }

    /**
     * For xhr-requests, we handle all errors by responding with a specific error code json struct that client can act on.
     *
     * @param request
     * @return
     */
    @RequestMapping(path = ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity handleErrorAsJsonResponse(WebRequest webRequest, HttpServletRequest request) {
        final HttpStatus httpStatus = getDispatcherErrorStatusCode(request);
        final String errorContext = getErrorContext(webRequest);
        final Throwable error = this.errorAttributes.getError(webRequest);
        ApiErrorResponse apiErrorResponse;
        if (error != null) {
            apiErrorResponse = fromException(error);
        } else {
            apiErrorResponse = fromHttpStatus(httpStatus);
        }
        LOG.error(String.format("(REST) Request error intercepted: %s - responding with: %s", errorContext, apiErrorResponse.toString()), error);
        return new ResponseEntity<>(apiErrorResponse, httpStatus);
    }


    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private String getErrorContext(WebRequest webRequest) {
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, false);
        return Arrays.toString(attributes.entrySet().toArray());
    }


    private HttpStatus getDispatcherErrorStatusCode(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return (status != null) ? HttpStatus.valueOf(Integer.valueOf(status.toString())) : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ApiErrorResponse fromException(Throwable error) {

        if (error instanceof IbServiceException) {
            final IbServiceException ibException = (IbServiceException) error;
            return new ApiErrorResponse(error.getMessage(), ibException.getErrorCode().name(), ibException.getLogId());
        } else if (error instanceof AuthoritiesException) {
            return new ApiErrorResponse(error.getMessage(), IbErrorCodeEnum.UNAUTHORIZED.name(), UUID.randomUUID().toString());
        } else {
            return new ApiErrorResponse(error.getMessage(), IbErrorCodeEnum.UNKNOWN_INTERNAL_PROBLEM.name(), UUID.randomUUID().toString());
        }
    }

    private ApiErrorResponse fromHttpStatus(final HttpStatus statusCode) {
        IbErrorCodeEnum errorCode = IbErrorCodeEnum.UNKNOWN_INTERNAL_PROBLEM;

        if (statusCode == HttpStatus.FORBIDDEN) {
            errorCode = IbErrorCodeEnum.UNAUTHORIZED;
        } else if (statusCode == HttpStatus.NOT_FOUND) {
            errorCode = IbErrorCodeEnum.NOT_FOUND;
        }
        return new ApiErrorResponse("<no message>", errorCode.name(), UUID.randomUUID().toString());
    }

}
