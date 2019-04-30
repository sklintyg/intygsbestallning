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
      return deleteBestallning(bestallning.id, { fritextForklaring })
        .then(openBorttagenDialog)
        .catch(() => {})
    }
  }

  const complete = () => completeBestallning(bestallning.id, 'COMPLETED')

  const vidarebefodra = () => {
    window.location.href = `mailto:?subject=${encodeURIComponent('Vidarebefordrad beställning')}&body=${encodeURIComponent(
      bestallning.metaData.filter((metaData) => metaData.typ === 'MAIL_VIDAREBEFORDRA')[0].text
    )}`
  }

  const printBestallning = (type) => {
    window.open('/api/bestallningar/' + bestallning.id + '/pdf/' + type, '_blank')
    return Promise.resolve()
  }

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
          <StyledButton onClick={openAcceptDialog} color={'primary'} id={'BestallningAcceptActionButton'}>
            <Check color={IbColors.IB_COLOR_00} /> Acceptera
          </StyledButton>
          <StyledButton onClick={openAvvisaDialog} color={'primary'}>
            <Block color={IbColors.IB_COLOR_00} /> Avvisa
          </StyledButton>
        </Fragment>
      )}
      {bestallning.status === 'Accepterad' && (
        <StyledButton onClick={complete} color={'primary'} id={'BestallningKlarActionButton'}>
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
          <DropdownItem onClick={bestallning.invanare.sekretessMarkering ? openSkrivUtDialog : () => printBestallning('forfragan')}>
            Förfrågan/Beställning
          </DropdownItem>
          <DropdownItem onClick={() => printBestallning('faktureringsunderlag')}>Fakturaunderlag</DropdownItem>
        </DropdownMenu>
      </ButtonDropdown>
      <BorttagenBestallning onClose={goBack} />
      <AccepteraBestallning accept={accept} />
      <AvvisaBestallning accept={reject} />
      <SkrivUtBestallning sekretess={bestallning.invanare.sekretessMarkering} accept={() => printBestallning('forfragan')} />
    </Fragment>
  )
}

export default compose(
  connect(
    null,
    { ...actions, ...modalActions }
  )
)(BestallningActionBar)
