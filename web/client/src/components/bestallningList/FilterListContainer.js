import React, { useState, Fragment } from "react";
import * as actions from "../../store/actions/bestallningList";
import BestallningFilter from "../textSearch/TextSearch";
import BestallningListContainer from "./BestallningListContainer";
import { compose } from "recompose";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import BestallningListPagination from "./BestallningListPagination";
import {
  getVisibleBestallningList
} from "../../store/reducers/bestallningList";

const FilterListContainer = (props) => {
  const [textFilter, setTextFilter] = useState("");
  const { categoryFilter, bestallningList } = props;

  const handleFilterChange = (textFilter) => {
    setTextFilter(textFilter);
    fetchList(bestallningList.pageIndex)
  };

  const handlePageChange = (pageNumber) => {
    fetchList(pageNumber)
  }

  const fetchList = (pageIndex) => {
    const pageIndexZeroBased = pageIndex - 1
    props.fetchBestallningList({categoryFilter, textFilter, pageIndex: pageIndexZeroBased});
  }
  
  return (
    <Fragment>
      <BestallningFilter onChange={handleFilterChange} />
      <BestallningListContainer textFilter={textFilter} />
      <BestallningListPagination bestallningList={bestallningList} handlePageChange={handlePageChange}/>
    </Fragment>
  );
};

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || 'AKTUELLA';
  return {
    bestallningList: getVisibleBestallningList(state, categoryFilter),
    categoryFilter
  };
};

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  )
)(FilterListContainer);
