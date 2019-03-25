import React from 'react'
import { withRouter } from 'react-router-dom'
import { connect } from 'react-redux'
import * as actions from '../../store/actions/bestallning'
import { getBestallning, getErrorMessage } from '../../store/reducers/bestallning'
import BestallningFraga from './bestallningFraga'
import BestallningHeader from './bestallningHeader'
import { FlexColumnContainer, ScrollingContainer, WorkareaContainer } from '../styles/ibLayout'
import Footer from '../Footer/Footer'
import styled from 'styled-components'
import Colors from '../styles/IbColors'
import { compose, lifecycle } from 'recompose'

const CustomScrollingContainer = styled(ScrollingContainer)`
  background-color:${Colors.IB_COLOR_27};
  max-width: none;
`

const BestallningContainer = ({ errorMessage, bestallning, history }) => {
    const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object;

    if (errorMessage) {
      console.log(errorMessage);
    }

    return (bestallningIsEmpty ? null :
      <FlexColumnContainer>
        <BestallningHeader props={{ bestallning, history }}></BestallningHeader>
        <CustomScrollingContainer>
          <WorkareaContainer>
            {bestallning.struktur.map((b, i) => <BestallningFraga key={i} props={b} />)}
          </WorkareaContainer>
          <Footer />
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
  }
}

const mapStateToProps = (state, { match, history }) => {
  const id = match.params.id;
  return {
    id,
    history,
    bestallning: getBestallning(state),
    errorMessage: getErrorMessage(state),
  };
};

export default compose(
  withRouter,
  connect(mapStateToProps, actions),
  lifecycle(lifeCycleValues)
)(BestallningContainer);
