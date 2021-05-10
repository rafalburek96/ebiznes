import React, { Component } from "react";
import { AddressService } from "../services/AddressService";
import Table from 'react-bootstrap/Table';

const styling = { 
    width: '400px',
    background: '#fff'
};

export class AddressComponent extends Component {

    addressService: AddressService;

    constructor(props) {
        super(props);
        this.addressService = new AddressService();
        this.state = {address: []};
    }

    async componentDidMount() {
        let res = await this.addressService.getAddressByID(1)
        this.setState({address: res});
    }

    render() {
        return(
            <div style={styling}>
                <h5>Address</h5>
                <Table striped bordered hover size="sm">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Street Name</th>
                        <th>Flat Number</th>
                        <th>Zip-Code</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{ this.state.address.id }</td>
                        <td>{ this.state.address.street }</td>
                        <td>{ this.state.address.flatNumber }</td>
                        <td>{ this.state.address.zipCode }</td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}
