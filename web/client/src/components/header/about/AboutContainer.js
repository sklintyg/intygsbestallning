import { compose } from 'recompose'
import { connect } from 'react-redux'
import * as actions from '../../../store/actions/appConfig'
import { getErrorMessage, getIsFetching, getSettings } from '../../../store/reducers/appConfig'
import AboutUI from './About'

const mapStateToProps = (state) => {
  return {
    settings: getSettings(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    actions
  )
)(AboutUI)
