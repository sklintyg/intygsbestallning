import React, { Fragment } from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button, UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'
import {AccepteraBestallning, AvvisaBestallning, SkrivUtBestallning} from './dialogs'
import { Check, Reply, Print } from '../styles/IbSvgIcons'
import IbColors from '../styles/IbColors'

const StyledButton = styled(Button)`
  margin-right: 16px;
`

const BestallningActionBar = ({bestallning, accepteraBestallning, rejectBestallning}) => {

  const accept = (fritextForklaring) => accepteraBestallning(bestallning.id, {fritextForklaring});
  
  const reject = (fritextForklaring, avvisa) => {
    if (avvisa) {
      rejectBestallning(bestallning.id, {fritextForklaring});
    } else {
      //raderaBestallning();
    }
  }
  
  const complete = () => {
    //completeBestallning(bestallning.id, 'COMPLETED');
  }

  const vidarebefodra = () => {
    //vidarebefodra(bestallning.id)
  }

  const printBestallning = () => {

  }

  return (
    <Fragment>
      { bestallning.status === 'Läst' ? <AccepteraBestallning accept={accept} /> : null }
      { bestallning.status === 'Läst' ? <AvvisaBestallning accept={reject} /> : null }
      { bestallning.status === 'Accepterad' ? <StyledButton onClick={complete}><Check color={IbColors.IB_COLOR_00}/> Klarmakera</StyledButton> : null }
      <StyledButton onClick={vidarebefodra} color={'primary'}><Reply color={IbColors.IB_COLOR_00}/> Vidarebefodra</StyledButton>
      <UncontrolledButtonDropdown>
        <DropdownToggle caret color={'primary'}>
        <Print color={IbColors.IB_COLOR_00}/> Skriv ut
        </DropdownToggle>
        <DropdownMenu>
          <SkrivUtBestallning sekretess={bestallning.invanare.sekretessMarkering} accept={printBestallning}/>
          <DropdownItem>Fakturaunderlag</DropdownItem>
        </DropdownMenu>
      </UncontrolledButtonDropdown>
    </Fragment>
  )
};

export default compose(
  connect(null, actions)
)(BestallningActionBar);