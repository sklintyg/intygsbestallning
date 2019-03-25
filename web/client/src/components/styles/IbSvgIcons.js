import React from 'react';
import IbColors from "../styles/IbColors";


export const ExpandIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_08} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,15.41L12,10.83L16.59,15.41L18,14L12,8L6,14L7.41,15.41Z" />
  </svg>
)

export const CollapseIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_08} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M7.41,8.58L12,13.17L16.59,8.58L18,10L12,16L6,10L7.41,8.58Z" />
  </svg>
)

export const UnitIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36px" height="36px" viewBox="0 0 24 24">
    <path
      d="M5,3V21H11V17.5H13V21H19V3H5M7,5H9V7H7V5M11,5H13V7H11V5M15,5H17V7H15V5M7,9H9V11H7V9M11,9H13V11H11V9M15,9H17V11H15V9M7,13H9V15H7V13M11,13H13V15H11V13M15,13H17V15H15V13M7,17H9V19H7V17M15,17H17V19H15V17Z"
      shapeRendering="crispEdges" />
  </svg>
)
export const LogoutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path
      d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z" />
  </svg>
)

export const ChangeUnitIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path
      d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zM6.5 9L10 5.5 13.5 9H11v4H9V9H6.5zm11 6L14 18.5 10.5 15H13v-4h2v4h2.5z" />
  </svg>
)

export const AboutIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="24px" height="24px" viewBox="0 0 24 24">
    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z" />
  </svg>
)

export const UserIcon = () => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={IbColors.IB_COLOR_20} width="36px" height="36px" viewBox="0 0 24 24">
    <path
      d="M12,4A4,4 0 0,1 16,8A4,4 0 0,1 12,12A4,4 0 0,1 8,8A4,4 0 0,1 12,4M12,14C16.42,14 20,15.79 20,18V20H4V18C4,15.79 7.58,14 12,14Z" />
  </svg>
)
export const InfoIcon = ({color}) => (
  <svg xmlns="http://www.w3.org/2000/svg" fill={color} width="30px" height="24px" viewBox="0 0 24 24">
    <path
      d="M11,9H13V7H11M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M12,2A10,10 0 0,0 2,12A10,10 0 0,0 12,22A10,10 0 0,0 22,12A10,10 0 0,0 12,2M11,17H13V11H11V17Z" />
  </svg>
)
