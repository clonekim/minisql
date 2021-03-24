import React from 'react';
import { Form, Button, Col} from 'react-bootstrap';


function CodePane({title}) {

  return (
    <Form>
      <Form.Group>
        <Form.Label>{title}</Form.Label>
        <Form.Row>
          <Col md="11" sm="11">
            <Form.Control as="textarea" rows="10"></Form.Control>
          </Col>
          <Col md="1" sm="1">
            <Button className="float-right" variant="outline-info">Copy</Button>
          </Col>
        </Form.Row>
      </Form.Group>
    </Form>
  );

};

export default CodePane;
