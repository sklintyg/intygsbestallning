import React, { Component } from "react";
import * as actions from "../../store/actions/bestallningList";
import BestallningFilter from '../textSearch/TextSearch';
import { fetchBestallningList } from './../../store/actions/bestallningList';
import BestallningListContainer from "./BestallningListContainer";
import { connect } from "react-redux";
import { withRouter } from "react-router-dom";

class FilterListContainer extends Component {

  constructor(props){
    super(props)

    this.state = {
      textFilter: ''
    }
  }

  handleFilterChange = (textFilter) => {
    const {categoryFilter} = this.props;
    this.setState({textFilter})
    fetchBestallningList(categoryFilter, textFilter)
  }

  render() {
    return (
      <div>
        <BestallningFilter onChange={this.handleFilterChange} />
        <BestallningListContainer textFilter={this.state.textFilter} />
      </div>
    );
  }
}

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || "active";
  return {categoryFilter}
};

FilterListContainer = withRouter(
  connect(
    mapStateToProps,
    actions
  )(FilterListContainer)
);

export default FilterListContainer;
