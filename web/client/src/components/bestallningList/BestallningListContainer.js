import React, { Fragment } from "react";
import PropTypes from "prop-types";
import { compose, lifecycle } from "recompose";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import * as actions from "../../store/actions/bestallningList";
import {
  getVisibleBestallningList,
  getErrorMessage,
  getIsFetching
} from "../../store/reducers/bestallningList";
import BestallningList from "./BestallningList";
import FetchError from "./FetchError";

const BestallningarListContainer = props => {
  const { isFetching, errorMessage, bestallningList } = props;
  if (isFetching && !bestallningList.length) {
    return <p>Loading...</p>;
  }
  if (errorMessage && !bestallningList.length) {
    return (
      <FetchError message={errorMessage} onRetry={() => fetchData(props)} />
    );
  }

  const handleSort = newSortColumn => {
    let {sortColumn, sortDirection} = bestallningList
    if(sortColumn === newSortColumn){
      sortDirection = (bestallningList.sortDirection === 'DESC') ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({...props, sortColumn, sortDirection})
  };

  return (
    <Fragment>
      <BestallningList bestallningList={bestallningList} onSort={handleSort} />
    </Fragment>
  );
};

BestallningarListContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningList: PropTypes.object,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func
};

const fetchData = ({ fetchBestallningList, categoryFilter, textFilter, sortColumn, sortDirection }) => {
  fetchBestallningList({categoryFilter, textFilter, sortColumn, sortDirection});
};

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props);
  },
  componentDidUpdate(prevProps) {
    if (
      this.props.categoryFilter !== prevProps.categoryFilter ||
      this.props.textFilter !== prevProps.textFilter
    ) {
      fetchData(this.props);
    }
  }
};

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || "AKTUELLA";
  return {
    bestallningList: getVisibleBestallningList(state, categoryFilter),
    categoryFilter,
    isFetching: getIsFetching(state, categoryFilter),
    errorMessage: getErrorMessage(state, categoryFilter)
  };
};

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  ),
  lifecycle(lifeCycleValues)
)(BestallningarListContainer);
