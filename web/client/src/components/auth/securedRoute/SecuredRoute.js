import React from 'react';
import { Route, Redirect} from 'react-router-dom';
import { connect } from 'react-redux'

const mapStateToProps = (state) => {
  return {
    authorized: state.user.authorized,
    loading: state.user.loading
  };
};

export const SecuredRouteComponent = (props) => {
  const {component: Component, path, authorized, loading} = props;

  if(!authorized && loading){
    return (
      <div style={{textAlign:'center',marginTop:'1rem'}}>
        Loading...
      </div>)
}

  return (
    <Route path={path} render={() => {
      if (!authorized) {
        return <Redirect to='/login' />;
      }

      return (
        <div>
          <div style={{margin: "18px"}}>
            <Component />
          </div>
        </div>
      )
    }} />
  );
}

export default connect(mapStateToProps)(SecuredRouteComponent);
