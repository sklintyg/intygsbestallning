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
package se.inera.intyg.intygsbestallning.integration.client;

import org.springframework.stereotype.Component;
import se.riv.intygsbestallning.certificate.order.respondtoorder.v1.rivtabp21.RespondToOrderResponderInterface;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderResponseType;
import se.riv.intygsbestallning.certificate.order.respondtoorderresponder.v1.RespondToOrderType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;
import se.riv.intygsbestallning.certificate.order.v1.ResultType;

@Component
public class RespondToOrderIntygsbestallningStub implements RespondToOrderResponderInterface {

    @Override
    public RespondToOrderResponseType respondToOrder(String s, RespondToOrderType respondToOrderType) {
        var response = new RespondToOrderResponseType();
        var result = new ResultType();
        result.setResultCode(ResultCodeType.OK);
        response.setResult(result);
        return response;
    }
}
