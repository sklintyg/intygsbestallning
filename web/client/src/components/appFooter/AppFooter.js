import React from 'react'
import styled from 'styled-components'
import ibValues from '../styles/IbValues'
import ibColors from '../styles/IbColors'
import { IbTypo14, IbTypo07 } from '../styles/IbTypography'
import { Button } from 'reactstrap'

const FooterOuterContainer = styled.div`
  background-color: ${ibColors.IB_COLOR_28};
  box-shadow: inset 1px 4px 9px -6px #000;
  width: 100%;
  padding: 39px 10px;
`
const FooterContainer = styled.div`
  display: flex;
  flex-direction: row;

  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
`
const FooterSection = styled.section`
  padding: 10px 50px 10px 10px;
`

const Text = styled(IbTypo07)`
  margin-bottom: 0;
`

const InternLank = styled(Button)`
  color: ${ibColors.IB_COLOR_17} !important;
  text-decoration: underline !important;
  &:hover {
    color: ${ibColors.IB_COLOR_21} !important;
  }
`

const AppFooter = () => {
  const openCookieDialog = () => {
    // öppna cookie dialog
  }

  return (
    <FooterOuterContainer>
      <FooterContainer>
        <FooterSection>
          <IbTypo14 as="h3">Intygsbeställning är en tjänst som drivs av Inera AB</IbTypo14>
          <Text as="p">Box 11703, 118 93 Stockholm</Text>
          <Text as="p">
            <a rel="noopener noreferrer" target="_blank" href="https://www.inera.se" className="extern">
              www.inera.se
            </a>
          </Text>
        </FooterSection>
        <FooterSection>
          <IbTypo14>Kundtjänst</IbTypo14>
          <Text as="p">
            <a rel="noopener noreferrer" target="_blank" href="https://www.inera.se/kundservice/kontakta-oss/" className="extern">
              Kontakt och support
            </a>
          </Text>
        </FooterSection>
        <FooterSection>
          <IbTypo14>Läs mer om inloggningen</IbTypo14>
          <Text as="p">
            <a rel="noopener noreferrer" target="_blank" href="https://www.inera.se/intygsbestallning/inloggning" className="extern">
              SITHS-kort
            </a>
          </Text>
        </FooterSection>
      </FooterContainer>
      <FooterContainer>
        <FooterSection>
          <Text as="p">
            Intygsbeställning använder kakor (cookies).{' '}
            <InternLank color="link" onClick={openCookieDialog}>
              Läs mer om kakor
            </InternLank>
          </Text>
        </FooterSection>
      </FooterContainer>
    </FooterOuterContainer>
  )
}

export default AppFooter
