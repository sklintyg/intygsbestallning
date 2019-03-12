import React, {Component} from 'react';
import PropTypes from 'prop-types'
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import * as actions from '../../store/actions/BestallningarActions';
import { getVisibleBestallningar, getErrorMessage, getIsFetching } from '../../store/reducers/bestallningar';
import BestallningarList from './BestallningarList';
import FetchError from '../FetchError';

class BestallningarContainer extends Component {

  componentDidMount() {
    this.fetchData();
  }

  componentDidUpdate(prevProps) {
    if (this.props.filter !== prevProps.filter) {
      this.fetchData();
    }
  }

  fetchData() {
    const { fetchBestallningar, filter} = this.props;
    fetchBestallningar(filter);
  }

  render() {

    const { isFetching, errorMessage, bestallningar } = this.props;
    if (isFetching && !bestallningar.length) {
      return <p>Loading...</p>;
    }
    if (errorMessage && !bestallningar.length) {
      return (
        <FetchError
          message={errorMessage}
          onRetry={() => this.fetchData()}
        />
      );
    }

    return (
      <BestallningarList bestallningar={bestallningar}></BestallningarList>
    )
  }
};

BestallningarContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningar: PropTypes.array,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func,
};

const mapStateToProps = (state, { match }) => {
  console.log(state, match.params.filter);
  const filter = match.params.filter || 'all';
  return {
    isFetching: getIsFetching(state, filter),
    errorMessage: getErrorMessage(state, filter),
    bestallningar: getVisibleBestallningar(state, filter),
    filter,
  };
};

BestallningarContainer = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningarContainer));

export default BestallningarContainer;
