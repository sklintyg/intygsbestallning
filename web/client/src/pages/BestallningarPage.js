import React from 'react';

import BestallningarFilter from './components/Bestallningar/BestallningarFilter';
import BestallningarTable from './components/Bestallningar/BestallningarTable';

const BestallningarPage = () => {
    return (
        <div>
            <BestallningarFilter />
            <BestallningarTable />
        </div>
    )
};

export default BestallningarPage;