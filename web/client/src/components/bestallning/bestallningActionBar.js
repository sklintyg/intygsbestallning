import React, { Fragment, useState } from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'
import { AccepteraBestallning, AvvisaBestallning, SkrivUtBestallning } from './dialogs'
import { Check, Reply, Print } from '../styles/IbSvgIcons'
import IbColors from '../styles/IbColors'
import BorttagenBestallning from "./dialogs/borttagenBestallning";

const StyledButton = styled(Button)`
  margin-right: 16px;
`

const BestallningActionBar = ({bestallning, accepteraBestallning, rejectBestallning, deleteBestallning, completeBestallning, goBack}) => {
  const [dropdownOpen, setDropdownOpen] = useState(false);

  const accept = (fritextForklaring) => accepteraBestallning(bestallning.id, {fritextForklaring});

  const reject = (fritextForklaring, avvisa) => {
    if (avvisa) {
      rejectBestallning(bestallning.id, {fritextForklaring});
    } else {
      deleteBestallning(bestallning.id, {fritextForklaring});
    }
  }

  const complete = () => {
    completeBestallning(bestallning.id, 'COMPLETED');
  }

  const vidarebefodra = () => {
    //vidarebefodra(bestallning.id)
  }

  const printBestallning = () => {

  }

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen)
  }

  return (
    <Fragment>
      { bestallning.status === 'Läst' ? <AccepteraBestallning accept={accept} /> : null }
      { bestallning.status === 'Läst' ? <AvvisaBestallning accept={reject} goBack={goBack} /> : null }
      { bestallning.status === 'Accepterad' ? <StyledButton onClick={complete} color={'primary'}><Check color={IbColors.IB_COLOR_00}/> Klarmarkera</StyledButton> : null }
      <StyledButton onClick={vidarebefodra} color={'primary'}><Reply color={IbColors.IB_COLOR_00}/> Vidarebefodra</StyledButton>
      <ButtonDropdown isOpen={dropdownOpen} toggle={toggleDropdown}>
        <DropdownToggle color={'primary'} className={dropdownOpen ? 'dropdown-toggle up-icon' : 'dropdown-toggle down-icon'}>
          <Print color={IbColors.IB_COLOR_00}/> Skriv ut
        </DropdownToggle>
        <DropdownMenu right={true}>
          <SkrivUtBestallning sekretess={bestallning.invanare.sekretessMarkering} accept={printBestallning}/>
          <DropdownItem>Fakturaunderlag</DropdownItem>
        </DropdownMenu>
      </ButtonDropdown>
    </Fragment>
  )
};

export default compose(
  connect(null, actions)
)(BestallningActionBar);
