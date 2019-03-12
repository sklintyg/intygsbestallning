package se.inera.intyg.intygsbestallning.web.service.bestallning;


import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;

public interface CreateBestallningService {

    Long create(CreateBestallningRequest request);
}
