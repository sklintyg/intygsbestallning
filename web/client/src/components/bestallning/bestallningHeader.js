import React, { Fragment } from 'react'
import BestallningActionBar from './bestallningActionBar'
import ibValues from '../styles/IbValues'
import { IbTypo04, IbTypo01 } from '../styles/IbTypography'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import { ArrowBack, EventAvailableIcon, Block, Check, Create, InfoIcon } from '../styles/IbSvgIcons'
import IbAlert, { alertType } from '../alert/Alert.js'
import { Link } from 'react-router-dom'
import { compose } from 'recompose'
import { connect } from 'react-redux'
import { openModal } from '../../store/actions/modal'
import { Button } from 'reactstrap'
import SekretessInfo, { SekretessInfoDialogId } from './dialogs/sekretessInfo'

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
const InternLank = styled(Button)`
  color: ${IbColors.IB_COLOR_17} !important;
  text-decoration: underline !important;
  &:hover {
    color: ${IbColors.IB_COLOR_21} !important;
  }
`

const BestallningHeader = ({ bestallning, history, error, openModal }) => {
  const openSekretessInfoDialog = () => {
    return openModal(SekretessInfoDialogId)
  }

  const getStatusIcon = () => {
    switch (bestallning.status) {
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
  if (history && history.location && history.location.state && history.location.state.fromList) {
    backPath += history.location.state.fromList
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
          {!error && (
            <Fragment>
              <span>Avser {bestallning.intygTypBeskrivning}</span>
              <span>
                {getStatusIcon()}
                Status {bestallning.status}
              </span>
              <span>
                <EventAvailableIcon />
                Inkom {bestallning.ankomstDatum}
              </span>
            </Fragment>
          )}
        </div>
        {!error && (
          <ButtonRow>
            <div className="left">
              <IbTypo04 color={IbColors.IB_COLOR_19}>{bestallning.id}</IbTypo04>

              {bestallning.invanare.sekretessMarkering ? (
                <IbTypo01 color={IbColors.IB_COLOR_19}>{bestallning.invanare.personId}</IbTypo01>
              ) : (
                <IbTypo01 color={IbColors.IB_COLOR_19}>
                  {bestallning.invanare.personId} {bestallning.invanare.headerName}
                </IbTypo01>
              )}

              {bestallning.invanare.sekretessMarkering ? (
                <IbAlert type={alertType.SEKRETESS}>
                  <InternLank color="link" onClick={openSekretessInfoDialog}>
                    Patienten har sekretessmarkering
                  </InternLank>
                  <SekretessInfo />
                </IbAlert>
              ) : null}
            </div>
            <BestallningActionBar bestallning={bestallning} goBack={history.goBack} />
          </ButtonRow>
        )}
      </CenterContainer>
    </HeaderContainer>
  )
}

export default compose(
  connect(
    null,
    { openModal }
  )
)(BestallningHeader)
