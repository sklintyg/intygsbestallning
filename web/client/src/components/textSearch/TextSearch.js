import React, { Fragment } from 'react'
import { debounce } from 'lodash'
import PropTypes from 'prop-types'

const TextSearch = ({ onChange, placeholder }) => {
  const debounceHandleChange = debounce(value => {
    onChange(value)
  }, 1000)

  const handleChange = e => {
    debounceHandleChange(e.target.value) // perform a search only once every 1000ms
  }

  return (
    <Fragment>
      <label htmlFor="textFilter">Fritextfilter</label>
      <div>
        <input id="textFilter" type="text" placeholder={placeholder} onChange={handleChange} />
      </div>
    </Fragment>
  )
}

TextSearch.propTypes = {
  onChange: PropTypes.func.isRequired
}

export default TextSearch
