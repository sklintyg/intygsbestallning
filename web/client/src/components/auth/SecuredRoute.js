import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import {connect} from 'react-redux'
import LoadingSpinner from '../loadingSpinner'

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.user.isAuthenticated,
    isLoading: state.user.isLoading,
    hasCurrentUnit: !!state.user.unitContext
  };
};

const SecuredRoute = ({component: Component, isAuthenticated, isLoading, hasCurrentUnit, allowMissingUnit, ...rest}) => {

  if (!isAuthenticated && isLoading){
    return <LoadingSpinner loading={true} message={'Laddar sida...'} />
  }

  return (
    <Route {...rest} render={(props) => {
      if (!isAuthenticated) {
        return <Redirect to='/' />;
      }
      if (!hasCurrentUnit && !allowMissingUnit) {
        return <Redirect to='/valj-enhet' />;
      }

      return (<Component {...props}/>)
    }} />
  );
}

export default connect(mapStateToProps)(SecuredRoute);
