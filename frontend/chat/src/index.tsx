import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Login from './Login'
import reportWebVitals from './reportWebVitals';

import { Link, Route, BrowserRouter as Router, Switch } from 'react-router-dom'
import { Chat } from './Chat';
import { Loading } from './Loading';

import { Provider } from 'mobx-react'
import { store } from './data/store';

ReactDOM.render(
  // <Router history={createHashHistory()}>
  //   <Route component={Loading} path='/' exact />
  //   <Route component={Login} path='/login' />
  //   <Route component={Chat} path='/chat' />
  // </Router>
  <Provider
    {...store}
  >
    <Router>
      <Switch>
        <Route path="/" component={Loading} exact>
        </Route>
        <Route path="/chat" component={Chat}>
        </Route>
        <Route path="/login" component={Login}>
        </Route>
      </Switch>
    </Router >
  </Provider>
  ,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
