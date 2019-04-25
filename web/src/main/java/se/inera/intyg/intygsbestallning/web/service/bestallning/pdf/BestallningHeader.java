package se.inera.intyg.intygsbestallning.web.service.bestallning.pdf;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.IB_COLOR_07;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.PAGE_MARGIN_LEFT;
import static se.inera.intyg.intygsbestallning.web.service.util.PdfUtil.millimetersToPoints;

public class BestallningHeader implements IEventHandler {

    // All constants related to positioning should be in points.
    private static final float FONT_SIZE = 10;
    private static final float LOGOTYPE_Y_TOP_OFFSET = millimetersToPoints(15f);
    private static final float LOGOTYPE_MAX_HEIGHT = millimetersToPoints(15f);
    private static final float LOGOTYPE_MAX_WIDTH = millimetersToPoints(45f);
    private static final float UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET = millimetersToPoints(25f);
    private static final float UTSKRIFTSDATUM_HEADER_X_OFFSET = millimetersToPoints(7f);
    private static final float LINE_WIDTH = 0.5f;
    private static final float HR_Y_TOP_OFFSET = millimetersToPoints(28f);
    private static final float DEFAULT_PADDING = millimetersToPoints(5f);
    private static final int WIDTH_SCALE_THRESHOLD = 3;

    private final PdfFont font;
    private ImageData logotypeData;
    private Long bestallningId;

    public BestallningHeader(PdfFont font, ImageData logotypeData, Long bestallningId) {
        this.font = font;
        this.logotypeData = logotypeData;
        this.bestallningId = bestallningId;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();

        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(), page.getResources(), pdf);
        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);

        canvas.setFont(font).setFontSize(FONT_SIZE);
        canvas.setFontColor(IB_COLOR_07);

        renderLogotype(pageSize, pdfCanvas);
        renderUtskriftsDatum(pageSize, canvas);
        renderId(pageSize, canvas);
        renderHorizontalLine(pageSize, pdfCanvas);
    }

    private void renderLogotype(Rectangle pageSize, PdfCanvas pdfCanvas) {
        // We need to constrain the logotype either by width or by height. Typically width.
        if (logotypeData.getWidth() > LOGOTYPE_MAX_WIDTH) {
            float ratio = LOGOTYPE_MAX_WIDTH / logotypeData.getWidth();
            float widthHeightRatio = logotypeData.getWidth() / logotypeData.getHeight();
            // Decide on constraint depending on how quadratic the logo image is determined to be
            if (widthHeightRatio < WIDTH_SCALE_THRESHOLD) {
                // Height constraint, add bottom padding
                pdfCanvas.addImage(logotypeData, PAGE_MARGIN_LEFT,
                        pageSize.getTop() - LOGOTYPE_Y_TOP_OFFSET - LOGOTYPE_MAX_HEIGHT + DEFAULT_PADDING,
                        LOGOTYPE_MAX_HEIGHT, false, false);
            } else {
                // Width constraint
                pdfCanvas.addImage(logotypeData, PAGE_MARGIN_LEFT,
                        pageSize.getTop() - LOGOTYPE_Y_TOP_OFFSET - (logotypeData.getHeight() * ratio), LOGOTYPE_MAX_WIDTH, false);
            }
        }
    }

    private void renderUtskriftsDatum(Rectangle pageSize, Canvas canvas) {
        canvas.showTextAligned("Utskrift gjord " + LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                PAGE_MARGIN_LEFT + UTSKRIFTSDATUM_HEADER_X_OFFSET,
                pageSize.getTop() - UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET, TextAlignment.LEFT);
    }

    private void renderId(Rectangle pageSize, Canvas canvas) {
        canvas.showTextAligned("ID " + bestallningId,
                pageSize.getWidth() - PAGE_MARGIN_LEFT,
                pageSize.getTop() - UTSKRIFTSDATUM_HEADER_Y_TOP_OFFSET, TextAlignment.RIGHT);
    }

    private void renderHorizontalLine(Rectangle pageSize, PdfCanvas pdfCanvas) {
        pdfCanvas.moveTo(PAGE_MARGIN_LEFT, pageSize.getTop() - HR_Y_TOP_OFFSET);
        pdfCanvas.lineTo(pageSize.getWidth() - PAGE_MARGIN_LEFT,
                pageSize.getTop() - HR_Y_TOP_OFFSET);
        pdfCanvas.setLineWidth(LINE_WIDTH);
        pdfCanvas.setStrokeColor(IB_COLOR_07);
        pdfCanvas.stroke();
    }
}
