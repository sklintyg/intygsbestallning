import React from 'react';
import { shallow, mount } from 'enzyme';
import sinon from 'sinon';

import {Button} from "reactstrap";
import Vardgivare from './Vardgivare';
import Toggler from '../toggler/Toggler';

describe('<Vargivare />', () => {
  const vg = {
    name: 'vg',
    vardenheter: [{
      id: '123',
      name: 've'
    }]
  };

  it('initiallyExpanded false', () => {
    const wrapper = shallow(<Vardgivare initiallyExpanded={false} vg={vg} handleSelect={() => {}} />);
    expect(wrapper.find(Button)).toHaveLength(0);
  });

  it('initiallyExpanded true', () => {
    const wrapper = shallow(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={() => {}} />);
    expect(wrapper.find(Button)).toHaveLength(1);
  });

  it('Toggle expanded', () => {
    const wrapper = mount(<Vardgivare initiallyExpanded={false} vg={vg} handleSelect={() => {}} />);

    expect(wrapper.find(Button)).toHaveLength(0);

    wrapper.find(Toggler).simulate('click');

    expect(wrapper.find(Button)).toHaveLength(1);
  });

  it('active Unit true', () => {
    const unitContext = {
      id: '123'
    };

    const wrapper = mount(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={() => {}} unitContext={unitContext} />);

    expect(wrapper.find(Button).text()).toContain('(nuvarande enhet)');
  });

  it('select unit', () => {
    const handleSelect = sinon.spy();

    const wrapper = mount(<Vardgivare initiallyExpanded={true} vg={vg} handleSelect={handleSelect} />);
    wrapper.find(Button).simulate('click');

    expect(handleSelect).toHaveProperty('callCount', 1);
  });
});
