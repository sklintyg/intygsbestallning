import React from 'react'
import { NavLink } from 'react-router-dom'

// Links to easily navigate between sections in dev mode
const TestLinks = () => (
  <nav>
    <a href="/welcome.html">welcome</a> |{' '}
    <NavLink exact to="/">
      start
    </NavLink>{' '}
    | <NavLink to="/valj-enhet">valj-enhet</NavLink> | <NavLink to="/bestallningar">bestallningar</NavLink>
  </nav>
)

export default TestLinks
