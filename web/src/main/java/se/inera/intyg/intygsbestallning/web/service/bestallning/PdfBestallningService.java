package se.inera.intyg.intygsbestallning.web.service.bestallning;

import se.inera.intyg.intygsbestallning.common.dto.PdfBestallningRequest;

public interface PdfBestallningService {
    byte[] pdf(PdfBestallningRequest request);
}
