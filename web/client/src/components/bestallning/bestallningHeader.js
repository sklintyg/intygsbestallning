import React from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import * as ibTypo from '../styles/IbTypography'
import styled from 'styled-components'
import Colors from '../styles/IbColors'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  padding: 10px 30px 20px;
`

const HeaderContainer = styled.div`
  box-shadow: 0px 5px 9px -6px #000;
  position: relative;
`

const ButtonRow = styled.div`
  display: flex;
  .left {
    flex: 1 0;
  }
  .right {
    
  }
  border-top: 1px solid ${Colors.IB_COLOR_15};
  padding-top: 10px;
  margin-top: 10px;
  align-items: flex-end;
  justify-content: space-between;
`


const BestallningHeader = ({props}) => (
  <HeaderContainer>
    <CenterContainer>
      <div>
        <span onClick={props.history.goBack}>Tillbaka till lista</span>
        <span> :: Förfrågan av Intygstyp '{props.bestallning.intygTyp}'</span>
        <span> :: Status {props.bestallning.status}</span>
        <span> :: Inkom {props.bestallning.ankomstDatum}</span>
      </div>
      <ButtonRow>
        <div className="left">
          <ibTypo.IbTypo05>{props.bestallning.id}</ibTypo.IbTypo05>
          <ibTypo.IbTypo01>{props.bestallning.invanare.personId} - {props.bestallning.invanare.name}</ibTypo.IbTypo01>
        </div>
        <BestallningActionBar bestallning={props.bestallning} />
      </ButtonRow>
    </CenterContainer>
  </HeaderContainer>
)

export default BestallningHeader;