import React from 'react';
import ReactDOM from 'react-dom';
import '@atlaskit/css-reset';

import WebFragments from 'websight-fragments-esm';

import Error from './Error.js';

const errorHeader = document.getElementById('websight-error-header').value;
const errorDescription = document.getElementById('websight-error-description').value;

const FooterFragment = () => (
    <WebFragments fragmentsKey='websight.global.page.footer' />
);

class App extends React.Component {
    render() {
        return (
            <>
                <Error header={errorHeader} description={errorDescription} />
                <FooterFragment />
            </>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('app-root'));