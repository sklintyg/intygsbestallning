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
package se.inera.intyg.intygsbestallning.web.service.bestallning.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.IB_COLOR_22;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.PAGE_MARGIN_LEFT;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.PAGE_MARGIN_RIGHT;
import static se.inera.intyg.intygsbestallning.web.service.util.PdfUtil.millimetersToPoints;

public class BestallningFooter implements IEventHandler {

    private static final float FONT_SIZE = 10;
    private static final float LINE_WIDTH = 0.5f;
    private static final float HR_Y_BOTTOM_OFFSET = millimetersToPoints(24f);
    private static final float FOOTER_Y_BOTTOM_OFFSET = millimetersToPoints(20f);

    private final PdfFont font;

    public BestallningFooter(PdfFont font) {
        this.font = font;
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
        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize)
                .setFont(font)
                .setFontSize(FONT_SIZE)
                .setFontColor(IB_COLOR_22)
                .setItalic();

        renderHorizontalLine(pageSize, pdfCanvas);
        renderFooterText(pageSize, canvas);
    }

    private void renderFooterText(Rectangle pageSize, Canvas canvas) {
        Paragraph p = new Paragraph("Intygsbeställning är en tjänst från Inera AB som myndigheter kan använda för att skicka förfrågan för medicinska utlåtanden och intyg till vården. Mer information finns på https://www.inera.se/");
        p.setWidth(pageSize.getWidth() - PAGE_MARGIN_LEFT - PAGE_MARGIN_RIGHT);
        canvas.showTextAligned(p,
                PAGE_MARGIN_LEFT,
                FOOTER_Y_BOTTOM_OFFSET, TextAlignment.LEFT, VerticalAlignment.TOP);
    }

    private void renderHorizontalLine(Rectangle pageSize, PdfCanvas pdfCanvas) {
        pdfCanvas.moveTo(PAGE_MARGIN_LEFT, HR_Y_BOTTOM_OFFSET);
        pdfCanvas.lineTo(pageSize.getWidth() - PAGE_MARGIN_LEFT,
                HR_Y_BOTTOM_OFFSET);
        pdfCanvas.setLineWidth(LINE_WIDTH);
        pdfCanvas.setStrokeColor(IB_COLOR_22);
        pdfCanvas.stroke();
    }
}
