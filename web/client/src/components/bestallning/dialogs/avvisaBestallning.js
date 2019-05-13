import React, {Fragment, useState} from 'react'
import {Button, Modal, ModalBody, ModalFooter, ModalHeader} from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import {compose} from 'recompose'
import RadioButton from '../../radioButton'
import SpinnerButton from '../../spinnerButton'
import IbTextarea from '../../styles/IbTexarea'

const AvvisaBestallning = ({ handleClose, isOpen, accept }) => {
  const [fritextForklaring, setFritextForklaring] = useState('')
  const [avvisa, setAvvisa] = useState(undefined)

  const handleTextareaChange = (value) => {
    setFritextForklaring(value)
  }

  const handleRadioChange = (e) => {
    setAvvisa(e.target.value)
  }

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
        <ModalHeader toggle={handleClose}>Avvisa</ModalHeader>
        <ModalBody>
          <p>Du kan välja att bara avvisa beställningen eller att även radera den.</p>
          <p>
            Radering får endast ske om beställningen har skickats till fel vårdenhet och någon journalanteckning med anledning av
            beställningen inte har upprättats.
          </p>
          <RadioButton onChange={handleRadioChange} label={'Avvisa'} selected={avvisa} value={'true'} id={'AvvisaDialogRadioAvvisa'} />
          <RadioButton onChange={handleRadioChange} label={'Avvisa och radera'} selected={avvisa} value={'false'} id={'AvvisaDialogRadioDelete'} />
          <p>Vänligen förtydliga skälet till varför beställningen avvisas eller raderas.</p>
          <p>
            Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355), patientsäkerhetslagen
            (2010:659) eller Offentlighets- och sekretesslagen (2009:400).
          </p>
          <IbTextarea placeholder={''} minRows={3} maxRows={6} maxLength={4000} onChange={handleTextareaChange}/>
        </ModalBody>
        <ModalFooter>
          <SpinnerButton
            color={'primary'}
            disabled={avvisa === undefined}
            accept={() => accept(fritextForklaring, avvisa).then(handleClose)}
            id={'AvvisaDialogConfirmButton'}>
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

export const AvvisaBestallningId = 'avvisaBestallning'
export default compose(modalContainer(AvvisaBestallningId))(AvvisaBestallning)
