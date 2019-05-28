/**
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
package se.inera.intyg.intygsbestallning.common.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.core.io.Resource

@ConfigurationProperties(prefix = "ntjp.ws")
open class NtjpWsProperties {
  val certificate = NtjpCertificate()
  val truststore = NtjpTruststore()

  class NtjpCertificate {
    lateinit var type: String
    lateinit var file: Resource
    lateinit var password: String
  }

  class NtjpTruststore {
    lateinit var type: String
    lateinit var file: Resource
    lateinit var password: String
  }
}