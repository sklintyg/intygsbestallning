import React from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import styled from 'styled-components'

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

const BestallningHeader = ({props}) => (
  <HeaderContainer>
    <CenterContainer>
      <div>
        <span onClick={props.history.goBack}>Tillbaka till lista</span>
        <span> :: Förfrågan av Intygstyp '{props.bestallning.intygTyp}'</span>
        <span> :: Status {props.bestallning.status}</span>
        <span> :: Inkom {props.bestallning.ankomstDatum}</span>
      </div>
      <div>{props.bestallning.id}</div>
      <div>{props.bestallning.invanare.personId} - {props.bestallning.invanare.name}</div>
      <BestallningActionBar bestallning={props.bestallning} />
    </CenterContainer>
  </HeaderContainer>
)

export default BestallningHeader;