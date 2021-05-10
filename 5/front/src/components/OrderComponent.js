// eslint-disable-next-line
import React, { Component } from "react";
import { OrderService } from "../services/OrderService";
import Table from "react-bootstrap/Table";
import {CategoryComponent} from "./CategoryComponent";
 
const styling = {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center'
};

export class OrderComponent extends Component {

    orderService: OrderService;

    constructor() {
        super();
        this.orderService = new OrderService();
        this.state = {order: []};
    }

    async componentDidMount() {
        let res = await this.orderService.getAllOrders()
        this.setState({order: res});
    }

    render() {

        let orderTable = (
            <div style={styling}>
                <h5>Order</h5>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Customer ID</th>
                        <th>Delivery Date</th>
                        <th>Order Status Code</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>{ this.state.order.id }</td>
                        <td>{ this.state.order.customerID }</td>
                        <td>{ this.state.order.deliveryDate }</td>
                        <td>{ this.state.order.orderStatusCode }</td>
                    </tr>
                    </tbody>
                </Table>
            </div>
        );

        return (
            <div>
                <CategoryComponent></CategoryComponent>
                { orderTable }
            </div>

        );
    }
}
