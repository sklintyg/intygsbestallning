package se.inera.intyg.intygsbestallning.web.service.util;

public class PdfUtil {
    private static final float PPI = 72f;
    private static final float MM_PER_TUM = 25.4f;

    private PdfUtil() {

    }

    /**
     * Konverterar från millimeter till iText7 points.
     */
    public static float millimetersToPoints(final float value) {
        return inchesToPoints(millimetersToInches(value));
    }

    /**
     * Konverterar från millimeter till tum.
     */
    private static float millimetersToInches(final float value) {
        return value / MM_PER_TUM;
    }

    /**
     * Konverterar från tum till iText 7 points.
     */
    private static float inchesToPoints(final float value) {
        return value * PPI;
    }
}
