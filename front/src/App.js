import React from 'react';
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link
} from 'react-router-dom';

import { Container, Navbar } from 'react-bootstrap';

import Home from './components/Home';
import IDE from './components/IDE';


function App() {
    return (
        <>
        <Navbar bg="light">
          <Navbar.Brand>MiniSQL</Navbar.Brand>
        </Navbar>

        <Container fluid="md">
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
        </>
    );
}

export default App;
