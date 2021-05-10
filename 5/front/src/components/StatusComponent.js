import React, { Component } from "react";
import { StatusService } from "../services/StatusService";
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

 
export class StatusComponent extends Component {

    statusService: StatusService;

    constructor() {
        super();
        this.statusService = new StatusService();
        this.state = {status: []};
    }

    async componentDidMount() {
        let res = await this.statusService.getAllStatuses();
        // let res = await this.orderService.getAddressByID(1)
        this.setState({status: JSON.stringify(res)});
    }

    render() {
        return (
            <Modal.Dialog>
                <Modal.Header closeButton>
                    <Modal.Title>Status of your order</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <p>Waiting for seller to send...</p>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary">Close</Button>
                </Modal.Footer>
            </Modal.Dialog>
        );
    }
}
