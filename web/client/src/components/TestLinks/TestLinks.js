import React from 'react'
import {NavLink} from 'react-router-dom'
import {compose} from 'recompose'
import {connect} from 'react-redux'

// Links to easily navigate between sections in dev mode
const TestLinks = ({ sessionState }) => (
  <nav>
    <a href="/welcome.html">welcome</a> |{' '}
    <NavLink exact to="/">
      start
    </NavLink>{' '}
    | <NavLink to="/valj-enhet">valj-enhet</NavLink> | <NavLink to="/bestallningar">bestallningar</NavLink>
    <span> session-status: {JSON.stringify(sessionState)}</span>
  </nav>
)
const mapStateToProps = (state) => ({
  sessionState: state.sessionPoll.sessionState,
})

export default compose(
  connect(
    mapStateToProps,
    null
  )
)(TestLinks)
