import React, { Fragment } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter, DropdownItem } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import IbAlert from '../../alert/Alert'

const SkrivUtBestallning = ({handleOpen, handleClose, isOpen, accept, sekretess}) => (
  <Fragment>
    {sekretess ? <DropdownItem onClick={handleOpen}>Förfrågan</DropdownItem> :
    <DropdownItem onClick={accept}>Förfrågan</DropdownItem>}
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Skriv ut beställning</ModalHeader>
      <ModalBody>
        <IbAlert type="sekretess">Invånaren har sekretessmarkering. Hantera utskriften varsamt.</IbAlert>
      </ModalBody>
      <ModalFooter>
        <Button color={'secondary'} outline={true} onClick={() => {handleClose()}}>Avbryt</Button>
        <Button color={'primary'} onClick={() => {accept(); handleClose()}}>Skriv ut</Button>
      </ModalFooter>
    </Modal>
  </Fragment>
)


  
export default compose(
  modalContainer('skrivUtBestallning')
)(SkrivUtBestallning)