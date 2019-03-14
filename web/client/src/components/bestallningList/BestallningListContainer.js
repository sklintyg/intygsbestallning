import React, {Component} from 'react';
import PropTypes from 'prop-types'
import { connect } from 'react-redux';
import { withRouter } from 'react-router-dom';
import * as actions from '../../store/actions/bestallningar';
import { getVisibleBestallningList, getErrorMessage, getIsFetching } from '../../store/reducers/bestallningList';
import BestallningList from './BestallningList';
import FetchError from './FetchError';

class BestallningarListContainer extends Component {

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

    const { isFetching, errorMessage, bestallningList } = this.props;
    if (isFetching && !bestallningList.length) {
      return <p>Loading...</p>;
    }
    if (errorMessage && !bestallningList.length) {
      return (
        <FetchError
          message={errorMessage}
          onRetry={() => this.fetchData()}
        />
      );
    }

    return (
      <BestallningList bestallningList={bestallningList} filter={this.props.filter}></BestallningList>
    )
  }
};

BestallningarListContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningList: PropTypes.object,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func,
};

const mapStateToProps = (state, { match }) => {
  console.log(state, match.params.filter);
  const filter = match.params.filter || 'active';
  return {
    isFetching: getIsFetching(state, filter),
    errorMessage: getErrorMessage(state, filter),
    bestallningList: getVisibleBestallningList(state, filter),
    filter,
  };
};

BestallningarListContainer = withRouter(connect(
  mapStateToProps,
  actions,
)(BestallningarListContainer));

export default BestallningarListContainer;
