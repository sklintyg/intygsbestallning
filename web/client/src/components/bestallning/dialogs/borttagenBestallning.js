import React, { Fragment } from 'react'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'

const BorttagenBestallning = ({ handleOpen, handleClose, isOpen, onClose }) => {
  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Beställning raderad</ModalHeader>
        <ModalBody>
          <p>Beställningen är nu raderad och finns inte längre tillgänglig i tjänsten.</p>
        </ModalBody>
        <ModalFooter>
          <Button
            color={'primary'}
            onClick={() => {
              handleClose()
              onClose()
            }}>
            Stäng
          </Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

export const BorttagenBestallningId = 'borttagenBestallning'
export default compose(modalContainer(BorttagenBestallningId))(BorttagenBestallning)
