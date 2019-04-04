import React, { Fragment } from 'react'
import Pagination from 'react-js-pagination'
import styled from 'styled-components'
import ibColors from '../styles/IbColors'

const Wrapper = styled.div`
  padding: 20px 0 10px 0;

  & .page-item .page-link {
    color: ${ibColors.IB_COLOR_07}
  }

  & .page-item .page-link:hover {
    background-color: transparent;
    color: ${ibColors.IB_COLOR_21}
  }

  & .page-item.active .page-link:hover {
    color: ${ibColors.IB_COLOR_21}
    background-color: transparent;
  }

  & .page-item.active .page-link {
    background-color: transparent;
    color: ${ibColors.IB_COLOR_21}
  }

  & .page-item.disabled .page-link {
    color: ${ibColors.IB_COLOR_22}
  }
  `

const BestallningarListPagination = props => {
  if (!props.bestallningList.bestallningar || props.bestallningList.bestallningar.length < 1) {
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
