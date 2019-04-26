import React from 'react'
import { School } from './IbSvgIcons'
import ExternalLink from '../externalLink/ExternalLink'
import colors from './IbColors'
import styled from 'styled-components'
import { IbTypo07 } from './IbTypography'

/**
 * This component implements the link to Inera "Intygsskola"
 * see https://inera-certificate.atlassian.net/wiki/spaces/IT/pages/1313570866/Z+-+IB+Intygsskolan
 */

const Wrapper = styled(IbTypo07)`
  & a {
    padding: 0 4px;
    color: ${colors.IB_COLOR_31};
  }
`

const IntygsskolaLink = () => {
  return (
    <Wrapper>
      <School color={colors.IB_COLOR_31} />
      <ExternalLink href="https://www.inera.se/aktuellt/utbildningar/intygsskolan/intygsbestallning/">
        Hitta svar på dina frågor i Ineras Intygsskola
      </ExternalLink>
    </Wrapper>
  )
}

export default IntygsskolaLink
