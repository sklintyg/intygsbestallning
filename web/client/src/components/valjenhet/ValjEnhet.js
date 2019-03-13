import React from 'react';

const ValjEnhet = ({vardgivare, selectEnhet}) => {
  const handleSelect = (hsaid) => () => selectEnhet(hsaid);
  return (
    <div>
      { vardgivare.map(vg => {
        return (
          <div key={vg.id}>
            <span onClick={handleSelect(vg.id)}>{vg.namn}</span> {
            vg.vardenheter.map(ve => {
              return (
                <div key={ve.id}><span>{ve.namn}</span> {
                  ve.mottagningar.map(m => {
                    return (
                      <div key={m.id}><span>{m.namn}</span></div>
                    )
                  })}
                </div>
              )
            })
          }
          </div>
        );
      }
      )}
    </div>
  )
};

export default ValjEnhet;
