import React, {Fragment} from 'react'
import {compose} from 'recompose'
import {connect} from 'react-redux'

import WelcomeChart from './WelcomeChart'

import ibColors from '../styles/IbColors'
import {IbTypo02, IbTypo07} from '../styles/IbTypography'
import styled from 'styled-components'

import LoadingSpinner from '../loadingSpinner'
import {getStat, isPending} from '../../store/reducers/sessionPoll'

const Color07 = styled.div`
  color: ${ibColors.IB_COLOR_07};
`

const Wrapper = styled.div`
  display: flex;
  flex-direction: row;
  padding: 50px;
`
const WelcomeTextCol = styled.div`
  flex: 1 0 30%;
`
const WelcomeChartCol = styled.div`
  flex: 1 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
`

const SpinnerContainer = styled.div`
  position: relative;
  height: 100%;
  width: 100%;
`

const WelcomeContainer = ({ welcomeStats, isFetching }) => (
  <Fragment>
    <Wrapper>
      <WelcomeTextCol>
        <Color07>
          <IbTypo02 as="h1">Du är nu inloggad i tjänsten Intygsbeställning</IbTypo02>
          <IbTypo07 as="p">
            Här kan du ta del av och hantera de förfrågningar och beställningar av intyg som har inkommit till din vårdenhet.
          </IbTypo07>
        </Color07>
      </WelcomeTextCol>
      <WelcomeChartCol>
        {isFetching && (
          <SpinnerContainer>
            <LoadingSpinner loading={isFetching} message={'Hämtar diagram'} />
          </SpinnerContainer>
        )}
        {!isFetching && <WelcomeChart stats={welcomeStats} />}
      </WelcomeChartCol>
    </Wrapper>
  </Fragment>
)

const mapStateToProps = (state) => {
  return {
    welcomeStats: getStat(state),
    isFetching: isPending(state),
  }
}

export default compose(connect(mapStateToProps))(WelcomeContainer)
