import React from 'react';
import { shallow, mount } from 'enzyme';
import sinon from 'sinon';
import About from './About';
import { Button, ModalBody } from 'reactstrap';
import { ActionButton } from '../styles';

describe('<About />', () => {
  let handleOpen;
  let handleClose;

  const settings = {
    versionInfo: {
      buildVersion: 'VERSION'
    }
  }

  beforeEach(() => {
    handleOpen = sinon.spy();
    handleClose = sinon.spy();
  })

  it('default closed', () => {
    const wrapper = mount(<About handleOpen={handleOpen} handleClose={handleClose} isOpen={false} settings={settings} />);
    expect(wrapper.find(ActionButton)).toHaveLength(1);
    expect(wrapper.find(ModalBody)).toHaveLength(0);
  });

  it('default open', () => {
    const wrapper = mount(<About handleOpen={handleOpen} handleClose={handleClose} isOpen={true} settings={settings} />);
    expect(wrapper.find(ActionButton)).toHaveLength(1);
    expect(wrapper.find(ModalBody)).toHaveLength(1);
  });

  it('onClick', () => {
    const wrapper = shallow(<About handleOpen={handleOpen} handleClose={handleClose} isOpen={false} settings={settings} />);
    wrapper.find(ActionButton).simulate('click');

    expect(handleOpen).toHaveProperty('callCount', 1);
  });

  it('onClose', () => {
    const wrapper = shallow(<About handleOpen={handleOpen} handleClose={handleClose} isOpen={true} settings={settings} />);
    wrapper.find(Button).simulate('click');

    expect(handleClose).toHaveProperty('callCount', 1);
  });
});
