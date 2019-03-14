import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import {connect} from 'react-redux'

const mapStateToProps = (state) => {
  return {
    isAuthenticated: state.user.isAuthenticated,
    isLoading: state.user.isLoading
  };
};

const UnsecuredRoute = (props) => {
  const {component: Component, isAuthenticated, isLoading, ...rest} = props;

  if(isLoading){
    return (
      <div style={{textAlign:'center',marginTop:'1rem'}}>
        Loading...
      </div>)
}

  return (
    <Route {...rest} render={(props) => {
      if (isAuthenticated) {
        return <Redirect to='/bestallningar/active' />;
      }
      return (<Component {...props}/>)
    }} />);
}

export default connect(mapStateToProps)(UnsecuredRoute);
