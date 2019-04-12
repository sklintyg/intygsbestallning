import React from 'react'
import { Redirect, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/bestallning'
import * as modalActions from '../../store/actions/modal'
import { getBestallning, getErrorMessage, isFetching } from '../../store/reducers/bestallning'
import BestallningFraga from './bestallningFraga'
import BestallningHeader from './bestallningHeader'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../styles/ibLayout'
import AppFooter from '../appFooter/AppFooter'
import styled from 'styled-components'
import Colors from '../styles/IbColors'
import { compose, lifecycle } from 'recompose'
import LoadingSpinner from '../loadingSpinner'

const CustomScrollingContainer = styled(ScrollingContainer)`
  background-color: ${Colors.IB_COLOR_27};
  max-width: none;
`

const SpinnerContainer = styled.div`
  position: relative;
  height: 100%;
  width: 100%;
`

const BestallningContainer = ({ error, bestallning, history, fetching }) => {
  const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object

  if (error && bestallningIsEmpty) {
    return <Redirect to={'/exit/' + error.error.errorCode} />
  }

  if (fetching) {
    return (
      <SpinnerContainer>
        <LoadingSpinner loading={fetching} message={'Hämtar Beställning'} />
      </SpinnerContainer>
    )
  }

  return bestallningIsEmpty ? null : (
    <FlexColumnContainer>
      <BestallningHeader props={{ bestallning, history }} />
      <CustomScrollingContainer>
        <WorkareaContainer>
          {bestallning.fragor.map((b, i) => (
            <BestallningFraga key={i} props={b} />
          ))}
        </WorkareaContainer>
        <AppFooter />
      </CustomScrollingContainer>
    </FlexColumnContainer>
  )
}

const fetchData = ({ fetchBestallning, id }) => {
  fetchBestallning(id)
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
  componentDidUpdate(prevProps) {
    if (this.props.id !== prevProps.id) {
      fetchData(this.props)
    }
    if (this.props.error && this.props.error.modal) {
      this.props.closeAllModals()
      this.props.displayErrorModal(this.props.error.modal)
    }
  },
}

const mapStateToProps = (state, { match, history }) => {
  const id = match.params.id
  return {
    id,
    history,
    bestallning: getBestallning(state),
    error: getErrorMessage(state),
    fetching: isFetching(state),
  }
}

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    { ...actions, ...modalActions }
  ),
  lifecycle(lifeCycleValues)
)(BestallningContainer)
