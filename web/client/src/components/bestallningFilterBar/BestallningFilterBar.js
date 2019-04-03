import React from 'react'
import styled from 'styled-components/macro'
import { PageHeaderContainer } from '../styles/ibLayout'
import ibColors from '../styles/IbColors'
import { Navbar, Nav } from 'reactstrap'
import FilterBarButton from './FilterBarButton';

const Wrapper = styled.div`
  background-color: ${ibColors.IB_COLOR_26};

  & .navbar-ib {
    padding: 6px 30px;
  }

  & .navbar-ib li {
    display: inline-block
  }

  & .navbar-nav {
    flex-direction: row;
  }
`

const BestallningFilterBar = () => {
  const menu = [
    {
      to: '/bestallningar/AKTUELLA',
      text: 'Aktiva'
    },
    {
      to: '/bestallningar/KLARA',
      text: 'Klara'
    },
    {
      to: '/bestallningar/AVVISADE',
      text: 'Avvisade'
    }
  ]

  return (
    <Wrapper>
      <PageHeaderContainer>
        <Navbar className="navbar-ib">
          <Nav navbar>
            {menu.map(menuItem => (
              <FilterBarButton key={menuItem.text} menuItem={menuItem} />
            ))}
          </Nav>
        </Navbar>
      </PageHeaderContainer>
    </Wrapper>
  )
}

export default BestallningFilterBar
