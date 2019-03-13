import {compose} from "recompose";
import {connect} from "react-redux";
import ValjEnhet from "./ValjEnhet";
import {selectEnhet} from "../../store/actions/UserActions";

const mapStateToProps = (state, ownProps) => {
  return {
    vardgivare: state.user.vardgivare
  }
};
// expose selected dispachable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    selectEnhet: (hsaId) => dispatch(selectEnhet(hsaId))
  }
};
export default compose(
  connect(mapStateToProps, mapDispatchToProps)
)(ValjEnhet);

