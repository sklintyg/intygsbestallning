import React, { Component } from 'react';
import { withRouter } from 'react-router-dom';
import { connect } from 'react-redux';
import * as actions from '../../store/actions/bestallning';
import { getBestallning, getErrorMessage } from '../../store/reducers/bestallning';


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
          <span> :: Förfrågan av Intygstyp ABC</span>
          <span> :: Status Oläst</span>
          <span> :: Inkom 2018-01-01</span>
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
