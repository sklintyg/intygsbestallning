import React, { Fragment } from 'react'
import Pagination from 'react-js-pagination'
import styled from 'styled-components'

const Wrapper = styled.div`
  padding: 20px 0 10px 0;
`

const BestallningarListPagination = props => {
  if (props.bestallningList.bestallningar.length < 1) {
    return null
  }

  const pageIndex = !props.bestallningList.pageIndex ? 1 : props.bestallningList.pageIndex + 1
  return (
    <Fragment>
      <Wrapper>
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
      </Wrapper>
    </Fragment>
  )
}

export default BestallningarListPagination
