import React, { Fragment } from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button, UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'
import AccepteraBestallning from './accepteraBestallning'
import AvvisaBestallning from './avvisaBestallning'

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

  return (
    <Fragment>
      { bestallning.status === 'Läst' ? <AccepteraBestallning accept={accept} /> : null }
      { bestallning.status === 'Läst' ? <AvvisaBestallning accept={reject} /> : null }
      { bestallning.status === 'Accepterad' ? <StyledButton outline={true} onClick={complete}>Klarmakera</StyledButton> : null }
      <StyledButton onClick={vidarebefodra}>Vidarebefodra</StyledButton>
      <UncontrolledButtonDropdown>
        <DropdownToggle caret>
          Skriv ut
        </DropdownToggle>
        <DropdownMenu>
          <DropdownItem>Förfrågan</DropdownItem>
          <DropdownItem>Fakturaunderlag</DropdownItem>
        </DropdownMenu>
      </UncontrolledButtonDropdown>
    </Fragment>
  )
};

export default compose(
  connect(null, actions)
)(BestallningActionBar);