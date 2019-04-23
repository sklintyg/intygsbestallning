import React, { Fragment } from 'react'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'

const CookieModal = ({ handleClose, isOpen, accept }) => {
  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Om kakor (cookies)</ModalHeader>
        <ModalBody>
          <p>
            Vi använder kakor (cookies) för att den här webbplatsen ska fungera på ett bra sätt för dig. Genom att logga in accepterar du
            vår användning av kakor.
          </p>
          <h5>Så här använder vi kakor</h5>
          <p>
            Den typ av kakor som används på den här webbplatsen kallas för sessionskakor. De lagras temporärt i din dators minne under tiden
            du är inne på webbplatsen. Sessionskakor sparar ingen personlig information om dig, och de försvinner när du stänger din
            webbläsare.
          </p>
          <p>
            I Intygsbeställning används sessionskakor för att du ska kunna navigera i tjänsten utan att behöva logga in på nytt varje gång
            du går till en ny sida. De används också för att de filterinställningar du gör ska finnas kvar under hela tiden du är inloggad.
            För att vara säker på att kakorna inte sparas i din dator efter avslutad session måste du stänga webbläsaren när du har loggat
            ut.
          </p>
          <h5>Undvika kakor</h5>
          <p>
            Vill du inte acceptera kakor kan din webbläsare ställas in så att du automatiskt nekar till lagring av kakor eller informeras
            varje gång en webbplats begär att få lagra en kaka. Genom webbläsaren kan också tidigare lagrade kakor raderas. Se webbläsarens
            hjälpsidor för mer information.
          </p>
          <p>Väljer du att inte acceptera kakor så kan du inte identifiera dig med e-legitimation i denna e-tjänst.</p>
          <p>Mer information om kakor kan du finna på <a className="extern" href="https://pts.se/sv/privat/internet/integritet/kakor-cookies/">Kommunikationsmyndigheten PTS sida om kakor</a></p>
        </ModalBody>
        <ModalFooter>
          <Button color={'primary'} onClick={() => accept()}>
            Jag godkänner
          </Button>
          <Button
            color={'default'}
            onClick={() => {
              handleClose()
            }}>
            Avbryt
          </Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

export const CookieModalId = 'cookieModal'
export default compose(modalContainer(CookieModalId))(CookieModal)
