import React, { Fragment } from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import { IbTypo04, IbTypo01 } from '../styles/IbTypography'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import { ArrowBack, EventAvailableIcon, Block, Check, Create, InfoIcon } from '../styles/IbSvgIcons'
import IbAlert, { alertType } from '../alert/Alert.js'
import { Link } from 'react-router-dom'

const CenterContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: ${ibValues.maxContentWidth};
  padding: 10px 30px 20px;
  > div {
    display: flex;
    > span,
    > a {
      color: ${IbColors.IB_COLOR_07};
      font-weight: 400;
      font-size: 12px;
      display: inline-block;
      margin-right: 50px;
      position: relative;
      svg {
        position: absolute;
        left: -17px;
        width: 14px;
        height: 16px;
      }
    }
    > a {
      text-decoration: underline;
      &:hover {
        color: ${IbColors.IB_COLOR_21};
        svg {
          fill: ${IbColors.IB_COLOR_21};
        }
      }
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

  let backPath = '/bestallningar/'
  if (props.history && props.history.location && props.history.location.state && props.history.location.state.fromList) {
    backPath += props.history.location.state.fromList
  } else {
    backPath += 'AKTUELLA'
  }

  return (
    <HeaderContainer>
      <CenterContainer>
        <div>
          <Link to={backPath} id={'BestallningBackToList'}>
            <ArrowBack />
            Tillbaka till lista
          </Link>
          {!props.error && (
            <Fragment>
              <span>Avser {props.bestallning.intygTypBeskrivning}</span>
              <span>
                {getStatusIcon()}
                Status {props.bestallning.status}
              </span>
              <span>
                <EventAvailableIcon />
                Inkom {props.bestallning.ankomstDatum}
              </span>
            </Fragment>
          )}
        </div>
        {!props.error && (
          <ButtonRow>
            <div className="left">
              <IbTypo04 color={IbColors.IB_COLOR_19}>{props.bestallning.id}</IbTypo04>

              {
                props.bestallning.invanare.sekretessMarkering
                  ? (
                    <IbTypo01 color={IbColors.IB_COLOR_19}>
                      {props.bestallning.invanare.personId}
                    </IbTypo01>)
                  : (
                    <IbTypo01 color={IbColors.IB_COLOR_19}>
                      {props.bestallning.invanare.personId} {props.bestallning.invanare.headerName}
                    </IbTypo01>)
              }

              {props.bestallning.invanare.sekretessMarkering ? (
                <IbAlert type={alertType.SEKRETESS}>Patienten har sekretessmarkering</IbAlert>
              ) : null}
            </div>
            <BestallningActionBar bestallning={props.bestallning} goBack={props.history.goBack} />
          </ButtonRow>
        )}
      </CenterContainer>
    </HeaderContainer>
  )
}

export default BestallningHeader
