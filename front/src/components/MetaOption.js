import React from 'react';
import {Form, Button} from 'react-bootstrap';


function MetaOption () {


  return (
    <Form>
      <div className="mb-12">
        <Form.Check inline type="checkbox" label="도메인 모델 생성" id="selectDomainModel"/>
        <Form.Check inline type="checkbox" label="검증(Annotation)적용" id="selectValidationModel" />
        <Form.Check inline type="checkbox" label="DAO" id="selectDao" />
        <Form.Check inline type="checkbox" label="Batis ResultMap" id="selectResultMap" />

        <Button size="sm">생성</Button>
      </div>
    </Form>

  );

};


export default MetaOption;
