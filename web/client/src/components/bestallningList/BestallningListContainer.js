import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import * as actions from "../../store/actions/bestallningList";
import {
  getVisibleBestallningList,
  getErrorMessage,
  getIsFetching} from "../../store/reducers/bestallningList";
import BestallningList from "./BestallningList";
import FetchError from "./FetchError";

class BestallningarListContainer extends Component {

  componentDidMount() {
    this.fetchData();
  }

  componentDidUpdate(prevProps) {
    if (this.props.categoryFilter !== prevProps.categoryFilter
      || this.props.textFilter !== prevProps.textFilter) {
      this.fetchData();
    }
  }

  fetchData() {
    const { fetchBestallningList, categoryFilter, textFilter} = this.props;
    fetchBestallningList(categoryFilter, textFilter);
  }

  render() {
    const { isFetching, errorMessage, bestallningList, textFilter } = this.props;
    if (isFetching && !bestallningList.length) {
      return <p>Loading...</p>;
    }
    if (errorMessage && !bestallningList.length) {
      return (
        <FetchError message={errorMessage} onRetry={() => this.fetchData()} />
      );
    }

    return (
      <div>
        <BestallningList
          bestallningList={bestallningList}
          filter={this.props.categoryFilter}
        />
      </div>
    );
  }
}

BestallningarListContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningList: PropTypes.object,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func
};

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || "active";
  return {
    bestallningList: getVisibleBestallningList(state, categoryFilter),
    categoryFilter,
    isFetching: getIsFetching(state, categoryFilter),
    errorMessage: getErrorMessage(state, categoryFilter),
  };
};

BestallningarListContainer = withRouter(
  connect(
    mapStateToProps,
    actions
  )(BestallningarListContainer)
);

export default BestallningarListContainer;
