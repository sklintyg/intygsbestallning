import React from 'react';
import Logo from "./logo/Logo";
import Unit from "./unit/Unit";
import Actions from "./actions/Actions";

import "./Header.css"
import User from "./user/User";
import staticUser from "./static-user";


const Header = () => {
  //Mocked user
  const user = staticUser;
  return (
    <div className="header-wrapper">
      <div className="header">
        <Logo/>
        <User username={user.namn} userrole={user.currentRole.desc}/>
        <Unit vg={user.valdVardgivare} ve={user.valdVardenhet}/>
        <Actions/>
      </div>
    </div>
  )
};

export default Header;
