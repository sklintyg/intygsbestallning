import React, { useState, Fragment } from "react";
import * as actions from "../../store/actions/bestallningList";
import BestallningFilter from "../textSearch/TextSearch";
import { fetchBestallningList } from "./../../store/actions/bestallningList";
import BestallningListContainer from "./BestallningListContainer";
import { compose } from "recompose";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import BestallningListPagination from "./BestallningListPagination";

const FilterListContainer = (props) => {
  const [textFilter, setTextFilter] = useState("");

  const handleFilterChange = (textFilter) => {
    const { categoryFilter } = props;
    setTextFilter(textFilter);
    fetchBestallningList(categoryFilter, textFilter);
  };
  return (
    <Fragment>
      <BestallningFilter onChange={handleFilterChange} />
      <BestallningListContainer textFilter={textFilter} />
      <BestallningListPagination />
    </Fragment>
  );
};

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || "active";
  return { categoryFilter };
};

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  )
)(FilterListContainer);
