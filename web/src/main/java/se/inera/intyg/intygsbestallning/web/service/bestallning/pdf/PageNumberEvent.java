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

import static se.inera.intyg.intygsbestallning.web.service.bestallning.PdfBestallningServiceImpl.TEXT_SIZE;
import static se.inera.intyg.intygsbestallning.web.service.util.PdfUtil.millimetersToPoints;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

/**
 * Renders the page number. Note that quirky placeholder stuff going on since we don't know the total number of pages
 * until the full PDF have been rendered.
 */
public class PageNumberEvent implements IEventHandler {

    private static final float PAGE_NUMBER_Y_OFFSET = millimetersToPoints(25f);
    private static final float SIDE = 20f;
    private static final float DESCENT = 3;

    private PdfFont font;
    private PdfFormXObject placeholder;

    public PageNumberEvent(PdfFont font) {
        this.font = font;
        placeholder = new PdfFormXObject(new Rectangle(0, 0, SIDE, SIDE));
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event instanceof PdfDocumentEvent)) {
            return;
        }

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        int pageNumber = pdf.getPageNumber(page);
        Rectangle pageSize = page.getPageSize();
        PdfCanvas pdfCanvas = new PdfCanvas(
                page.newContentStreamBefore(), page.getResources(), pdf);
        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        canvas.setFont(font).setFontSize(TEXT_SIZE);

        String text = "Sida " + pageNumber + " av ";
        float textWidth = font.getWidth(text, TEXT_SIZE);
        canvas.showTextAligned(text, pageSize.getWidth() / 2, pageSize.getTop() - PAGE_NUMBER_Y_OFFSET, TextAlignment.CENTER);
        pdfCanvas.addXObject(placeholder, pageSize.getWidth() / 2 + textWidth / 2 + 2, pageSize.getTop() - PAGE_NUMBER_Y_OFFSET - DESCENT);
        pdfCanvas.release();
    }

    public void writeTotal(PdfDocument pdf) {
        Canvas canvas = new Canvas(placeholder, pdf);
        canvas.setFont(font).setFontSize(TEXT_SIZE);
        canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()), 0, DESCENT, TextAlignment.LEFT);
    }
}
