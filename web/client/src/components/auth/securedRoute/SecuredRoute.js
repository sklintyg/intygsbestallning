import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import {connect} from 'react-redux'

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.user.isAuthenticated,
    isLoading: state.user.isLoading,
    hasCurrentUnit: !!state.user.valdVardenhet
  };
};

const SecuredRoute = (props) => {
  const {component: Component, path, isAuthenticated, isLoading, hasCurrentUnit, allowMissingUnit} = props;

  if(!isAuthenticated && isLoading){
    return (
      <div style={{textAlign:'center',marginTop:'1rem'}}>
        Loading...
      </div>)
}

  return (
    <Route path={path} render={(props) => {
      if (!isAuthenticated) {
        return <Redirect to='/' />;
      }
      if (!hasCurrentUnit && !allowMissingUnit) {
        return <Redirect to='/valj-enhet' />;
      }

      return (<Component {...props}/>)
    }} />);
}

export default connect(mapStateToProps)(SecuredRoute);
