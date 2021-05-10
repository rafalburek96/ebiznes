import React, { Component } from "react";
import Table from "react-bootstrap/Table";
import { OrderedProductsService } from "../services/OrderedProductsService";
import { CategoryComponent } from "./CategoryComponent";

 
export class OrderedProductsComponent extends Component {

    orderedProductsService: OrderedProductsService;

    constructor() {
        super();
        this.orderedProductsService = new OrderedProductsService();
        this.state = {orderedProducts: []};
    }

    async componentDidMount() {
        let res = await this.orderedProductsService.getAllOrderedProducts();
        this.setState({orderedProducts: res});
        console.log('ORDERED: ', this.state.orderedProducts);
    }

    render() {
        let orderedProductsRows = this.state.orderedProducts.map( op => (
            <tr>
                <td>{ op.id }</td>
                <td>{ op.productID }</td>
                <td>{ op.productName }</td>
                <td>{ op.quantity }</td>
            </tr>
        ));

        return (
            <div>
                <CategoryComponent/>
                <div>
                <h5>Ordered Products</h5>
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                    </tr>
                    </thead>
                    <tbody>
                    { orderedProductsRows }
                    </tbody>
                </Table>
                </div>
            </div>
        );
    }
}
