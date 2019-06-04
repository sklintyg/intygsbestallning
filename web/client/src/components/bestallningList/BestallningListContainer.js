import React  from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle } from 'recompose'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'
import * as actions from '../../store/actions/bestallningList'
import { getVisibleBestallningList, getErrorMessage, getIsFetching } from '../../store/reducers/bestallningList'
import BestallningList from './BestallningList'
import styled from 'styled-components'
import LoadingSpinner from '../loadingSpinner'

const ListWrapper = styled.div`
  position: relative;
`

const BestallningarListContainer = (props) => {
  const { isFetching, errorMessage, bestallningList, categoryFilter } = props

  const handleSort = (newSortColumn) => {
    let { sortColumn, sortDirection } = bestallningList
    if (sortColumn === newSortColumn) {
      sortDirection = bestallningList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({ ...props, sortColumn, sortDirection })
  }

  return (
    <ListWrapper>
      <BestallningList bestallningList={bestallningList} categoryFilter={categoryFilter} errorMessage={errorMessage} onSort={handleSort} />
      {isFetching && !bestallningList.length && <LoadingSpinner loading={isFetching} message={'Laddar beställningar'} />}
    </ListWrapper>
  )
}

BestallningarListContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningList: PropTypes.object,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func,
}

const fetchData = ({ fetchBestallningList, categoryFilter, textFilter, sortColumn, sortDirection }) => {
  fetchBestallningList({ categoryFilter, textFilter, sortColumn, sortDirection })
}

const lifeCycleValues = {
  componentDidMount() {
    fetchData(this.props)
  },
  componentDidUpdate(prevProps) {
    if (this.props.categoryFilter !== prevProps.categoryFilter || this.props.textFilter !== prevProps.textFilter) {
      fetchData(this.props)
    }
  },
}

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || 'AKTUELLA'
  return {
    bestallningList: getVisibleBestallningList(state, categoryFilter),
    categoryFilter,
    isFetching: getIsFetching(state, categoryFilter),
    errorMessage: getErrorMessage(state, categoryFilter),
  }
}

export default compose(
  withRouter,
  connect(
    mapStateToProps,
    actions
  ),
  lifecycle(lifeCycleValues)
)(BestallningarListContainer)
