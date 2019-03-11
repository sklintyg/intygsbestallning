package se.inera.intyg.intygsbestallning.common.util;

import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;
import se.riv.intygsbestallning.certificate.order.v1.ResultType;
import java.util.Objects;
import se.inera.intyg.intygsbestallning.common.exception.IbNotFoundException;
import se.inera.intyg.intygsbestallning.common.exception.NotFoundType;

public final class RivtaUtil {

    private RivtaUtil() {
    }

    public static IIType anII(String root, String extension) {
        IIType ii = new IIType();
        ii.setExtension(extension);
        ii.setRoot(root);
        return ii;
    }

    public static CVType aCv(final String code) {
        return aCv(code, null, null);
    }

    public static CVType aCv(String code, String codeSystem, String displayName) {
        CVType cv = new CVType();
        cv.setCode(code);
        cv.setCodeSystem(codeSystem);
        cv.setDisplayName(displayName);
        return cv;
    }

    public static ResultType aResultTypeOK() {

        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        return result;
    }

    public static ResultType aResultTypeError(final Exception exception) {

        String resultText;

        if (exception instanceof IbNotFoundException) {
            final NotFoundType notFoundType = ((IbNotFoundException) exception).getType();
            final Long entityId = ((IbNotFoundException) exception).getErrorEntityId();
            resultText = notFoundType != null
                    ? String.format(notFoundType.getErrorText(), Objects.toString(entityId))
                    : exception.getMessage();
        } else {
            resultText = exception.getMessage();
        }

        ResultType result = new ResultType();
        result.setResultCode(ResultCodeType.ERROR);
        result.setResultText(resultText);
        return result;
    }
}
