import {compose} from "recompose";
import {connect} from "react-redux";
import ChangeEnhet from "./ChangeEnhet";
import { openModal, closeModal } from "../../../../store/actions/modal";

const mapStateToProps = (state, ownProps) => {
  return {
    isOpen: !!state.modal['enhetModal']
  }
};
// expose selected dispatchable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    handleOpen: () => dispatch(openModal('enhetModal')),
    handleClose: () => dispatch(closeModal('enhetModal'))
  }
};
export default compose(
  connect(mapStateToProps, mapDispatchToProps)
)(ChangeEnhet);
