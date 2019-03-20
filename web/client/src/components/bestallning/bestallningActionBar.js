import React, {Component} from 'react';
import * as actions from '../../store/actions/bestallning';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

class BestallningActionBar extends Component {
  accept = () => {
    const { setStatus, bestallning } = this.props;
    setStatus(bestallning.id, 'ACCEPTED');
  }
  
  reject = () => {
    const { setStatus, bestallning } = this.props;
    setStatus(bestallning.id, 'REJECTED');
  }
  
  complete = () => {
    const { setStatus, bestallning } = this.props;
    setStatus(bestallning.id, 'COMPLETED');
  }

  render() {
    const { bestallning } = this.props;
    return (
      <div>
        { bestallning.status === 'LAST' ? <button onClick={ this.accept }>Acceptera</button> : null }
        { bestallning.status === 'LAST' ? <button onClick={ this.reject }>Avvisa</button> : null }
        { bestallning.status === 'ACCEPTERAD' ? <button onClick={ this.complete }>Klarmakera</button> : null }
        <button>Vidarebefodra</button>
        <button>Skriv ut</button>
      </div>
    )
  }
};

const mapStateToProps = (state, { props }) => {
  return {
    bestallning: props
  };
};

BestallningActionBar = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningActionBar));


export default BestallningActionBar;