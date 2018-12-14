import React from 'react';
import { Router,Route, Switch } from 'dva/router';
import IndexPage from './routes/IndexPage';

function RouterConfig({ history, app }) {
return (
    <Router history={history}>
        <Route path="/" exact component={IndexPage} />
    </Router>
  );
}

export default RouterConfig;


