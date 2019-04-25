import { compose, lifecycle } from 'recompose'
import { connect } from 'react-redux'
import * as actions from '../../../store/actions/versionInfo'
import { getVersionInfo, getErrorMessage, getIsFetching } from '../../../store/reducers/versionInfo'
import AboutUI from './About'

const lifeCycleValues = {
  componentDidMount() {
    this.props.fetchVersionInfo()
  },
}

const mapStateToProps = (state) => {
  return {
    versionInfo: getVersionInfo(state),
    isFetching: getIsFetching(state),
    errorMessage: getErrorMessage(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    actions
  ),
  lifecycle(lifeCycleValues)
)(AboutUI)
