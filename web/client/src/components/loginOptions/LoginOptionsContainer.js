import {compose} from "recompose";
import {connect} from "react-redux";
import LoginOptions from "./LoginOptions";

const mapStateToProps = (state, ownProps) => {
  return {
    user: state.user
  }
};
// expose selected dispatchable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    selectLoginType: (loginType) => console.log(loginType)
  }
};
export default compose(
  connect(mapStateToProps, mapDispatchToProps)
)(LoginOptions);

