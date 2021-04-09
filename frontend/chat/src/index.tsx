import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Login from './Login'
import reportWebVitals from './reportWebVitals';

import { useHistory, Route } from 'react-router-dom'
import { Router, Switch } from 'react-router'
import { Chat } from './Chat';
import { createBrowserHistory } from 'history';
import { Loading } from './Loading';

ReactDOM.render(
  <React.StrictMode>
    <Router history={createBrowserHistory()}>
      <Switch>
        <Route component={Chat} path="/chat" />
        <Route component={Login} path="/login" />
        <Route component={Loading} path="/" />
      </Switch>
    </Router>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
