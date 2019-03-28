import React, { Fragment, useState } from 'react'
import { Debounce } from 'react-throttle'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import styled from 'styled-components'
import modalContainer from '../modalContainer/modalContainer'
import { compose } from 'recompose'

const StyledButton = styled(Button)`
margin-right: 16px;
`

const Textarea = styled.textarea`
  width: 100%;
  resize: none;
`

const AccepteraBestallning = ({handleOpen, handleClose, isOpen, accept}) => {
  const [fritextForklaring, setFritextForklaring] = useState("");

  const handleChange = (e) => {
    setFritextForklaring(e.target.value);
  }

  return (
    <Fragment>
      <StyledButton onClick={handleOpen}>Acceptera</StyledButton>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Acceptera</ModalHeader>
        <ModalBody>
          <p>Ett meddelande om att beställningen accepteras kommer skickas till beställaren. Om du vill förtydliga något kan du ange ett meddelande.</p>
          <p>Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355), patientsäkerhetslagen (2010:659) eller Offentlighets- och sekretesslagen (2009:400).</p>
          <Debounce time="1000" handler="onChange">
            <Textarea rows="5" onChange={handleChange} />
          </Debounce>
        </ModalBody>
        <ModalFooter>
          <Button color={'secondary'} outline={true} onClick={() => {handleClose()}}>Avbryt</Button>
          <Button color={'primary'} onClick={() => {accept(fritextForklaring); handleClose()}}>Bekräfta</Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

  
export default compose(
  modalContainer('accepteraBestallning')
)(AccepteraBestallning)