import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/bestallning';
import { getBestallning, getErrorMessage } from '../../store/reducers/bestallning';
import BestallninFraga from './bestallningFraga';
import Config from './af00213.v1.config';

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
    const { fetchBestallning, id } = this.props;
    fetchBestallning(id);
  }

  render() {
    const { errorMessage, bestallning, id, history } = this.props;
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
      <div>
        <div>
          <span onClick={history.goBack}>Tillbaka till lista</span>
          <span> :: Förfrågan av Intygstyp '{bestallning.intygName}'</span>
          <span> :: Status {bestallning.status}</span>
          <span> :: Inkom {bestallning.ankomstDatum}</span>
        </div>
        <hr />
        <div>
          <span>
            <div>{id}</div>
            <div>{bestallning.patient.id} - {bestallning.patient.name}</div>
          </span>
          <span><button>Acceptera</button></span>
          <span><button>Avvisa</button></span>
          <span><button>Vidarebefodra</button></span>
          <span><button>Skriv ut</button></span>
        </div>
        {Config(bestallning).map((b, i) => <BestallninFraga key={i} props={b}/>)}
      </div>
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
  };
};

BestallningContainer = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningContainer));

export default BestallningContainer;
