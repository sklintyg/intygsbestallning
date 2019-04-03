import React, { Fragment, useState } from 'react'
import { Button, Modal, ModalBody, ModalHeader, ModalFooter } from 'reactstrap'
import styled from 'styled-components'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Block } from '../../styles/IbSvgIcons'
import IbColors from '../../styles/IbColors'
import BorttagenBestallning from "./borttagenBestallning";

const StyledButton = styled(Button)`
  margin-right: 16px;
`

const Textarea = styled.textarea`
  width: 100%;
  resize: none;
`

const AvvisaBestallning = ({ handleOpen, handleClose, isOpen, accept, goBack }) => {
  const [fritextForklaring, setFritextForklaring] = useState('')
  const [avvisa, setAvvisa] = useState(undefined)

  const handleTextareaChange = e => {
    setFritextForklaring(e.target.value)
  }

  const handleRadioChange = e => {
    setAvvisa(e.target.value === 'true')
  }

  return (
    <Fragment>
      <StyledButton onClick={handleOpen} color={'primary'}>
        <Block color={IbColors.IB_COLOR_00} /> Avvisa
      </StyledButton>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Avvisa</ModalHeader>
        <ModalBody>
          <p>Du kan välja att bara avvisa beställningen eller att även radera den.</p>
          <p>
            Radering får endast ske om beställningen har skickats till fel vårdenhet och någon journalanteckning med
            anledning av beställningen inte har upprättats.
          </p>
          <div className='radio-wrapper'>
            <div className='ib-radio'>
              <input
                type='radio'
                name='avvisa'
                id='avvisa'
                value='true'
                checked={avvisa === true}
                onChange={handleRadioChange}
              />
              <label onClick={() => setAvvisa(true)}>Avvisa</label>
            </div>
          </div>
          <div className='radio-wrapper'>
            <div className='ib-radio'>
              <input
                className='ib-radio'
                type='radio'
                name='avvisa'
                id='radera'
                value='false'
                checked={avvisa === false}
                onChange={handleRadioChange}
              />
              <label onClick={() => setAvvisa(false)}>Radera</label>
            </div>
          </div>
          <p>Vänligen förtydliga skälet till varför beställningen avvisas eller raderas.</p>
          <p>
            Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355),
            patientsäkerhetslagen (2010:659) eller Offentlighets- och sekretesslagen (2009:400).
          </p>
          <Textarea rows='5' onChange={handleTextareaChange} />
        </ModalBody>
        <ModalFooter>
          <Button
            color={'primary'}
            disabled={avvisa === undefined}
            onClick={() => {
              accept(fritextForklaring, avvisa)
              handleClose()
            }}>
            Bekräfta
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
      <BorttagenBestallning onClose={goBack}/>
    </Fragment>
  )
}

export default compose(modalContainer('avvisaBestallning'))(AvvisaBestallning)
