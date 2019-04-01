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

/**
 * Custom ErrorController that overrides Spring boots default "whitepage" error handling.
 */
@RestController
public class RequestErrorController implements ErrorController {

    public static final String IB_CLIENT_ROOTPATH = "/#/";
    public static final String IB_CLIENT_EXIT_BOOT_PATH = IB_CLIENT_ROOTPATH + "exit/";

    private static final String ERROR_PATH = "/error";
    private ErrorAttributes errorAttributes = new DefaultErrorAttributes(true);

    /**
     * For normal "browser navigation" initiated requests, we handle all error with a redirect to a specific
     * startup view to present an error.
     *
     * @param request
     * @return
     */
    @RequestMapping(path = ERROR_PATH, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView handleErrorAsRedirect(HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = resolveByHttpStatus(getDispatcherErrorStatusCode(request));

        return new ModelAndView("redirect:" + IB_CLIENT_EXIT_BOOT_PATH + apiErrorResponse.getErrorCode());
    }

    /**
     * For xhr-requests, we handle all error with a specific error code value that client can react to.
     *
     * @param request
     * @return
     */
    @RequestMapping(path = ERROR_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity handleErrorAsJson(WebRequest webRequest, HttpServletRequest request) {
        final HttpStatus statusCode = getDispatcherErrorStatusCode(request);
        ApiErrorResponse apiErrorResponse = buildApiErrorResponseErrorCode(this.errorAttributes.getError(webRequest), statusCode);

        return new ResponseEntity<>(apiErrorResponse, statusCode);
    }


    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    private HttpStatus getDispatcherErrorStatusCode(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return (status != null) ? HttpStatus.valueOf(Integer.valueOf(status.toString())) : HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ApiErrorResponse buildApiErrorResponseErrorCode(Throwable error, HttpStatus statusCode) {
        ApiErrorResponse result;
        if (error != null) {
            result = resolveByException(error);
        } else {
            result = resolveByHttpStatus(statusCode);
        }
        return result;
    }

    private ApiErrorResponse resolveByException(Throwable error) {
        if (error instanceof IbServiceException) {
            final IbServiceException ibException = (IbServiceException) error;
            return new ApiErrorResponse(error.getMessage(), ibException.getErrorCode().name());
        } else if (error instanceof AuthoritiesException) {
            return new ApiErrorResponse(error.getMessage(), IbErrorCodeEnum.UNAUTHORIZED.name());
        } else {
            return new ApiErrorResponse(error.getMessage(), IbErrorCodeEnum.UNKNOWN_INTERNAL_PROBLEM.name());
        }
    }

    private ApiErrorResponse resolveByHttpStatus(final HttpStatus statusCode) {
        IbErrorCodeEnum errorCode = IbErrorCodeEnum.UNKNOWN_INTERNAL_PROBLEM;

        if (statusCode == HttpStatus.FORBIDDEN) {
            errorCode = IbErrorCodeEnum.UNAUTHORIZED;
        } else if (statusCode == HttpStatus.NOT_FOUND) {
            errorCode = IbErrorCodeEnum.NOT_FOUND;
        }

        return new ApiErrorResponse("no message", errorCode.name());
    }

}
