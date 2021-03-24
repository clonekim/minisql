import React , {useState} from 'react';
import { Form, Button, Col, Row} from 'react-bootstrap';


function MetaPane(){
  const [ form, setForm ] = useState({});

  return (
    <Form>

      <Row>
        <Col>
          <Form.Group >
            <Form.Label > 패키지명 </Form.Label>
            <Form.Control type="text" onChange={e => setForm['package']= e.target.value }/>
          </Form.Group>
        </Col>
        <Col>
          <Form.Group>
            <Form.Label> POJO </Form.Label>
            <Form.Control type="password" onChange={e => setForm['pojo'] = e.target.value}/>
          </Form.Group>
        </Col>
      </Row>

      <Form.Group>
        <Form.Label > SQL </Form.Label>
        <Row>
          <Col sm="11">
            <Form.Control as="textarea" rows="15" onChange={e => setForm['sql'] = e.target.value} />
          </Col>
          <Col sm="1">
            <Button className="float-right" variant="outline-primary">Run</Button>
          </Col>
        </Row>
      </Form.Group>

    </Form>

  );

};


export default MetaPane;
