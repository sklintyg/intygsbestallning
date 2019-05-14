import React from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/bestallning'
import * as modalActions from '../../store/actions/modal'
import { getBestallning, getErrorMessage, isFetching } from '../../store/reducers/bestallning'
import BestallningFraga from './bestallningFraga'
import BestallningHeader from './bestallningHeader'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../styles/ibLayout'
import styled from 'styled-components'
import Colors from '../styles/IbColors'
import { compose, lifecycle } from 'recompose'
import LoadingSpinner from '../loadingSpinner'
import {ErrorPageIcon} from "../styles/IbSvgIcons"
import ErrorMessageFormatter from "../../messages/ErrorMessageFormatter"

const CustomScrollingContainer = styled(ScrollingContainer)`
  background-color: ${Colors.IB_COLOR_27};
  max-width: none;
`

const SpinnerContainer = styled.div`
  position: relative;
  height: 100%;
  width: 100%;
`

const ErrorContainer = styled.div`
  margin: auto;
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 290px);
  padding-bottom: 60px;
  justify-content: center;
  align-items: center;
`

const BestallningContainer = ({ error, bestallning, history, fetching }) => {
  const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object

  if (fetching) {
    return (
      <SpinnerContainer>
        <LoadingSpinner loading={fetching} message={'Laddar beställning'} />
      </SpinnerContainer>
    )
  }

  if (error && !error.modal) {
    return (
      <FlexColumnContainer>
        <BestallningHeader  bestallning={bestallning} history={history} error={error} />
        <ErrorContainer>
          <ErrorPageIcon />
          <br />
          <ErrorMessageFormatter error={error} />
        </ErrorContainer>
      </FlexColumnContainer>
    )
  }

  return bestallningIsEmpty ? null : (
    <FlexColumnContainer>
      <BestallningHeader bestallning={bestallning} history={history} error={error} />
      <CustomScrollingContainer>
        <WorkareaContainer>
          {bestallning.fragor.map((b, i) => (
            <BestallningFraga key={i} props={b} />
          ))}
        </WorkareaContainer>
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
