// eslint-disable-next-line
import React, { Component } from "react";
import { BasketService } from "../services/BasketService";
import Table from 'react-bootstrap/Table';

const styling = { 
    width: '500px'
}

export class BasketComponent extends Component {

    basketService: BasketService;

    constructor() {
        super();
        this.basketService = new BasketService();
        this.state = {basket: []};
    }

    async componentDidMount() {
        let res = await this.basketService.getBasketByID(2);
        this.setState({basket: res});
    }

    render() {
        return (
            <div style={styling}>
                <h4>Basket</h4>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer ID</th>
                        <th>Basket date added</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{ this.state.basket.id }</td>
                        <td>{ this.state.basket.customerID }</td>
                        <td>{ this.state.basket.basketDateAdded }</td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        );
    }
}
