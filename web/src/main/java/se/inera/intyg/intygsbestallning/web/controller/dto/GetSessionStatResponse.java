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
package se.inera.intyg.intygsbestallning.web.controller.dto;

import se.inera.intyg.intygsbestallning.common.dto.StatResponse;

/**
 * Created by marced on 09/03/16.
 */
public class GetSessionStatResponse {

    private SessionState sessionState;
    private StatResponse statResponse;

    public GetSessionStatResponse(SessionState sessionState, StatResponse statResponse) {
        this.sessionState = sessionState;
        this.statResponse = statResponse;
    }

    public StatResponse getStatResponse() {
        return statResponse;
    }

    public void setStatResponse(StatResponse statResponse) {
        this.statResponse = statResponse;
    }

    public SessionState getSessionState() {
        return sessionState;
    }

    public void setSessionState(SessionState sessionState) {
        this.sessionState = sessionState;
    }
}
