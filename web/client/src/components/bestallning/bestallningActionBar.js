import React, {Component} from 'react';
import * as actions from '../../store/actions/bestallning';
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';

class BestallningActionBar extends Component {
  accept = () => {
    this.props.setStatus(this.props.bestallning.id, 'ACCEPTED');
  }
  
  reject = () => {
    this.props.setStatus(this.props.bestallning.id, 'REJECTED');
  }
  
  complete = () => {
    this.props.setStatus(this.props.bestallning.id, 'COMPLETED');
  }

  render() {
    console.log(this.props);
    return (
      <div>
        {this.props.bestallning.status === 'LAST' ? <button onClick={this.accept}>Acceptera</button> : ''}
        {this.props.bestallning.status === 'LAST' ? <button onClick={this.reject}>Avvisa</button> : ''}
        {this.props.bestallning.status === 'ACCEPTERAD' ? <button onClick={this.complete}>Klarmakera</button> : ''}
        <button>Vidarebefodra</button>
        <button>Skriv ut</button>
      </div>
    )
  }
};

const mapStateToProps = (state, props) => {
  return {
    bestallning: props.props
  };
};

BestallningActionBar = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningActionBar));


export default BestallningActionBar;