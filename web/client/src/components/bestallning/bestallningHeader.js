import React from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import * as ibTypo from '../styles/IbTypography'
import styled from 'styled-components'
import ibColors from '../styles/IbColors'
import * as ibIcons from '../styles/IbSvgIcons'

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
  border-top: 1px solid ${ibColors.IB_COLOR_15};
  padding-top: 10px;
  margin-top: 10px;
  align-items: center;
  justify-content: space-between;
`

const Tillbaka = styled(ibTypo.IbTypo09)`
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
        <Tillbaka onClick={props.history.goBack} as="span" color={ibColors.IB_COLOR_07}><ibIcons.ArrowBack/>Tillbaka till lista</Tillbaka>
        <ibTypo.IbTypo09 as="span" color={ibColors.IB_COLOR_07}>Förfrågan av Intygstyp '{props.bestallning.intygTyp}'</ibTypo.IbTypo09>
        <ibTypo.IbTypo09 as="span" color={ibColors.IB_COLOR_07}>Status {props.bestallning.status}</ibTypo.IbTypo09>
        <ibTypo.IbTypo09 as="span" color={ibColors.IB_COLOR_07}>Inkom {props.bestallning.ankomstDatum}</ibTypo.IbTypo09>
      </div>
      <ButtonRow>
        <div className="left">
          <ibTypo.IbTypo04 color={ibColors.IB_COLOR_19}>{props.bestallning.id}</ibTypo.IbTypo04>
          <ibTypo.IbTypo01 color={ibColors.IB_COLOR_06}>{props.bestallning.invanare.personId} - {props.bestallning.invanare.name}</ibTypo.IbTypo01>
        </div>
        <BestallningActionBar bestallning={props.bestallning} />
      </ButtonRow>
    </CenterContainer>
  </HeaderContainer>
)

export default BestallningHeader;