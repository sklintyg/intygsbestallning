import React from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import { IbTypo09, IbTypo04, IbTypo01 } from '../styles/IbTypography'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import { ArrowBack, EventAvailableIcon, Block, Check, Create, InfoIcon } from '../styles/IbSvgIcons'
import IbAlert, { alertType } from '../alert/Alert.js'

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
  margin-left: -10px;
  margin-right: -10px;
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
    width: 14px;
    height: 16px;
  }
  &.hand {
    text-decoration: underline;
    cursor: pointer;
    &:hover {
      color: ${IbColors.IB_COLOR_21};
  }
`

const BestallningHeader = ({ props }) => {
  const getStatusIcon = () => {
    switch (props.bestallning.status) {
      case 'Oläst':
        return <InfoIcon color={IbColors.IB_COLOR_21} />
      case 'Läst':
      case 'Accepterad':
        return <Create color={IbColors.IB_COLOR_19} />
      case 'Avvisad':
        return <Block color={IbColors.IB_COLOR_19} />
      case 'Klar':
        return <Check color={IbColors.IB_COLOR_16} />
      default:
        return null
    }
  }
  return (
    <HeaderContainer>
      <CenterContainer>
        <div>
          <IconSpan onClick={props.history.goBack} className="hand" color={IbColors.IB_COLOR_07} as="span">
            <ArrowBack />
            Tillbaka till lista
          </IconSpan>
          <IbTypo09 as="span" color={IbColors.IB_COLOR_07}>
            Avser {props.bestallning.intygTyp}
          </IbTypo09>
          <IconSpan as="span" color={IbColors.IB_COLOR_07}>
            {getStatusIcon()}
            Status {props.bestallning.status}
          </IconSpan>
          <IconSpan as="span" color={IbColors.IB_COLOR_07}>
            <EventAvailableIcon />
            Inkom {props.bestallning.ankomstDatum}
          </IconSpan>
        </div>
        <ButtonRow>
          <div className="left">
            <IbTypo04 color={IbColors.IB_COLOR_19}>{props.bestallning.id}</IbTypo04>
            <IbTypo01 color={IbColors.IB_COLOR_19}>
              {props.bestallning.invanare.personId} - {props.bestallning.invanare.name}
            </IbTypo01>
            {props.bestallning.invanare.sekretessMarkering ? (
              <IbAlert type={alertType.SEKRETESS}>Patienten har sekretessmarkering</IbAlert>
            ) : null}
          </div>
          <BestallningActionBar bestallning={props.bestallning} goBack={props.history.goBack} />
        </ButtonRow>
      </CenterContainer>
    </HeaderContainer>
  )
}

export default BestallningHeader
