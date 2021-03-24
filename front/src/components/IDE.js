import React , {useState} from 'react';
import { useParams } from 'react-router-dom';
import {Row, Col, Form, Button} from 'react-bootstrap';

import MetaPane from './MetaPane';
import MetaOption from './MetaOption';
import ResultPane from './ResultPane';
import CodePane from './CodePane';


function IDE() {

  const { id} = useParams();

  return (
    <>
      <Row>
        <Col>
          <MetaPane />
        </Col>
      </Row>
      <br/>
      <Row>
        <Col>
          <ResultPane/>
        </Col>
      </Row>

      <Row>
        <Col>
          <MetaOption/>
        </Col>
      </Row>
      <hr/>
      <CodePane title="도메인" />
      <CodePane title="도메인" />
      <CodePane title="도메인" />
    </>
  );
}


export default IDE;
