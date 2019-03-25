import {compose} from "recompose";
import {connect} from "react-redux";
import About from "./About";
import {closeModal, openModal} from "../../../store/actions/modal";

const mapStateToProps = (state, ownProps) => {
  return {
    isOpen: !!state.modal['aboutModal']
  }
};
// expose selected dispatchable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    handleOpen: () => dispatch(openModal('aboutModal')),
    handleClose: () => dispatch(closeModal('aboutModal'))
  }
};
export default compose(
  connect(mapStateToProps, mapDispatchToProps)
)(About);
