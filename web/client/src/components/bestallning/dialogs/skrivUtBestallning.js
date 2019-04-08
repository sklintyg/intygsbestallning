import React, { Fragment } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter, DropdownItem } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import IbAlert, { alertType } from '../../alert/Alert'

const SkrivUtBestallning = ({ handleOpen, handleClose, isOpen, accept, sekretess }) => (
  <Fragment>
    <DropdownItem onClick={sekretess ? handleOpen : accept}>Förfrågan/Beställning</DropdownItem>
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Skriv ut beställning</ModalHeader>
      <ModalBody>
        <IbAlert type={alertType.SEKRETESS}>Invånaren har sekretessmarkering. Hantera utskriften varsamt.</IbAlert>
      </ModalBody>
      <ModalFooter>
        <Button
          color={'primary'}
          onClick={() => {
            accept()
            handleClose()
          }}>
          Skriv ut
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

export const SkrivUtBestallningId = 'skrivUtBestallning'
export default compose(modalContainer(SkrivUtBestallningId))(SkrivUtBestallning)
