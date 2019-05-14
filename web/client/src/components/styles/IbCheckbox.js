import React, {Fragment} from 'react'
import styled from 'styled-components'
import IbColors from '../styles/IbColors'
import {IbTypo07} from '../styles/IbTypography'

const CheckboxWrapper = styled.div`
  display: inline-block;
  padding: 0 22px 12px 22px;
`

const Label = styled(IbTypo07)`
  display: inline-block;
  position: relative;
  padding: 0 0 0 6px;
  margin-bottom: 0;
  cursor: pointer;

  .square,
  .tickmark {
    position: absolute;
    top: 0;
    left: -20px;
    padding: 0;
    width: 20px;
    height: 20px;
    
  }

  .square {
    content: '';
    border-radius: 3px;
    border: 1px solid ${IbColors.IB_COLOR_23};
    box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.24);
    background-color: ${IbColors.IB_COLOR_00};
  }

  .tickmark {
    display: none;
    border: none;
  }
`

const Input = styled.input`
  opacity: 0.01; //Important: 0.01 is not practically visible, but if we set it to 0, protractor won't find it..
  z-index: 1;

  margin: 0;
  position: absolute;
  top: 0;
  left: -20px;
  width: 20px;
  height: 20px;

  cursor: pointer;

  &:checked + ${Label} .tickmark {
    display: block;
  }

  &:checked + ${Label} .square {
    background-color: ${IbColors.IB_COLOR_21};
    box-shadow: inset 0 2px 4px 0 rgba(0, 0, 0, 0.5);
    border: none;
  }

  &:focus + ${Label} .square {
    outline: thin dotted;
    outline-offset: 0px;
  }

  &:disabled,
  fieldset[disabled] & {
    cursor: not-allowed;

    & + ${Label} {
      cursor: not-allowed;
      color: ${IbColors.IB_COLOR_09};
    }

    & + ${Label} .square {
      background-color: ${IbColors.IB_COLOR_20};
      color: ${IbColors.IB_COLOR_09};
      cursor: not-allowed;
    }

    & + ${Label} .tickmark {
      background-color: ${IbColors.IB_COLOR_09};
    }
  }
`

const CheckboxContainer = styled.div`
  position: relative;
`
const IbCheckbox = ({ selected, onChange, label, value, id }) => {
  return (
    <Fragment>
      <CheckboxWrapper>
        <CheckboxContainer>
          <Input type="checkbox" name={label} id={id ? id : label} value={value} checked={selected === value} onChange={onChange} />
          <Label as="label" htmlFor={id ? id : label}>
            <div className="square" />
            <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_00} width="20px" height="20px" viewBox="0 0 24 24" className="tickmark">
              <path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z" />
            </svg>
            {label}
          </Label>
        </CheckboxContainer>
      </CheckboxWrapper>
    </Fragment>
  )
}

export default IbCheckbox
