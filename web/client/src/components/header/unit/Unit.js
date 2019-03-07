import React from 'react';
import './Unit.css';
import icon from './unit-icon.png';

const Unit = (props) => {
  return (
    <div className="ib-header-unit header-section-container">

      <img className="header-icon" src={icon} alt="unit-logo" />
      <div className="vertical-container">
        <div className="single-text-row-container">
          <div className="vg-name">{props.vg.namn}</div>
          <div className="truncate-when-needed ve-name">&nbsp;-{props.ve.namn}</div>
        </div>
      </div>
    </div>
  )
};

export default Unit;
