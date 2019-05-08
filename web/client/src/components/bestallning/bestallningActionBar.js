import React, {Fragment, useState} from 'react'
import * as actions from '../../store/actions/bestallning'
import {connect} from 'react-redux'
import {Button, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle} from 'reactstrap'
import {compose} from 'recompose'
import styled from 'styled-components'
import {
  AccepteraBestallning,
  AccepteraBestallningId,
  AvvisaBestallning,
  AvvisaBestallningId,
  BorttagenBestallning,
  BorttagenBestallningId,
  SkrivUtBestallning,
  SkrivUtBestallningId,
} from './dialogs'
import {Block, Check, Print, Reply} from '../styles/IbSvgIcons'
import IbColors from '../styles/IbColors'
import * as modalActions from '../../store/actions/modal'
import Toggler from '../toggler/Toggler'


const StyledToggler = styled(Toggler)`
  button {
  padding: 0;
  }
`
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
          <StyledButton onClick={openAvvisaDialog} color={'primary'} id={'BestallningAvvisaActionButton'}>
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
        <DropdownToggle id="skrivUtBtn" color={'primary'}>
          <Print color={IbColors.IB_COLOR_00} /> Skriv ut <StyledToggler color={IbColors.IB_COLOR_00} expanded={dropdownOpen} />
        </DropdownToggle>
        <DropdownMenu right={true}>
          <DropdownItem id="skrivUtForfraganBtn" onClick={bestallning.invanare.sekretessMarkering ? openSkrivUtDialog : () => printBestallning('forfragan')}>
            Förfrågan/Beställning
          </DropdownItem>
          <DropdownItem id="skrivUtFakturaunderlagBtn" onClick={() => printBestallning('faktureringsunderlag')}>Fakturaunderlag</DropdownItem>
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
