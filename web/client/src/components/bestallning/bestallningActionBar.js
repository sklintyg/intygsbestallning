import React, {Fragment} from 'react'
import * as actions from '../../store/actions/bestallning'
import { connect } from 'react-redux'
import { Button, UncontrolledButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle } from 'reactstrap'
import { compose } from 'recompose'
import styled from 'styled-components'

const StyledButton = styled(Button)`
margin-right: 16px;
`

const BestallningActionBar = ({bestallning, accepteraBestallning, rejectBestallning}) => {

  const accept = () => accepteraBestallning(bestallning.id);
  
  const reject = () => rejectBestallning(bestallning.id, 'REJECTED');

  
  const complete = () => {
    //completeBestallning(bestallning.id, 'COMPLETED');
  }

  const vidarebefodra = () => {
    //vidarebefodra(bestallning.id)
  }

  return (
    <Fragment>
      { bestallning.status === 'LAST' ? <StyledButton onClick={accept}>Acceptera</StyledButton> : null }
      { bestallning.status === 'LAST' ? <StyledButton onClick={reject}>Avvisa</StyledButton> : null }
      { bestallning.status === 'ACCEPTERAD' ? <StyledButton outline={true} onClick={complete}>Klarmakera</StyledButton> : null }
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