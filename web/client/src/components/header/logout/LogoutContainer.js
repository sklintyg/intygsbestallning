import {compose} from "recompose";
import {connect} from "react-redux";
import Logout from "./Logout";
import {logOut} from "../../../store/actions/user";

// expose selected dispatchable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    handleLogout: () => dispatch(logOut())
  }
};
export default compose(
  connect(null, mapDispatchToProps)
)(Logout);
