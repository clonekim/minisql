import React from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from 'react-router-dom';

import { Container } from 'react-bootstrap';

import Home from './components/Home';
import IDE from './components/IDE';


function App() {
    return (
        <Container >
          <Router>
            <Switch>
              <Route path="/" exact>
                <Home />
              </Route>

              <Route path="/connect/:id">
                <IDE />
              </Route>
            </Switch>
          </Router>
        </Container>
    );
}

export default App;
