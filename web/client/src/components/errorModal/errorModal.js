import React, { Fragment } from 'react'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import ErrorMessageFormatter from '../../messages/ErrorMessageFormatter'

const BorttagenBestallning = ({ handleClose, isOpen, data }) => {
  if (!data) return null

  const error = { ...data, title: '' }

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>{data.title}</ModalHeader>
        <ModalBody>
          {data.body && data.body}
          {!data.body && <ErrorMessageFormatter error={error} />}
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
