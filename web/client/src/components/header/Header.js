import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";
import Actions from "./actions/Actions";

import "./Header.css"
import User from "./user/User";


const Header = ({namn}) => {
  return (
    <div className="header-wrapper">
      <div className="header">
        <Logo/>
        <User userName={namn} userRole={''}/>
        { (false) ? <Unit vg={''} ve={''}/>: '' }


        <Actions/>
      </div>
    </div>
  )
};

export default Header;
