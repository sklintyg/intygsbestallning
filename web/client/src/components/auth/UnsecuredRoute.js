import React from 'react'
import {Redirect, Route} from 'react-router-dom'
import {connect} from 'react-redux'
import LoadingSpinner from '../loadingSpinner'

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.user.isAuthenticated,
    isLoading: state.user.isLoading,
  }
}

const UnsecuredRoute = ({ component: Component, isAuthenticated, isErrorPage, isLoading, ...rest }) => {
  if (isLoading) {
    return <LoadingSpinner loading={isLoading} message={'Laddar sida...'} />
  }

  return (
    <Route
      {...rest}
      render={(props) => {
        if (isAuthenticated && !isErrorPage) {
          return <Redirect to="/bestallningar" />
        }
        return <Component {...props} />
      }}
    />
  )
}

export default connect(mapStateToProps)(UnsecuredRoute)
