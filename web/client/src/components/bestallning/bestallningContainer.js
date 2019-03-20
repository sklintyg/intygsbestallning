import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/bestallning';
import { getBestallning, getErrorMessage, getBestallningLabels } from '../../store/reducers/bestallning';
import BestallningFraga from './bestallningFraga';
import Config from './af00213.v1.config';
import BestallningActionBar from './bestallningActionBar';
import Styled from 'styled-components';

const ScrollingContainer = Styled.div`
  overflow-y: auto;
  background: #eee;
`

const FlexColumnContainer = Styled.div`
  display: flex;
  flex-direction: column;
  height: 100%;
`

class BestallningContainer extends Component {

  componentDidMount() {
    this.fetchData();
  }

  componentDidUpdate(prevProps) {
    if (this.props.id !== prevProps.id) {
      this.fetchData();
    }
  }

  fetchData() {
    const { fetchBestallning, id, fetchBestallningLabels} = this.props;
    fetchBestallning(id).then(b => {
      //Hämta version och intygstyp från beställning
      fetchBestallningLabels('af20013', '1.0');
    });
  }

  render() {
    const { errorMessage, bestallning, history, labels } = this.props;
    const bestallningIsEmpty = Object.entries(bestallning).length === 0 && bestallning.constructor === Object;

    if (bestallningIsEmpty) {
      return <p>Loading...</p>;
    }
    if (errorMessage && bestallningIsEmpty) {
      return (
        <div>
          {errorMessage}
        </div>
      );
    }

    return (
      <FlexColumnContainer>
        <div>
          <span onClick={history.goBack}>Tillbaka till lista</span>
          <span> :: Förfrågan av Intygstyp '{bestallning.intygName}'</span>
          <span> :: Status {bestallning.status}</span>
          <span> :: Inkom {bestallning.ankomstDatum}</span>
        </div>
        <hr />
        <div>
          <div>{bestallning.id}</div>
          <div>{bestallning.patient.id} - {bestallning.patient.name}</div>
        </div>
        <BestallningActionBar props={bestallning}/>
        <ScrollingContainer>
          {Config(bestallning, labels.texter).map((b, i) => <BestallningFraga key={i} props={b}/>)}
          <div>
            footer...
          </div>
        </ScrollingContainer>
      </FlexColumnContainer>
    )
  }
};

const mapStateToProps = (state, { match, history }) => {
  const id = match.params.id;
  return {
    id,
    history,
    bestallning: getBestallning(state),
    errorMessage: getErrorMessage(state),
    labels: getBestallningLabels(state),
  };
};

BestallningContainer = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningContainer));

export default BestallningContainer;
