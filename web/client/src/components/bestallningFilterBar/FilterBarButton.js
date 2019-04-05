import React from 'react'
import { NavLink } from 'react-router-dom'
import styled from 'styled-components/macro'
import ibColors from '../styles/IbColors'
import { NavItem, Badge } from 'reactstrap'

const Wrapper = styled.span`
  & .badge__stat {
    margin-left: 10px;
    background-color: ${ibColors.IB_COLOR_20};
    border-color: ${ibColors.IB_COLOR_20};
    color: ${ibColors.IB_COLOR_19};
    font-size: 12px;
  }

  & a {
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

  & a:hover,
  & a:hover,
  & a:hover:focus {
    background-color: ${ibColors.IB_COLOR_19};
    border-color: ${ibColors.IB_COLOR_21};
    color: ${ibColors.IB_COLOR_00};
    text-decoration: none;
  }

  & a.active,
  & a.active:hover,
  & a.active:focus {
    background-color: ${ibColors.IB_COLOR_21};
    color: ${ibColors.IB_COLOR_00};
    text-decoration: none;
  }
`

const FilterBarButton = ({ menuItem }) => {
  return (
    <Wrapper>
      <NavItem key={menuItem.text}>
        <NavLink to={menuItem.to}>
          {menuItem.text}
          {
            menuItem.stat &&
            <Badge color="primary" className="badge__stat">
              {menuItem.stat}
            </Badge>
          }
        </NavLink>
      </NavItem>
    </Wrapper>
  )
}

export default FilterBarButton
