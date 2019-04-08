import React, { Fragment } from 'react'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'

const BorttagenBestallning = ({ handleClose, isOpen, data }) => {
  if (!data) data = {}
  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>{data.header}</ModalHeader>
        <ModalBody>
          <p>{data.body}</p>
        </ModalBody>
        <ModalFooter>
          <Button color={'primary'} onClick={handleClose}>
            Stäng
          </Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

export default compose(modalContainer('errorModal'))(BorttagenBestallning)
