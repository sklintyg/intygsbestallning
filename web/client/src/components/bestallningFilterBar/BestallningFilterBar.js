import React from 'react'
import styled from 'styled-components/macro'
import { PageHeaderContainer } from '../styles/ibLayout'
import ibColors from '../styles/IbColors'
import { Navbar, Nav } from 'reactstrap'
import FilterBarButton from './FilterBarButton'
import { compose } from 'recompose'
import { getStat } from '../../store/reducers/stat'
import { connect } from 'react-redux'

const Wrapper = styled.div`
  background-color: ${ibColors.IB_COLOR_26};

  & .navbar-ib {
    padding: 6px 30px;
  }

  & .navbar-ib li {
    display: inline-block;
  }

  & .navbar-nav {
    flex-direction: row;
  }
`

const BestallningFilterBar = (stat) => {
  const menu = [
    {
      to: '/bestallningar/AKTUELLA',
      text: 'Aktiva',
      stat: stat.stat.antalOlastaBestallningar,
    },
    {
      to: '/bestallningar/KLARA',
      text: 'Klara',
    },
    {
      to: '/bestallningar/AVVISADE',
      text: 'Avvisade',
    },
  ]

  return (
    <Wrapper>
      <PageHeaderContainer>
        <Navbar className="navbar-ib">
          <Nav navbar>
            {menu.map((menuItem) => (
              <FilterBarButton key={menuItem.text} menuItem={menuItem} />
            ))}
          </Nav>
        </Navbar>
      </PageHeaderContainer>
    </Wrapper>
  )
}

const mapStateToProps = (state) => {
  return {
    stat: getStat(state),
  }
}

export default compose(
  connect(
    mapStateToProps,
    null
  )
)(BestallningFilterBar)
