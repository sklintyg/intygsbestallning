import React, {Component} from "react";
import Pagination from "react-js-pagination";

class BestallningarListPagination extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activePage: 15
    };
  }

  handlePageChange = (pageNumber) => {
    console.log(`active page is ${pageNumber}`);
    this.setState({activePage: pageNumber});
  }

  render() {
    return (
      <div>
        <Pagination
          activePage={this.state.activePage}
          itemsCountPerPage={5}
          totalItemsCount={100}
          pageRangeDisplayed={5}
          hideFirstLastPages={true}
          prevPageText='Föregående'
          nextPageText='Nästa'
          itemClass='page-item'
          linkClass='page-link'
          onChange={this.handlePageChange}
        />
      </div>
    );
  }
};

export default BestallningarListPagination;
