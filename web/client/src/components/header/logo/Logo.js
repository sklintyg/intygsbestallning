import React from 'react';
import styled from "styled-components";
import {HeaderSectionContainer} from "../styles";
import IbColors from "../../styles/IbColors";

const ComponentWrapper = styled(HeaderSectionContainer)`
  
  flex: 1;
  height: 100%;
  min-width: 200px;
  max-width: 200px;
  background-color: ${IbColors.IB_COLOR_19}
  justify-content: center;

  border-image: linear-gradient(to left, rgba(0, 0, 0, 0.1), rgba(42, 48, 78, 0.1)) 1 100%;
  color: ${IbColors.IB_COLOR_20};

`
const SvgLogo = styled.div`
  width: 180px;
  height: 26px;
  background: no-repeat center url(data:image/svg+xml;base64,PHN2ZyBpZD0iTGF5ZXJfMSIgZGF0YS1uYW1lPSJMYXllciAxIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAyMjEuNDMgMjkuNTYiPjxkZWZzPjxzdHlsZT4uY2xzLTF7ZmlsbDojZmZmO308L3N0eWxlPjwvZGVmcz48dGl0bGU+Zm11bG9nbzwvdGl0bGU+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMzcuMDgsMjEuOTJWNC44MWgzLjY0VjIxLjkyWiIvPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTUxLDIxLjkyVjE0LjE5YzAtMS4wNy0uMDctMi4zNC0xLjU4LTIuMzRBMy43NSwzLjc1LDAsMCwwLDQ3LDEzdjguOUg0My4zOXYtMTNoMi4zNWwuNDksMS44M2E1LjQzLDUuNDMsMCwwLDEsNC4xMy0yLjIxYzIuODIsMCw0LjIzLDEuODQsNC4yMyw1LjE1djguMjZaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNNjQuNDEsMjEuNTJhOS4yNCw5LjI0LDAsMCwxLTMuMjQuNzNjLTEuMjIsMC0zLjQ1LS42MS0zLjQ1LTQuMTFWMTJINTUuODZWOS4zNGwxLjg4LS40Ny4zNS0yLjY4LDMuMjItLjg5VjguODlINjRWMTJoLTIuN3Y2LjE1YzAsLjg0LjQ3LDEsLjk0LDFhNC42OCw0LjY4LDAsMCwwLDEuNDUtLjI2WiIvPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTcxLjQ5LDI2LjU5SDY3Ljk0TDcwLDIxLjkyaC0uNjZsLTQuNDEtMTNoMy43NUw3MC41MiwxNWEyNSwyNSwwLDAsMSwuNzMsMy41NkEyNy43LDI3LjcsMCwwLDEsNzIsMTVsMS43OC02LjFoMy43MVoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik03OS41LDIzLjMzQTEyLjYyLDEyLjYyLDAsMCwwLDgzLDI0YzEuNDgsMCwzLS4zNSwzLTIuNzJ2LTFhNCw0LDAsMCwxLTMuMjIsMS40OGMtMywwLTUtMi41Ni01LTYuNDNTNzkuNjQsOC41MSw4Myw4LjUxYTQuMjIsNC4yMiwwLDAsMSwzLjY5LDIuMDVsLjQ0LTEuNjdoMi4zNVYyMS4yNmMwLDQuNi0zLjM2LDUuNjYtNS43Nyw1LjY2YTE0LjEsMTQuMSwwLDAsMS01LTFaTTg2LDEzLjA3YTIuNzQsMi43NCwwLDAsMC0yLjI2LTEuMzJjLTEuMjksMC0yLjMuNzEtMi4zLDMuNXMxLjEzLDMuNDgsMi4yNiwzLjQ4QTIuOTEsMi45MSwwLDAsMCw4NiwxNy40NloiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik05Mi4xNywxOC42OGEyMCwyMCwwLDAsMCw0LjM3LjY2YzEuMzEsMCwyLS4zMywyLTEuMTFzLS41LTEtMi45MS0xLjQ4LTQuMDktMS41LTQuMDktNC4xM2MwLTIuNzksMi4zLTQuMTEsNS4xOS00LjExYTE0LjQ0LDE0LjQ0LDAsMCwxLDUsLjhMMTAxLDExLjkyYTE5Ljc5LDE5Ljc5LDAsMCwwLTMuOC0uNTJjLTEuNSwwLTIuMTEuMzMtMi4xMSwxLjA2cy42OCwxLDMsMS4zOCw0LDEuNDQsNCwzLjkyYzAsMy0yLjA5LDQuNTMtNS4zNyw0LjUzYTE2Ljg0LDE2Ljg0LDAsMCwxLTUuMjgtMVoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xMDQsMjEuOTJWNGgzLjU3djYuMjRhNC40NCw0LjQ0LDAsMCwxLDMuNTktMS43NmMzLjE3LDAsNC41MywyLjg3LDQuNTMsNi43MiwwLDMuNjQtMS43Niw3LjA2LTUuMjYsNy4wNmE0LjM1LDQuMzUsMCwwLDEtMy41NC0ybC0uNTcsMS42Wm0zLjU3LTQuMjVhMi44LDIuOCwwLDAsMCwyLjE4LDEuNDFjMS4yLDAsMi4zLTEsMi4zLTMuNTIsMC0yLjE0LS4zMy0zLjgzLTIuMTgtMy44My0xLjQ2LDAtMi4xOCwxLjUtMi4zLDEuNjRaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTI4LjY2LDIxLjMxYTE0LjI3LDE0LjI3LDAsMCwxLTUuMTksMWMtNC41MywwLTYuMjktMy4wNy02LjI5LTYuOXMyLTYuODgsNi4xNy02Ljg4YzMuODcsMCw1LjU2LDIuNTksNS41Niw2LjQ2LDAsLjYxLDAsMS4xNywwLDEuMzhoLTguMDhjLjE0LDIuMTksMS4yMiwyLjk0LDMuMTUsMi45NGEyMiwyMiwwLDAsMCw0LjExLS41NFptLTMuMTUtNy4xNmEyLjI5LDIuMjksMCwwLDAtMi4yOC0yLjU2LDIuNDgsMi40OCwwLDAsMC0yLjQ2LDIuNjhaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTMxLjA2LDE4LjY4YTIwLjEyLDIwLjEyLDAsMCwwLDQuMzcuNjZjMS4zMSwwLDItLjMzLDItMS4xMXMtLjQ5LTEtMi45MS0xLjQ4LTQuMDktMS41LTQuMDktNC4xM2MwLTIuNzksMi4zLTQuMTEsNS4xOS00LjExYTE0LjQ0LDE0LjQ0LDAsMCwxLDUsLjhsLS42OCwyLjYxYTE5LjY3LDE5LjY3LDAsMCwwLTMuOC0uNTJjLTEuNSwwLTIuMTEuMzMtMi4xMSwxLjA2cy42OCwxLDMsMS4zOCw0LDEuNDQsNCwzLjkyYzAsMy0yLjA5LDQuNTMtNS4zOCw0LjUzYTE2Ljg0LDE2Ljg0LDAsMCwxLTUuMjgtMVoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xNTAuNDMsMjEuNTJhOS4yNCw5LjI0LDAsMCwxLTMuMjQuNzNjLTEuMjIsMC0zLjQ1LS42MS0zLjQ1LTQuMTFWMTJoLTEuODVWOS4zNGwxLjg3LS40Ny4zNi0yLjY4LDMuMjEtLjg5VjguODlIMTUwVjEyaC0yLjd2Ni4xNWMwLC44NC40NywxLC45NCwxYTQuOCw0LjgsMCwwLDAsMS40Ni0uMjZaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTU5LjgxLDIxLjkybC0uNTYtMS43NGE0LjQ4LDQuNDgsMCwwLDEtMy45NSwyQTQsNCwwLDAsMSwxNTEuMDUsMThjMC0zLjE0LDIuODItMy44OSw1LjEyLTMuODloMi40NFYxMy43YzAtMS41OS0uOTQtMi4wOS0yLjMtMi4wOWExNy44OSwxNy44OSwwLDAsMC0zLjU3LjU5TDE1Miw5LjQ4YTE3LjQ2LDE3LjQ2LDAsMCwxLDQuODYtLjkyYzIuMDYsMCw1LjMuNTIsNS4zLDUuNTl2Ny43N1pNMTU0LjYyLDcuNDFjLTEuMzgsMC0xLjgxLS4zNS0xLjgxLTEuNjRzLjQzLTEuNjUsMS44MS0xLjY1LDEuNzkuMzYsMS43OSwxLjY1UzE1Niw3LjQxLDE1NC42Miw3LjQxWm0xLjc2LDguODNjLTEuNDEsMC0xLjkuOC0xLjksMS41OWExLjU4LDEuNTgsMCwwLDAsMS43NiwxLjY3YzEuMjcsMCwyLjM3LS44MiwyLjQtMy4zM1ptMy4xNS04LjgzYy0xLjM5LDAtMS44MS0uMzUtMS44MS0xLjY0cy40Mi0xLjY1LDEuODEtMS42NSwxLjgxLjM2LDEuODEsMS42NVMxNjAuOTEsNy40MSwxNTkuNTMsNy40MVoiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0xNjQuNjMsMjEuOTJWNGgzLjU5VjIxLjkyWiIvPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTE3MC43MywyMS45MlY0aDMuNTlWMjEuOTJaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTg0LjQyLDIxLjkyVjE0LjE5YzAtMS4wNy0uMDctMi4zNC0xLjU3LTIuMzRBMy43NywzLjc3LDAsMCwwLDE4MC4zOCwxM3Y4LjloLTMuNTd2LTEzaDIuMzVsLjQ5LDEuODNhNS40NCw1LjQ0LDAsMCwxLDQuMTQtMi4yMWMyLjgxLDAsNC4yMiwxLjg0LDQuMjIsNS4xNXY4LjI2WiIvPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTE5Mi4yNiw0LjE3YzEuNiwwLDIuMDkuMzgsMi4wOSwxLjcycy0uNDksMS43MS0yLjA5LDEuNzEtMi4xMS0uMzgtMi4xMS0xLjcxUzE5MC42NCw0LjE3LDE5Mi4yNiw0LjE3Wm0tMS43OCwxNy43NXYtMTNoMy41N3YxM1oiLz48cGF0aCBjbGFzcz0iY2xzLTEiIGQ9Ik0yMDQuMiwyMS45MlYxNC4xOWMwLTEuMDctLjA3LTIuMzQtMS41Ny0yLjM0QTMuNzUsMy43NSwwLDAsMCwyMDAuMTcsMTN2OC45SDE5Ni42di0xM0gxOTlsLjQ5LDEuODNhNS40Myw1LjQzLDAsMCwxLDQuMTMtMi4yMWMyLjgyLDAsNC4yMywxLjg0LDQuMjMsNS4xNXY4LjI2WiIvPjxwYXRoIGNsYXNzPSJjbHMtMSIgZD0iTTIxMS40LDIzLjMzYTEyLjc4LDEyLjc4LDAsMCwwLDMuNDguNjNjMS40OCwwLDMtLjM1LDMtMi43MnYtMWE0LDQsMCwwLDEtMy4yMSwxLjQ4Yy0zLDAtNS0yLjU2LTUtNi40M3MxLjg0LTYuNzQsNS4yNC02Ljc0YTQuMTksNC4xOSwwLDAsMSwzLjY4LDIuMDVsLjQ1LTEuNjdoMi4zNVYyMS4yNmMwLDQuNi0zLjM2LDUuNjYtNS43OCw1LjY2YTE0LDE0LDAsMCwxLTUtMVptNi40OC0xMC4yNmEyLjczLDIuNzMsMCwwLDAtMi4yNS0xLjMyYy0xLjI5LDAtMi4zLjcxLTIuMywzLjVzMS4xMywzLjQ4LDIuMjUsMy40OGEyLjg5LDIuODksMCwwLDAsMi4zLTEuMjdaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTcuMjYsMTYuNjRIMTAuNThhLjYyLjYyLDAsMCwwLS42Mi42Mi42MS42MSwwLDAsMCwuNjIuNjJoNi42OGEuNjEuNjEsMCwwLDAsLjYyLS42MkEuNjIuNjIsMCwwLDAsMTcuMjYsMTYuNjRaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTQuOTIsMTkuNTNIMTAuNThhLjYzLjYzLDAsMCwwLDAsMS4yNWg0LjM0YS42My42MywwLDAsMCwwLTEuMjVaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTQuNzgsMEExNC43OCwxNC43OCwwLDEsMCwyOS41NiwxNC43OCwxNC43OCwxNC43OCwwLDAsMCwxNC43OCwwWm01LjUxLDIyLjIyYTEuNTMsMS41MywwLDAsMS0xLjUxLDEuNTJIOS4wNmExLjUzLDEuNTMsMCwwLDEtMS41Mi0xLjUyVjkuMTNBMS41MywxLjUzLDAsMCwxLDkuMDYsNy42MWg2LjJ2LjYyYTQuMzgsNC4zOCwwLDAsMCw0LjM0LDQuNDFjLjI4LDAsLjQ5LS4wNy42OS0uMDdaTTE5LjYsMTEuNjFBMy4zOCwzLjM4LDAsMSwxLDIzLDguMjMsMy4zOCwzLjM4LDAsMCwxLDE5LjYsMTEuNjFaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMTcuMjYsMTMuNzVIMTAuNThhLjYxLjYxLDAsMCwwLS42Mi42Mi42Mi42MiwwLDAsMCwuNjIuNjJoNi42OGEuNjIuNjIsMCwwLDAsLjYyLS42MkEuNjEuNjEsMCwwLDAsMTcuMjYsMTMuNzVaIi8+PHBhdGggY2xhc3M9ImNscy0xIiBkPSJNMjEuMDUsNy43NWgtMXYtMWEuNDguNDgsMCwwLDAtLjQ5LS40OS40Ny40NywwLDAsMC0uNDEuNDl2MWgtMWEuNDkuNDksMCwwLDAsMCwxaDF2MWEuNDYuNDYsMCwwLDAsLjQxLjQ4LjQ3LjQ3LDAsMCwwLC40OS0uNDh2LTFoMWEuNDkuNDksMCwwLDAsMC0xWiIvPjwvc3ZnPg==);
 `
const Logo = ({className}) => {
  return (
    <ComponentWrapper className={className}>
      <SvgLogo/>
    </ComponentWrapper>
  )
};

export default Logo;
