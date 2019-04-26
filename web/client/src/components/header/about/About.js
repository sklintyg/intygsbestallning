import React, { Fragment } from 'react'
import PropTypes from 'prop-types'
import { ActionButton } from '../styles'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import { School, AboutIcon } from '../../styles/IbSvgIcons'
import styled from 'styled-components'
import colors from '../../styles/IbColors'
import ExternalLink from '../../externalLink/ExternalLink'

const Intygsskola = styled.div`
  background-color: ${colors.IB_COLOR_20};
  padding: 20px;
  margin-bottom: 10px;

  & a {
    text-decoration: underline;
    color: ${colors.IB_COLOR_31} !important;
  }

  & a:hover {
    color: ${colors.IB_COLOR_21};
  }
`

const StyledSchool = styled.span`
  padding-right: 10px;
`

const About = ({ handleOpen, handleClose, isOpen, settings }) => {
  return (
    <Fragment>
      <ActionButton onClick={handleOpen} id="changeUnitBtn">
        <AboutIcon />
        <br />
        Om tjänsten
      </ActionButton>
      <Modal isOpen={isOpen} size={'lg'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Om Intygsbeställning</ModalHeader>
        <ModalBody>
          <Intygsskola>
            <StyledSchool><School color={colors.IB_COLOR_31}/></StyledSchool>
            <ExternalLink href="https://www.inera.se/aktuellt/utbildningar/intygsskolan/intygsbestallning/">
              Hitta svar på dina frågor i Ineras Intygsskola
            </ExternalLink>
          </Intygsskola>
          <p>Intygsbeställning är en tjänst som drivs av Inera AB.</p>
          <p>Nuvarande version är {settings.versionInfo.buildVersion}</p>
          <p>
            Intygsbeställning är utvecklat för Inernet Explorer 11 och efterföljande versioner. Andra webbläsare kan användas, men då finns
            det risk att problem uppstår.
          </p>
          <p>
            För att kunna generera utskrifter använder sig Intygbeställning av <ExternalLink href="https://itextpdf.com">iText7</ExternalLink>. För mer
            information under vilka villkor iText7 får användas läs mer om AGPL 3.0 på{' '}
            <ExternalLink href="https://www.gnu.org/licenses/agpl-3.0.en.html">gnu.org</ExternalLink>. För Intygsbeställnings källkod besök{' '}
            <ExternalLink href="https://github.com/sklintyg/intygsbestallning">Github</ExternalLink>. För Intygsbeställnings upphovsrättslicens besök{' '}
            <ExternalLink href="https://github.com/sklintyg/intygsbestallning">Github</ExternalLink>.
          </p>

          <h5>Behandling av personuppgifter</h5>
          <p>
            Inera behandlar personuppgifter som personuppgiftsansvarig för det egna bolagets verksamhet och i sin roll som
            personuppgiftsbiträde för landsting, regioner och kommuner. Inera värnar om den personliga integriteten och eftersträvar alltid
            en hög nivå av dataskydd.
          </p>
          <p>
            Läs mer om hur personuppgifter behandlas på{' '}
            <ExternalLink href="https://www.inera.se/om-inera/behandling-av-personuppgifter/">Ineras webbplats</ExternalLink>.
          </p>

          <h5>Användandet av kakor (cookies)</h5>
          <p>
            Den typ av kakor som används på den här webbplatsen kallas för sessionskakor. De lagras temporärt i din dators minne under tiden
            du är inne på webbplatsen. Sessionskakor sparar ingen personlig information om dig och de försvinner när du stänger din
            webbläsare.
          </p>
          <p>
            I Intygsbeställning används sessionskakor för att du ska kunna navigera i tjänsten utan att behöva logga in på nytt varje gång
            du går till en ny sida. För att vara säker på att kakorna inte sparas i din dator efter avslutad session måste du stänga
            webbläsaren när du har loggat ut.
          </p>
          <p>
            Om du inte accepterar användningen av kakor kan du ställa in webbläsaren så att kakor blockeras. Observera att det då inte går
            att logga in.
          </p>
          <p>
            Om du väljer att acceptera användningen av kakor kommer ditt samtyckte att lagras permanent i din webbläsare. För att ta
            tillbaka ditt samtycke behöver du rensa kakor och annan webbplatsdata för den här webbplatsen i din webbläsare.
          </p>
          <p>
            Mer information om kakor (cookies) finns på{' '}
            <ExternalLink href="https://pts.se/sv/privat/internet/integritet/kakor-cookies/">Post- och telestyrelsens webbplats</ExternalLink>.
          </p>

          <h5>Kontakt och support</h5>
          <p>Vid frågor gällande inloggning, kontakta leverantören av din e-legitimation eller SITHS-kortsadministratör.</p>
          <p>
            Tekniska frågor om Intygsbeställning hanteras i första hand av din lokala IT-avdelningen. Om din lokala IT-avdelning inte kan
            hitta felet ska de kontakta <ExternalLink href="https://www.inera.se/kundservice">Ineras kundservice</ExternalLink>.
          </p>
          <p>
            Har du som användare synpunkter på tjänsten kan du kontakta <ExternalLink href="https://www.inera.se/kundservice">Ineras kundservice</ExternalLink>.
          </p>
          <p>
            Inera AB
            <br />
            Box 17703
            <br />
            118 93 Stockholm
          </p>
          <p>Organisationsnummer: 556559-4230</p>
        </ModalBody>
        <ModalFooter>
          <Button color={'secondary'} outline={true} onClick={handleClose}>
            Stäng
          </Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

About.propTypes = {
  handleOpen: PropTypes.func.isRequired,
  handleClose: PropTypes.func.isRequired,
  isOpen: PropTypes.bool.isRequired,
}

export default About
