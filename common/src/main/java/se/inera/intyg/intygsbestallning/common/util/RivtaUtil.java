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

package se.inera.intyg.intygsbestallning.common.util;

import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.ErrorIdType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;
import se.riv.intygsbestallning.certificate.order.v1.ResultType;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;

public final class RivtaUtil {

    private RivtaUtil() {
    }

    public static IIType createIIType(String root, String extension) {
        IIType ii = new IIType();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;
    }

    public static CVType createCVType(String code, String codeSystem, String codeSystemName, String displayName) {
        CVType cv = new CVType();
        cv.setCode(code);
        cv.setCodeSystem(codeSystem);
        cv.setCodeSystemName(codeSystemName);
        cv.setDisplayName(displayName);
        return cv;
    }

    public static ResultType createResultTypeOk() {
        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        return result;
    }

    public static ResultType createResultTypeError(final Throwable throwable) {

        String resultText;
        ErrorIdType errorIdType;

        if (throwable instanceof IbResponderValidationException) {
            resultText = throwable.getMessage();
            errorIdType = ((IbResponderValidationException) throwable).getErrorCode().getErrorIdType();
        } else {
            resultText = throwable.getMessage();
            errorIdType = ErrorIdType.APPLICATION_ERROR;
        }

        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.ERROR);
        result.setResultText(resultText);
        result.getErrorId().add(errorIdType);
        return result;
    }
}
