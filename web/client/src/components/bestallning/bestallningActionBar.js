import React, { Fragment, useState } from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'
import {
  AccepteraBestallning,
  AccepteraBestallningId,
  AvvisaBestallning,
  AvvisaBestallningId,
  SkrivUtBestallning,
  SkrivUtBestallningId,
  BorttagenBestallning,
  BorttagenBestallningId,
} from './dialogs'
import { Check, Reply, Print, Block } from '../styles/IbSvgIcons'
import IbColors from '../styles/IbColors'
import * as modalActions from '../../store/actions/modal'

const StyledButton = styled(Button)`
  margin-right: 16px;
`

const BestallningActionBar = ({
  bestallning,
  accepteraBestallning,
  rejectBestallning,
  deleteBestallning,
  completeBestallning,
  goBack,
  openModal,
}) => {
  const [dropdownOpen, setDropdownOpen] = useState(false)

  const accept = (fritextForklaring) => accepteraBestallning(bestallning.id, { fritextForklaring })

  const reject = (fritextForklaring, avvisa) => {
    if (avvisa === 'true') {
      return rejectBestallning(bestallning.id, { fritextForklaring })
    } else {
      return deleteBestallning(bestallning.id, { fritextForklaring }).then(openBorttagenDialog)
    }
  }

  const complete = () => completeBestallning(bestallning.id, 'COMPLETED')

  const vidarebefodra = () => {
    //vidarebefodra(bestallning.id)
  }

  const printBestallning = () => {}

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen)
  }

  const openAcceptDialog = () => openModal(AccepteraBestallningId)
  const openAvvisaDialog = () => openModal(AvvisaBestallningId)
  const openSkrivUtDialog = () => openModal(SkrivUtBestallningId)
  const openBorttagenDialog = () => openModal(BorttagenBestallningId)

  return (
    <Fragment>
      {bestallning.status === 'Läst' && (
        <Fragment>
          <StyledButton onClick={openAcceptDialog} color={'primary'}>
            <Check color={IbColors.IB_COLOR_00} /> Acceptera
          </StyledButton>
          <StyledButton onClick={openAvvisaDialog} color={'primary'}>
            <Block color={IbColors.IB_COLOR_00} /> Avvisa
          </StyledButton>
        </Fragment>
      )}
      {bestallning.status === 'Accepterad' && (
        <StyledButton onClick={complete} color={'primary'}>
          <Check color={IbColors.IB_COLOR_00} /> Klarmarkera
        </StyledButton>
      )}
      <StyledButton onClick={vidarebefodra} color={'primary'}>
        <Reply color={IbColors.IB_COLOR_00} /> Vidarebefodra
      </StyledButton>
      <ButtonDropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
        <DropdownToggle color={'primary'} className={dropdownOpen ? 'dropdown-toggle up-icon' : 'dropdown-toggle down-icon'}>
          <Print color={IbColors.IB_COLOR_00} /> Skriv ut
        </DropdownToggle>
        <DropdownMenu right={true}>
          <DropdownItem onClick={bestallning.invanare.sekretessMarkering ? openSkrivUtDialog : printBestallning}>
            Förfrågan/Beställning
          </DropdownItem>
          <DropdownItem>Fakturaunderlag</DropdownItem>
        </DropdownMenu>
      </ButtonDropdown>
      <BorttagenBestallning onClose={goBack} />
      <AccepteraBestallning accept={accept} />
      <AvvisaBestallning accept={reject} />
      <SkrivUtBestallning sekretess={bestallning.invanare.sekretessMarkering} accept={printBestallning} />
    </Fragment>
  )
}

export default compose(
  connect(
    null,
    { ...actions, ...modalActions }
  )
)(BestallningActionBar)
