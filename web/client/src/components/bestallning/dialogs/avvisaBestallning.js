import React, { Fragment, useState } from 'react'
import { Debounce } from 'react-throttle'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import styled from 'styled-components'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Block } from '../../styles/IbSvgIcons'
import IbColors from '../../styles/IbColors'

const StyledButton = styled(Button)`
margin-right: 16px;
`

const Textarea = styled.textarea`
  width: 100%;
  resize: none;
`

const AvvisaBestallning = ({handleOpen, handleClose, isOpen, accept}) => {
  const [fritextForklaring, setFritextForklaring] = useState("");
  const [avvisa, setAvvisa] = useState(undefined);

  const handleTextareaChange = (e) => {
    setFritextForklaring(e.target.value);
  }

  const handleRadioChange = (e) => {
    setAvvisa(e.target.value === "true");
  }

  return (
    <Fragment>
      <StyledButton onClick={handleOpen} color="primary"><Block color={IbColors.IB_COLOR_00}/> Avvisa</StyledButton>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Avvisa</ModalHeader>
        <ModalBody>
          <p>Du kan välja att bara avvisa beställningen eller att även radera den.</p>
          <p>Radering får endast ske om beställningen har skickats till fel vårdenhet och någon journalanteckning med anledning av beställningen inte har upprättats.</p>
          <input type="radio" name="avvisa" id="avvisa" value="true" onChange={handleRadioChange}/>Avvisa
          <input type="radio" name="avvisa" id="radera" value="false" onChange={handleRadioChange}/>Radera
          <p>Vänligen förtydliga skälet till varför beställningen avvisas eller raderas.</p>
          <p>Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355), patientsäkerhetslagen (2010:659) eller Offentlighets- och sekretesslagen (2009:400).</p>
          <Debounce time="1000" handler="onChange">
            <Textarea rows="5" onChange={handleTextareaChange} />
          </Debounce>
        </ModalBody>
        <ModalFooter>
          <Button color={'secondary'} outline={true} onClick={() => {handleClose()}}>Avbryt</Button>
          <Button color={'primary'} disabled={avvisa === undefined} onClick={() => {accept(fritextForklaring, avvisa); handleClose()}}>Bekräfta</Button>
        </ModalFooter>
      </Modal>
    </Fragment>
  )
}

  
export default compose(
  modalContainer('avvisaBestallning')
)(AvvisaBestallning)