import { compose, lifecycle } from 'recompose'
import * as actions from '../../store/actions/stat'
import { connect } from 'react-redux'

const StatPoll = () => {
  return null
}

const fetch = ({ fetchStat }) => {
  fetchStat()
}

var timeoutId

const lifeCycleValues = {
  componentDidMount() {
    var props = this.props

    fetch(props)

    if (!timeoutId) {
      const delayFetch = () => {
        timeoutId = setTimeout(() => {
          fetch(props)
          delayFetch()
        }, 30000)
      }
      delayFetch()
    }
  },
  componentWillUnmount() {
    clearTimeout(timeoutId)
    timeoutId = null
  },
}

export default compose(
  connect(
    null,
    actions
  ),
  lifecycle(lifeCycleValues)
)(StatPoll)
