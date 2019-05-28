import React, { Fragment, useState } from 'react'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import RadioButton from '../../radioButton'
import SpinnerButton from '../../spinnerButton'
import IbTextarea from '../../styles/IbTexarea'
import { IbTypo11 } from '../../styles/IbTypography'
import IbCheckbox from '../../styles/IbCheckbox'
import styled from 'styled-components'

const MotivationSection = styled.div`
  padding-top: 20px;
  padding-bottom: 5px;
`

export const OPTION_AVVISA = 'avvisa'
export const OPTION_AVVISA_RADERA = 'avvisaRadera'

const AvvisaBestallning = ({ handleClose, isOpen, accept }) => {
  const [fritextForklaring, setFritextForklaring] = useState('')
  const [raderaConfirmed, setRaderaConfirmed] = useState(undefined)
  const [avvisaSelection, setAvvisaSelection] = useState(undefined)

  const handleTextareaChange = (value) => {
    setFritextForklaring(value)
  }

  const handleRadioChange = (e) => {
    setRaderaConfirmed(undefined)
    setAvvisaSelection(e.target.value)
  }
  const handleRaderaToggle = (e) => {
    setRaderaConfirmed(e.target.checked)
  }

  const onClose = () => {
    setRaderaConfirmed(undefined)
    setAvvisaSelection(undefined)
    handleClose()
  }
  const confirmEnabled = () => avvisaSelection === OPTION_AVVISA || (avvisaSelection === OPTION_AVVISA_RADERA && raderaConfirmed === true)

  return (
    <Fragment>
      <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={onClose}>
        <ModalHeader toggle={onClose}>Avvisa</ModalHeader>
        <ModalBody>
          <p>Du kan välja att bara avvisa förfrågan eller att även radera den.</p>
          <p>
            Radering får endast ske om förfrågan har skickats till fel vårdenhet och någon journalanteckning med anledning av
            förfrågan inte har upprättats.
          </p>
          <RadioButton
            onChange={handleRadioChange}
            label={'Avvisa'}
            selected={avvisaSelection}
            value={OPTION_AVVISA}
            id={'AvvisaDialogRadioAvvisa'}
          />
          <RadioButton
            onChange={handleRadioChange}
            label={'Avvisa och radera'}
            selected={avvisaSelection}
            value={OPTION_AVVISA_RADERA}
            id={'AvvisaDialogRadioDelete'}
          />
          {avvisaSelection !== undefined && (
            <MotivationSection>
              <p>Vänligen berätta för avsändaren varför förfrågan avvisas eller raderas.</p>
              <IbTypo11 as="p">
                Observera att patientrelaterad information inte får lämnas ut utan stöd i patientdatalagen (2008:355), patientsäkerhetslagen
                (2010:659) eller Offentlighets- och sekretesslagen (2009:400).
              </IbTypo11>
              <p><IbTextarea placeholder={''} minRows={3} maxRows={6} maxLength={4000} onChange={handleTextareaChange} /></p>
              {avvisaSelection === OPTION_AVVISA_RADERA && (
                <IbCheckbox
                  id={'confirmAvvisaRaderaCheckbox'}
                  label="Är du säker på att du vill radera förfrågan? Förfrågan kommer att raderas permanent och kan inte återställas."
                  onChange={handleRaderaToggle}
                  selected={raderaConfirmed}
                  value={true}
                />
              )}
            </MotivationSection>
          )}
        </ModalBody>
        <ModalFooter>
          <SpinnerButton
            color={'primary'}
            disabled={!confirmEnabled()}
            accept={() => accept(fritextForklaring, avvisaSelection).then(onClose)}
            id={'AvvisaDialogConfirmButton'}>
            Bekräfta
          </SpinnerButton>
          <Button
            color={'default'}
            onClick={() => {
              onClose()
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
