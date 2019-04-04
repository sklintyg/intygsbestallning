import React from 'react'
import { Redirect, withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/bestallning'
import { getBestallning, getErrorMessage } from '../../store/reducers/bestallning'
import BestallningFraga from './bestallningFraga'
import BestallningHeader from './bestallningHeader'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../styles/ibLayout'
import AppFooter from '../appFooter/AppFooter'
import styled from 'styled-components'
import Colors from '../styles/IbColors'
import { compose, lifecycle } from 'recompose'

const CustomScrollingContainer = styled(ScrollingContainer)`
  background-color:${Colors.IB_COLOR_27};
  max-width: none;
`

const BestallningContainer = ({ error, bestallning, history, displayErrorModal }) => {
    const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object;

    if (error && bestallningIsEmpty) {
      return <Redirect to={'/exit/' + error.error.errorCode} />
    }

    return (bestallningIsEmpty ? null :
      <FlexColumnContainer>
        <BestallningHeader props={{ bestallning, history }}></BestallningHeader>
        <CustomScrollingContainer>
          <WorkareaContainer>
            {bestallning.fragor.map((b, i) => <BestallningFraga key={i} props={b} />)}
          </WorkareaContainer>
          <AppFooter />
        </CustomScrollingContainer>
      </FlexColumnContainer>
    )
}

const fetchData = ({ fetchBestallning, id }) => {
  fetchBestallning(id);
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props);
  },
  componentDidUpdate(prevProps) {
    if (this.props.id !== prevProps.id) {
      fetchData(this.props);
    }
    if (this.props.error && this.props.error.modal) {
      this.props.displayErrorModal(this.props.error.modal);
    }
  }
}

const mapStateToProps = (state, { match, history }) => {
  const id = match.params.id;
  return {
    id,
    history,
    bestallning: getBestallning(state),
    error: getErrorMessage(state),
  };
};

export default compose(
  withRouter,
  connect(mapStateToProps, actions),
  lifecycle(lifeCycleValues)
)(BestallningContainer);
