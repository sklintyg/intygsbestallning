import React, { Fragment, useState } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import styled from 'styled-components'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import SpinnerButton from '../../spinnerButton'

const Textarea = styled.textarea`
  width: 100%;
  resize: none;
`

const AccepteraBestallning = ({ handleClose, isOpen, accept }) => {
  const [fritextForklaring, setFritextForklaring] = useState('')

  const handleChange = (e) => {
    setFritextForklaring(e.target.value)
  }

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Acceptera</ModalHeader>
        <ModalBody>
          <p>
            Ett meddelande om att beställningen accepteras kommer skickas till beställaren. Om du vill förtydliga något kan du ange ett
            meddelande.
          </p>
          <p>
            Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355), patientsäkerhetslagen
            (2010:659) eller Offentlighets- och sekretesslagen (2009:400).
          </p>
          <Textarea rows={5} onChange={handleChange} />
        </ModalBody>
        <ModalFooter>
          <SpinnerButton color={'primary'} accept={() => accept(fritextForklaring).then(handleClose)}>
            Bekräfta
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
}

export const AccepteraBestallningId = 'accepteraBestallning'
export default compose(modalContainer(AccepteraBestallningId))(AccepteraBestallning)
