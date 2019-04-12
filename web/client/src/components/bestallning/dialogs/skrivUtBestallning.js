import React, { Fragment } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import IbAlert, { alertType } from '../../alert/Alert'
import SpinnerButton from '../../spinnerButton'

const SkrivUtBestallning = ({ handleClose, isOpen, accept }) => (
  <Fragment>
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Skriv ut beställning</ModalHeader>
      <ModalBody>
        <IbAlert type={alertType.SEKRETESS}>Invånaren har sekretessmarkering. Hantera utskriften varsamt.</IbAlert>
      </ModalBody>
      <ModalFooter>
        <SpinnerButton color={'primary'} accept={() => accept().then(handleClose)}>
          Skriv ut
        </SpinnerButton>
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
