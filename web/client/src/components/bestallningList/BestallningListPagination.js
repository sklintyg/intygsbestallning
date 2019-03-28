import React from "react";
import Pagination from "react-js-pagination";

const BestallningarListPagination = props => {

  if(props.bestallningList.bestallningar.length < 1) {
    return null
  }

  const pageIndex = !props.bestallningList.pageIndex ? 1 : props.bestallningList.pageIndex + 1
  return (
    <div>
      <Pagination
        activePage={pageIndex}
        itemsCountPerPage={props.bestallningList.limit}
        totalItemsCount={props.bestallningList.totalElements}
        pageRangeDisplayed={10}
        hideFirstLastPages={true}
        prevPageText="Föregående"
        nextPageText="Nästa"
        itemClass="page-item"
        linkClass="page-link"
        onChange={props.handlePageChange}
      />
    </div>
  );
};

export default BestallningarListPagination;
