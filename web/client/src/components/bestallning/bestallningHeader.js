import React from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import { IbTypo09, IbTypo04, IbTypo01 } from '../styles/IbTypography'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import { ArrowBack, Error } from '../styles/IbSvgIcons'
import IbAlert from '../alert/Alert.js'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  padding: 10px 30px 20px;
  > div {
    display: flex;
    > span {
      margin-right: 50px;
    }
  }
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
  border-top: 1px solid ${IbColors.IB_COLOR_15};
  padding-top: 10px;
  margin-top: 10px;
  align-items: center;
  justify-content: space-between;
`

const IconSpan = styled(IbTypo09)`
  position: relative;
  svg {
    position: absolute;
    left: -17px;
    width: 16px;
    top: -4px;
  }
`

const BestallningHeader = ({props}) => (
  <HeaderContainer>
    <CenterContainer>
      <div>
        <IconSpan onClick={props.history.goBack} as="span" color={IbColors.IB_COLOR_07}><ArrowBack/>Tillbaka till lista</IconSpan>
        <IbTypo09 as="span" color={IbColors.IB_COLOR_07}>Förfrågan av Intygstyp '{props.bestallning.intygTyp}'</IbTypo09>
        <IconSpan as="span" color={IbColors.IB_COLOR_07}><Error/>Status {props.bestallning.status}</IconSpan>
        <IbTypo09 as="span" color={IbColors.IB_COLOR_07}>Inkom {props.bestallning.ankomstDatum}</IbTypo09>
      </div>
      <ButtonRow>
        <div className="left">
          <IbTypo04 color={IbColors.IB_COLOR_19}>{props.bestallning.id}</IbTypo04>
          <IbTypo01 color={IbColors.IB_COLOR_06}>{props.bestallning.invanare.personId} - {props.bestallning.invanare.name}</IbTypo01>
          {true ? <IbAlert type="sekretess">Patienten har sekretessmarkering</IbAlert> : null}
        </div>
        <BestallningActionBar bestallning={props.bestallning} />
      </ButtonRow>
    </CenterContainer>
  </HeaderContainer>
)

export default BestallningHeader;