import React from 'react';
import { shallow } from 'enzyme';
import sinon from 'sinon';
import TextSearch from './TextSearch';

describe('<TextSearch />', () => {
  let clock;

  beforeEach(() => {
    clock = sinon.useFakeTimers();
  });

  afterEach(() => {
    clock.restore();
  });

  it('Render search field', () => {
    const wrapper = shallow(<TextSearch onChange={() => {}} />);
    expect(wrapper.find('input')).toHaveLength(1);
  });

  it('debounce textchanges', () => {
    const onChange = sinon.spy();
    const wrapper = shallow(<TextSearch onChange={onChange} />);
    const input  = wrapper.find('input')

    input.simulate('change', { target: { value: 'abc'} });
    input.simulate('change', { target: { value: 'def'} });

    expect(onChange).toHaveProperty('callCount', 0);
    clock.tick(1500);
    expect(onChange).toHaveProperty('callCount', 1);
  });
});
