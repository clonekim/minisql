import React , {useState} from 'react';
import { useParams } from 'react-router-dom';
import {Row, Col, Form} from 'react-bootstrap';


const MetaPanel = () => {
  const [ form, setForm ] = useState({});

  return (
    <Form>
      <Form.Group >
        <Form.Label > 패키지명 </Form.Label>
        <Form.Control type="text" onChange={e => setForm['package']= e.target.value }/>
      </Form.Group>

      <Form.Group>
        <Form.Label> POJO </Form.Label>
        <Form.Control type="password" onChange={e => setForm['pojo'] = e.target.value}/>
      </Form.Group>

      <Form.Group>
        <Form.Label > SQL </Form.Label>
        <Form.Control as="textarea" onChange={e => setForm['sql'] = e.target.value} />
      </Form.Group>
    </Form>

  );



};



function IDE() {

  const { id} = useParams();

  return (
    <Row>
      <Col>
        <MetaPanel />
      </Col>
    </Row>
  );
}


export default IDE;
