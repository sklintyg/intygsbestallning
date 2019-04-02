import React from 'react'
import { NavLink } from 'react-router-dom'
import styled from 'styled-components/macro'
import { PageHeaderContainer } from '../styles/ibLayout'
import ibColors from '../styles/IbColors'
import { Badge, Navbar, Nav, NavItem } from 'reactstrap'

const Wrapper = styled.div`
  background-color: ${ibColors.IB_COLOR_26};

  & .badge__stat {
    margin-left: 10px;
    background-color: ${ibColors.IB_COLOR_20};
    border-color: ${ibColors.IB_COLOR_20};
    color: ${ibColors.IB_COLOR_19};
    font-size: 12px;
  }

  & .navbar-ib {
    padding: 6px 30px;
  }

  & .navbar-ib li {
    display: inline-block
  }

  & .navbar-nav {
    flex-direction: row;
  }

  & .navbar-nav > li > a {
    display: inline-block;
    color: ${ibColors.IB_COLOR_00};
    line-height: normal;
    background-color: transparent;
    border: 1px solid transparent;
    border-radius: 22.5px;
    padding: 5px 15px;
    margin: 0 26px 0 0;
    text-decoration: none;
  }

  & .navbar-nav a,
  & .navbar-nav a:hover,
  & .navbar-nav a:hover,
  & .navbar-nav a:hover:focus {
    background-color: ${ibColors.IB_COLOR_19};
    border-color: ${ibColors.IB_COLOR_21};
  }

  & .navbar-nav a.active,
  & .navbar-nav a.active:hover,
  & .navbar-nav a.active:focus {
    background-color: ${ibColors.IB_COLOR_21};
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
              <NavItem key={menuItem.text}>
                <NavLink to={menuItem.to}>
                  {menuItem.text}
                  {/*<Badge color="primary" className="badge__stat">
                    {5}
                  </Badge>*/}
                </NavLink>
              </NavItem>
            ))}
          </Nav>
        </Navbar>
      </PageHeaderContainer>
    </Wrapper>
  )
}

export default BestallningFilterBar
