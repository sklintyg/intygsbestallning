import React from "react";
import { NavLink } from "react-router-dom";

const Navbar = () => {
  return (
    <div>
      <NavLink to={`/bestallningar/active`}>Aktiva</NavLink>
      <NavLink to={`/bestallningar/completed`}>Klara</NavLink>
      <NavLink to={`/bestallningar/rejected`}>Avvisade</NavLink>
    </div>
  );
};

export default Navbar;
