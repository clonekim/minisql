import React , {useState, useEffect } from 'react';
import { Card, Button, Form, Row, Col, Modal} from 'react-bootstrap';


const vendors = ['ORACLE', 'MySQL', 'PostgreSQL'];

const DatabasePanel = ({name, vendor}) => {

  return(
    <Card>
      <Card.Body>
        <Card.Title>{name}</Card.Title>
        <Card.Subtitle>{vendor}</Card.Subtitle>
        <Card.Text>
        </Card.Text>
        <Button variant="primary" >connect</Button>&nbsp;
        <Button variant="secondary" >edit</Button>&nbsp;
      </Card.Body>
    </Card>
  );
};

const BlankPanel = ({handle}) => {

  return(
    <Card>
      <Card.Body>
        <Card.Text>
             새로운 데이터베이스를 연결하세요.
        </Card.Text>

        <Button variant="primary" onClick={() => handle(true)}>+</Button>&nbsp;
      </Card.Body>
    </Card>
  );
};


const FormPanel = ({handle}) => {
    const [vendor, setVendor] = useState(null);
  const [url, setUrl ] = useState(null);
  const [username, setUsername ] = useState(null);
  const [password, setPassword ] = useState(null);

  return (
    <>
      <Modal.Dialog>
        <Modal.Header>

          <Modal.Title>
          New Database Connection
            </Modal.Title>
            <button type="button" className="close" onClick={() => handle(false) }>
              <span aria-hidden="true">x</span>
            </button>
          </Modal.Header>
          <Modal.Body>

            <Form>
              <Form.Group as={Row}>
                <Form.Label  column sm="4" controlId="jdbcUrl">
                 Vendor 
                </Form.Label>
                <Col sm="8">
                  <Form.Control as="select" onChange={e => setVendor(e.target.value)} >
                      <option/>
                      {vendors.map(i => <option>{i}</option>)}
                  </Form.Control>
                </Col>
              </Form.Group>


            <Form.Group as={Row}>
              <Form.Label  column sm="4" controlId="jdbcUrl">
                JDBC URL
              </Form.Label>
              <Col sm="8">
                <Form.Control as="textarea" onChange={e => setUrl(e.target.value)} />
              </Col>
            </Form.Group>

            <Form.Group as={Row}>
              <Form.Label  column sm="4">
                Username
              </Form.Label>
              <Col sm="8">
                <Form.Control type="text" onChange={e => setUsername(e.target.value)}/>
              </Col>
            </Form.Group>

            <Form.Group as={Row}>
              <Form.Label column sm="4">
                Password
              </Form.Label>
              <Col sm="8">
                <Form.Control type="password" onChange={e => setPassword(e.target.value)}/>
              </Col>
            </Form.Group>

          </Form>
        </Modal.Body>

        <Modal.Footer>
          <Button>Save</Button>
        </Modal.Footer>
      </Modal.Dialog>

    </>
  );
};

function Vendor() {
    const [dbs, setDbs] = useState([
        {}
    ]);

    const [isShow, setShow] = useState(false)

    useEffect(() => {
        console.log('here')
    }, [dbs])

    return(
        <div>
            {isShow && <FormPanel handle={setShow} />}
            {!isShow &&
             <Row>
                 {dbs.map(item => <Col sm="3">
                                      {!item.vendor && <BlankPanel handle={setShow}/>}
                                      {item.vendor && <DatabasePanel {...item}/> }
                                  </Col>
                         )}
             </Row> }
        </div>
    );

}

        export default Vendor;
