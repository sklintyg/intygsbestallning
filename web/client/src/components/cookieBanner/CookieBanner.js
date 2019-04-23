import React, { Fragment, useState, useEffect } from 'react'
import IbColors from '../styles/IbColors'
import styled from 'styled-components'
import { Button } from 'reactstrap'
import CookieModal, {CookieModalId} from '../cookieModal/CookieModal'
import * as modalActions from '../../store/actions/modal'
import { connect } from 'react-redux'
import { compose } from 'recompose'

const CookieBannerWrapper = styled.div`
  display: flex;
  flex-direction: row;
  margin: 0;
  top: 0;
  position: fixed;
  left: 0;
  right: 0;
  z-index: 1020;
  padding: 10px 20px;
  color: ${IbColors.IB_COLOR_07};
  background: ${IbColors.IB_COLOR_03};

  div {
    flex: 1 0 auto;
  }

  & .cookieButton {
    flex: 0 1 auto;
    align-self: center;
  }
`

const CookieBanner = ({openModal}) => {
  let storedSetting = localStorage.getItem('COOKIE_BANNER_ACCEPTED')
  if(storedSetting === null){
    storedSetting = true
  } else {
    storedSetting = storedSetting === 'true'
  }
  const [showBanner, setShowBanner] = useState(storedSetting)

  const onBannerButtonClick = () => {
    setShowBanner(false)
  }

  const openCookieModal = () => openModal(CookieModalId)

  useEffect(() => {localStorage.setItem('COOKIE_BANNER_ACCEPTED', showBanner)}, [showBanner])

  if (!showBanner) {
    return null
  }
  return (
    <Fragment>
      <CookieBannerWrapper>
        <div>
          Vi använder kakor (cookies) för att den här webbplatsen ska fungera på ett bra sätt för dig. Genom att logga in accepterar du vår
          användning av kakor. <Button color="link" onClick={openCookieModal}>
            Läs mer om kakor
          </Button>
        </div>
        <div className="cookieButton">
          <Button onClick={() => onBannerButtonClick()}>Jag godkänner</Button>
        </div>
      </CookieBannerWrapper>
      <CookieModal accept={() => onBannerButtonClick()}></CookieModal>
    </Fragment>
  )
}

export default compose(
  connect(
    null,
    {...modalActions }
  )
)(CookieBanner)
