import React, { Fragment } from 'react'
import PropTypes from 'prop-types'
import { compose, lifecycle } from 'recompose'
import { connect } from 'react-redux'
import { withRouter } from 'react-router-dom'
import * as actions from '../../store/actions/bestallningList'
import { getVisibleBestallningList, getErrorMessage, getIsFetching } from '../../store/reducers/bestallningList'
import BestallningList from './BestallningList'
import { Spinner } from 'reactstrap'
import styled from 'styled-components'

const ListWrapper = styled.div`
  position: relative;
`

const SpinnerWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.5);
  position: absolute;
  top: 0;
  width: 100%;
  height: 100%;
`

const SpinnerBox = styled.div`
  position: absolute;
  width: auto;
  text-align: center;
  top: 100px;
  padding: 10px;
  border-radius: 4px;
`

const BestallningarListContainer = props => {
  const { isFetching, errorMessage, bestallningList } = props

  const handleSort = newSortColumn => {
    let { sortColumn, sortDirection } = bestallningList
    if (sortColumn === newSortColumn) {
      sortDirection = bestallningList.sortDirection === 'DESC' ? 'ASC' : 'DESC'
    } else {
      sortColumn = newSortColumn
    }

    fetchData({ ...props, sortColumn, sortDirection })
  }

  return (
    <Fragment>
      <ListWrapper>
        <BestallningList bestallningList={bestallningList} errorMessage={errorMessage} onSort={handleSort} />
        {isFetching && !bestallningList.length && (
          <Fragment>
            <SpinnerWrapper>
              <SpinnerBox>
                <Spinner color="secondary" />
                <div>Laddar beställningar</div>
              </SpinnerBox>
            </SpinnerWrapper>
          </Fragment>
        )}
      </ListWrapper>
    </Fragment>
  )
}

BestallningarListContainer.propTypes = {
  filter: PropTypes.string,
  errorMessage: PropTypes.string,
  bestallningList: PropTypes.object,
  isFetching: PropTypes.bool,
  fetchBestallningar: PropTypes.func
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
  }
}

const mapStateToProps = (state, { match }) => {
  const categoryFilter = match.params.filter || 'AKTUELLA'
  return {
    bestallningList: getVisibleBestallningList(state, categoryFilter),
    categoryFilter,
    isFetching: getIsFetching(state, categoryFilter),
    errorMessage: getErrorMessage(state, categoryFilter)
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
