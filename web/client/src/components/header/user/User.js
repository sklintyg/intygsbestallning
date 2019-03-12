import React from 'react';
import './User.css';
import icon from './user-icon.png';

const User = ({userName, userRole}) => {
  return (
    <div className="ib-header-user header-section-container">
      <img className="header-icon" src={icon} alt="user-logo" />
      <div className="vertical-container">
        <div className="single-text-row-container">
          <span className="truncate-when-needed user-name">{userName}</span>
          <span className="user-role" id="ib-header-user-role">&nbsp;-&nbsp;{userRole}</span>
        </div>
      </div>
    </div>
  )
};


export default User;
