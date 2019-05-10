import React from 'react'
import modalContainer from '../../modalContainer/modalContainer'
import { compose } from 'recompose'
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap'

const SekretessInfo = ({ handleClose, isOpen }) => {
  return (
    <Modal isOpen={isOpen} size={'md'} backdrop={true} toggle={handleClose}>
      <ModalHeader toggle={handleClose}>Patienten har en sekretessmarkering</ModalHeader>
      <ModalBody>
        <p>
          Att en invånare har sekretessmarkerade personuppgifter betyder att Skatteverket har bedömt att invånarens personuppgifter är extra
          viktiga att skydda. Det finns speciella riktlinjer för hur personuppgifter för de invånarna ska hanteras. I Intygsbeställning
          innebär det att:
        </p>
        <ul>
          <li>
            Du som användare av Intygsbeställning ska behandla personuppgifterna med försiktighet. Samtliga personuppgifter rörande
            invånaren är skyddsvärda.
          </li>
          <li>
            En symbol visas i alla vyer i gränssnittet där personuppgifter visas som indikerar att invånaren har sekretessmarkerade
            personuppgifter.
          </li>
        </ul>
      </ModalBody>
      <ModalFooter>
        <Button
          color={'default'}
          onClick={() => {
            handleClose()
          }}
          id={'SekretessInfoDialogCloseConfirmButton'}>
          Stäng
        </Button>
      </ModalFooter>
    </Modal>
  )
}

export const SekretessInfoDialogId = 'SekretessInfoDialogId'
export default compose(modalContainer(SekretessInfoDialogId))(SekretessInfo)
